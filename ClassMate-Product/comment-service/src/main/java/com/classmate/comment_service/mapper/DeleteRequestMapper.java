package com.classmate.comment_service.mapper;

import com.classmate.comment_service.dto.delete_request.DeleteRequestDTO;
import com.classmate.comment_service.entity.DeleteRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeleteRequestMapper {
    @Mapping(source = "reporterId", target = "reporterId")
    DeleteRequestDTO mapToDTO(DeleteRequest deleteRequest);
    @Mapping(source = "reporterId", target = "reporterId")
    DeleteRequest mapToEntity(DeleteRequestDTO deleteRequestDTO);
}
