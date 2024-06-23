package com.example.storage_service.consumer;

import com.example.storage_service.dto.CommentDeletionDTO;
import com.example.storage_service.dto.FileDeletionDTO;
import com.example.storage_service.dto.PostFileDeletionDTO;
import com.example.storage_service.repository.IFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class FileServiceListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceListener.class);

    private final IFileRepository fileRepository;

    public FileServiceListener(IFileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @RabbitListener(queues = "${rabbitmq.queue.delete-file-queue}")
    public void handleFileDelete(FileDeletionDTO event) {
        if (event == null || event.getFileId() == null) {
            LOGGER.error("Received null event or fileId");
            return;
        }
        LOGGER.info("Deleting file with ID: {}", event.getFileId());
        fileRepository.deleteById(event.getFileId());
    }

    @RabbitListener(queues = "${rabbitmq.queue.delete-comment-queue}")
    public void handleCommentDelete(CommentDeletionDTO event) {
        if (event == null || event.getAttachmentIdsToDelete() == null) {
            LOGGER.error("Received null event or attachmentIds");
            return;
        }
        LOGGER.info("Deleting attachments for comment: {}", event.getAttachmentIdsToDelete());
        for (Long fileId : event.getAttachmentIdsToDelete()) {
            fileRepository.deleteById(fileId);
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.delete-post-file-queue}")
    public void handlePostFileDelete(FileDeletionDTO event) {
        if (event == null || event.getFileId() == null) {
            LOGGER.error("Received null event or attachmentIds");
            return;
        }
        LOGGER.info("Deleting attachment with id: {}", event.getFileId());
        fileRepository.deleteById(event.getFileId());
    }

    @RabbitListener(queues = "${rabbitmq.queue.delete-post-all-file-queue}")
    public void handlePostAllFileDelete(PostFileDeletionDTO event) {
        if (event == null || event.getAttachmentIdsToDelete() == null) {
            LOGGER.error("Received null event or attachmentIds");
            return;
        }
        LOGGER.info("Deleting all attachments for post: {}", event.getAttachmentIdsToDelete());
        for (Long fileId : event.getAttachmentIdsToDelete()) {
            fileRepository.deleteById(fileId);
        }
    }
}
