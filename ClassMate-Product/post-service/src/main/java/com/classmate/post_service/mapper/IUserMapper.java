package com.classmate.post_service.mapper;

import com.classmate.post_service.dto.user.UserDTO;
import com.classmate.post_service.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    UserDTO mapUserToUserDTO(User user);
    User mapUserDTOToUser(UserDTO userDTO);


}
