package com.example.storage_service.service;

import com.example.storage_service.entity.File;
import com.example.storage_service.mapper.FileMapper;
import com.example.storage_service.repository.IFileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileService {
    private IFileRepository repository;
    private FileMapper fileMapper;

    public FileService(IFileRepository repository, FileMapper fileMapper) {
        this.repository = repository;
        this.fileMapper = fileMapper;
    }

    public Long saveFile(MultipartFile file) throws IOException {
        File newFile = fileMapper.mapMultipartFileToFile(file);
        newFile = repository.save(newFile);
        return newFile.getId();
    }

    public File getFile(Long fileId) throws Exception {
        return repository.findById(fileId)
                .orElseThrow(() -> new Exception());
    }
}
