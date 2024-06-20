package com.example.storage_service.mapper;

import com.example.storage_service.dto.FileDTO;
import com.example.storage_service.entity.File;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IFileMapper {
    FileDTO mapFileToFileDTO(File file);
}
