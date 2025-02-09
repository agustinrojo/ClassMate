package com.classmate.forum_service.mapper;

import com.classmate.forum_service.dto.delete_request.DeleteRequestDTO;
import com.classmate.forum_service.entity.DeleteRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeleteRequestMapper {
    @Mapping(source = "reporterId", target = "reporterId")
    DeleteRequestDTO mapToDTO(DeleteRequest deleteRequest);
    @Mapping(source = "reporterId", target = "reporterId")
    DeleteRequest mapToEntity(DeleteRequestDTO deleteRequestDTO);
}

