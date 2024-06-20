package com.example.storage_service.service.impl;

import com.example.storage_service.dto.FileResponseDTO;
import com.example.storage_service.entity.File;
import com.example.storage_service.exception.FileConversionException;
import com.example.storage_service.exception.FileNotFoundException;
import com.example.storage_service.repository.IFileRepository;
import com.example.storage_service.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
public class FileServiceImpl implements FileService {
    private final IFileRepository repository;


    public FileServiceImpl(IFileRepository repository) {
        this.repository = repository;
    }
    @Override
    public FileResponseDTO saveFile(MultipartFile file){
        File newFile = mapMultipartFileToFile(file);
        newFile = repository.save(newFile);
        return mapFileToFileResponseDTO(newFile);
    }
    @Override
    public File getFile(Long fileId)  {
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
        FileResponseDTO fileResponseDTO = new FileResponseDTO();
        fileResponseDTO.setFileId(file.getId());
        return fileResponseDTO;
    }

}
