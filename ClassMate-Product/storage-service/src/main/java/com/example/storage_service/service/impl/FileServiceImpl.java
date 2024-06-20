package com.example.storage_service.service.impl;

import com.example.storage_service.dto.FileResponseDTO;
import com.example.storage_service.entity.File;
import com.example.storage_service.exception.FileConversionException;
import com.example.storage_service.exception.FileNotFoundException;
import com.example.storage_service.repository.IFileRepository;
import com.example.storage_service.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {

    private final IFileRepository repository;
    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);

    public FileServiceImpl(IFileRepository repository) {
        this.repository = repository;
    }

    @Override
    public FileResponseDTO saveFile(MultipartFile file) {
        LOGGER.info("Saving file: {}", file.getOriginalFilename());
        File newFile = mapMultipartFileToFile(file);
        newFile = repository.save(newFile);
        return mapFileToFileResponseDTO(newFile);
    }

    @Override
    public File getFile(Long fileId) {
        LOGGER.info("Fetching file with id: {}", fileId);
        return repository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found"));
    }

    private File mapMultipartFileToFile(MultipartFile multipartFile) {
        File file = new File();
        try {
            file.setBytes(multipartFile.getBytes());
        } catch (IOException e) {
            throw new FileConversionException("Error al obtener los bytes del archivo", e);
        }
        file.setOriginalFilename(multipartFile.getOriginalFilename());
        file.setContentType(multipartFile.getContentType());
        return file;
    }

    private FileResponseDTO mapFileToFileResponseDTO(File file) {
        return FileResponseDTO.builder()
                .fileId(file.getId())
                .build();
    }
}
