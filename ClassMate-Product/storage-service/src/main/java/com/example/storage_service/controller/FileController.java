package com.example.storage_service.controller;

import com.example.storage_service.dto.FileResponseDTO;
import com.example.storage_service.entity.File;
import com.example.storage_service.service.FileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService service;
    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    public FileController(FileService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<FileResponseDTO> uploadFile(@RequestParam("file") MultipartFile file) {
        LOGGER.info("Uploading file: {}", file.getOriginalFilename());
        FileResponseDTO fileResponseDTO = service.saveFile(file);
        return new ResponseEntity<>(fileResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        LOGGER.info("Downloading file with id: {}", fileId);
        File file = service.getFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(new ByteArrayResource(file.getBytes()));
    }
}
