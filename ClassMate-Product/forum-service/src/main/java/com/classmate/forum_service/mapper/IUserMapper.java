package com.classmate.forum_service.mapper;

import com.classmate.forum_service.dto.user.UserDTO;
import com.classmate.forum_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    @Mapping(source = "userId", target = "userId")
    UserDTO toUserDTO(User user);

    @Mapping(source = "userId", target = "userId")
    User toUser(UserDTO userDTO);
}
