package com.example.storage_service.mapper;

import com.example.storage_service.entity.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring")
public interface FileMapper {

    File mapMultipartFileToFile(MultipartFile multipartFile);

    MultipartFile mapFileToMultipartFile(File file);
}
