package com.modsen.booktrackerservice.services;

import com.modsen.booktrackerservice.repositories.BookInfoRepository;
import com.modsen.commonmodels.enums.attributes.BookInfoStatus;
import com.modsen.commonmodels.models.dtos.BookInfoDto;
import com.modsen.commonmodels.models.entities.BookInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookInfoService {

    private final BookInfoRepository bookInfoRepository;

    public ResponseEntity<BookInfo> getCreateBookInfoResponseEntity(BookInfoDto bookInfoDto) {
        BookInfo bookInfo = bookInfoDto.toBookInfo();
        if(bookInfo == null) {
            return ResponseEntity.notFound().build();
        }

        BookInfo savedBookInfo = bookInfoRepository.save(bookInfo);
        return ResponseEntity.ok(savedBookInfo);
    }

    public ResponseEntity<List<BookInfo>> getAvailableBooksResponseEntity() {
        List<BookInfo> availableBooks = bookInfoRepository.findByBookInfoStatus(BookInfoStatus.AVAILABLE);
        return ResponseEntity.ok(availableBooks);
    }

    public ResponseEntity<BookInfo> getUpdateBookStatusResponseEntity(Long id, BookInfoStatus bookInfoStatus) {
        return bookInfoRepository.findById(id)
                .map(bookInfo -> {
                    bookInfo.setBookInfoStatus(bookInfoStatus);
                    if (bookInfoStatus.equals(BookInfoStatus.BORROWED)) {
                        bookInfo.setBorrowedAt(LocalDateTime.now());
                        bookInfo.setReturnDue(LocalDateTime.now().plusWeeks(2));
                    } else {
                        bookInfo.setBorrowedAt(null);
                        bookInfo.setReturnDue(null);
                    }
                    return ResponseEntity.ok(bookInfoRepository.save(bookInfo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Void> getDeleteBookInfoResponseEntity(Long id) {
        if (softDeleteBookInfo(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    public boolean softDeleteBookInfo(Long bookId) {
        Optional<BookInfo> bookInfoOptional = bookInfoRepository.findById(bookId);
        if (bookInfoOptional.isEmpty()) {
            return false;
        }

        BookInfo bookInfo = bookInfoOptional.get();
        bookInfo.setBookInfoStatus(BookInfoStatus.DELETED);
        bookInfoRepository.save(bookInfo);

        return true;
    }
}
