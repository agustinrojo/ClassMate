package com.example.storage_service.consumer;

import com.example.storage_service.dto.CommentDeletionDTO;
import com.example.storage_service.dto.FileDeletionDTO;
import com.example.storage_service.dto.PostFileDeletionDTO;
import com.example.storage_service.repository.IFileRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class FileServiceListener {

    private final IFileRepository fileRepository;

    public FileServiceListener(IFileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @RabbitListener(queues = "deleteFileQueue")
    public void handleFileDelete(FileDeletionDTO event) {
        fileRepository.deleteById(event.getFileId());
    }

    @RabbitListener(queues = "deleteCommentQueue")
    public void handleCommentDelete(CommentDeletionDTO event) {
        for (Long fileId : event.getAttachmentIdsToDelete()) {
            fileRepository.deleteById(fileId);
        }
    }

    @RabbitListener(queues = "deletePostQueue")
    public void handlePostFileDelete(PostFileDeletionDTO event) {
        for (Long fileId : event.getAttachmentIdsToDelete()) {
            fileRepository.deleteById(fileId);
        }
    }
}