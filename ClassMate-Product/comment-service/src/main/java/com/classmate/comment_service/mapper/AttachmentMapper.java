package com.classmate.comment_service.mapper;

import com.classmate.comment_service.dto.filedtos.FileDTO;
import com.classmate.comment_service.entity.Attachment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {
    FileDTO toFileDTO(Attachment attachment);
}
