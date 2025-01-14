package com.modsen.bookstorageservice.mappers;

import com.modsen.bookstorageservice.models.dtos.BookDTO;
import com.modsen.bookstorageservice.models.entities.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Service;

@Service
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {
    BookDTO toBookDTO(Book book);
}
