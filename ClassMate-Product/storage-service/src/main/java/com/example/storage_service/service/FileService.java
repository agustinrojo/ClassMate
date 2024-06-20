package com.example.storage_service.service;

import com.example.storage_service.dto.FileResponseDTO;
import com.example.storage_service.entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    FileResponseDTO saveFile(MultipartFile files);

    File getFile(Long fileId) ;
}
