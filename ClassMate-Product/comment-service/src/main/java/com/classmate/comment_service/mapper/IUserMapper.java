package com.classmate.comment_service.mapper;

import com.classmate.comment_service.dto.user.UserDTO;
import com.classmate.comment_service.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserMapper {

    UserDTO mapUserToUserDTO(User user);
}
