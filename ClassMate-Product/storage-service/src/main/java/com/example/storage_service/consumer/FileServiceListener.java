package com.example.storage_service.consumer;

import com.example.storage_service.dto.FileDeletionDTO;
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
}