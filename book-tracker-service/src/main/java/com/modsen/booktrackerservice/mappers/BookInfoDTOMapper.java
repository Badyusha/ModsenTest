package com.modsen.booktrackerservice.mappers;

import com.modsen.booktrackerservice.models.dtos.BookInfoDTO;
import com.modsen.booktrackerservice.models.entities.BookInfo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Service;

@Service
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookInfoDTOMapper {
    BookInfo toBookInfo(BookInfoDTO bookInfoDTO);
}
