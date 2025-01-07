package com.modsen.booktrackerservice.services;

import com.modsen.booktrackerservice.repositories.BookInfoRepository;
import com.modsen.commonmodels.enums.attributes.BookInfoStatus;
import com.modsen.commonmodels.exceptions.ObjectNotFoundException;
import com.modsen.commonmodels.models.dtos.BookInfoDto;
import com.modsen.commonmodels.models.entities.BookInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookInfoService {

    private final BookInfoRepository bookInfoRepository;

    public BookInfo createBook(BookInfoDto bookInfoDto) throws ObjectNotFoundException {
        BookInfo bookInfo = bookInfoDto.toBookInfo();
        return bookInfoRepository.save(bookInfo);
    }

    public List<BookInfo> getAvailableBooks() {
        return bookInfoRepository.findByBookInfoStatus(BookInfoStatus.AVAILABLE);
    }

    public BookInfo updateBookStatus(Long id, BookInfoStatus bookInfoStatus) throws ObjectNotFoundException {
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
                    return bookInfoRepository.save(bookInfo);
                })
                .orElseThrow(() ->
                new ObjectNotFoundException("Book info cannot be updated. Book info with provided id does not exist"));
    }

    public void deleteBookInfo(Long id) throws ObjectNotFoundException {
        softDeleteBookInfo(id);
    }

    public void softDeleteBookInfo(Long bookId) throws ObjectNotFoundException {
        Optional<BookInfo> bookInfoOptional = bookInfoRepository.findById(bookId);
        if (bookInfoOptional.isEmpty()) {
            throw new ObjectNotFoundException("Book info cannot be found. Book info with provided id does not exist");
        }

        BookInfo bookInfo = bookInfoOptional.get();
        bookInfo.setBookInfoStatus(BookInfoStatus.DELETED);
        bookInfoRepository.save(bookInfo);
    }
}
