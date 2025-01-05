package com.modsen.booktrackerservice.services;

import com.modsen.booktrackerservice.exceptions.BookIsNullException;
import com.modsen.commonmodels.enums.entityAttributes.Status;
import com.modsen.commonmodels.models.dtos.BookInfoDto;
import com.modsen.commonmodels.models.entities.BookInfo;
import org.springframework.stereotype.Component;

@Component
public class BookInfoService {
    public BookInfo fillInBookInfo(BookInfoDto bookInfoDto) {
        Long bookId = bookInfoDto.getBookId();
        if (bookId == null) {
            throw new BookIsNullException("Book id is null");
        }

        return new BookInfo(bookId, Status.AVAILABLE, null, null);
    }
}
