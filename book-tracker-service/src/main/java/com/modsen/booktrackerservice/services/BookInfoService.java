package com.modsen.booktrackerservice.services;

import com.modsen.booktrackerservice.exceptions.BookIsNullException;
import com.modsen.booktrackerservice.repositories.BookInfoRepository;
import com.modsen.commonmodels.enums.entityAttributes.BookInfoStatus;
import com.modsen.commonmodels.models.dtos.BookInfoDto;
import com.modsen.commonmodels.models.entities.BookInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookInfoService {

    private final BookInfoRepository bookInfoRepository;

    public BookInfo fillInBookInfo(BookInfoDto bookInfoDto) {
        Long bookId = bookInfoDto.getBookId();
        if (bookId == null) {
            throw new BookIsNullException("Book id is null");
        }

        return new BookInfo(bookId, BookInfoStatus.AVAILABLE, null, null);
    }

    public boolean softDeleteBookInfo(Long bookId) {
        System.out.println("Deleting book");
        Optional<BookInfo> bookInfoOptional = bookInfoRepository.findByBookId(bookId);
        if (bookInfoOptional.isPresent()) {
            BookInfo bookInfo = bookInfoOptional.get();
            bookInfo.setBookInfoStatus(BookInfoStatus.DELETED);
            bookInfoRepository.save(bookInfo);
            return true;
        }
        return false;
    }
}
