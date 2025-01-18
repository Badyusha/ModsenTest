package com.modsen.userstorage.mappers;

import com.modsen.userstorage.models.dtos.UserDTO;
import com.modsen.userstorage.models.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Service;

@Service
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserDTOMapper {
    User toBook(UserDTO bookDTO);
}
