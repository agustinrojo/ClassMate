package com.classmate.post_service.mapper;

import com.classmate.post_service.dto.filedtos.FileDTO;
import com.classmate.post_service.entity.Attachment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {
    FileDTO toFileDTO(Attachment attachment);
}
