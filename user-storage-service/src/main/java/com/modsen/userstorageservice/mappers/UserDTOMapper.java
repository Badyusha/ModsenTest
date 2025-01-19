package com.modsen.userstorageservice.mappers;

import com.modsen.userstorageservice.models.dtos.UserDTO;
import com.modsen.userstorageservice.models.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Service;

@Service
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserDTOMapper {
    User toBook(UserDTO bookDTO);
}
