package com.modsen.booktrackerservice.services;

import com.modsen.booktrackerservice.repositories.BookInfoRepository;
import com.modsen.commonmodels.enums.attributes.BookInfoStatus;
import com.modsen.commonmodels.exceptions.ObjectNotFoundException;
import com.modsen.commonmodels.models.dtos.BookInfoDto;
import com.modsen.commonmodels.models.entities.BookInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookInfoServiceTest {

    @Mock
    private BookInfoRepository bookInfoRepository;

    @InjectMocks
    private BookInfoService bookInfoService;

    private BookInfoDto bookInfoDto;
    private BookInfo bookInfo;

    @BeforeEach
    void setup() {
        bookInfoDto = new BookInfoDto(1L);
        bookInfo = new BookInfo(1L, BookInfoStatus.AVAILABLE, null, null);
    }

    @Test
    void createBook_shouldSaveBookInfo() throws ObjectNotFoundException {
        when(bookInfoRepository.save(any(BookInfo.class))).thenReturn(bookInfo);

        BookInfo createdBook = bookInfoService.createBook(bookInfoDto);

        assertNotNull(createdBook);
        assertEquals(bookInfo.getBookId(), createdBook.getBookId());
        verify(bookInfoRepository).save(any(BookInfo.class));
    }

    @Test
    void getAvailableBooks_shouldReturnAvailableBooks() {
        when(bookInfoRepository.findByBookInfoStatus(BookInfoStatus.AVAILABLE)).thenReturn(List.of(bookInfo));

        List<BookInfo> availableBooks = bookInfoService.getAvailableBooks();

        assertNotNull(availableBooks);
        assertEquals(1, availableBooks.size());
        assertEquals(BookInfoStatus.AVAILABLE, availableBooks.get(0).getBookInfoStatus());
        verify(bookInfoRepository).findByBookInfoStatus(BookInfoStatus.AVAILABLE);
    }

    @Test
    void updateBookStatus_shouldUpdateStatusAndSave() throws ObjectNotFoundException {
        when(bookInfoRepository.findById(1L)).thenReturn(Optional.of(bookInfo));
        when(bookInfoRepository.save(any(BookInfo.class))).thenReturn(bookInfo);

        BookInfo updatedBook = bookInfoService.updateBookStatus(1L, BookInfoStatus.BORROWED);

        assertNotNull(updatedBook);
        assertEquals(BookInfoStatus.BORROWED, updatedBook.getBookInfoStatus());
        assertNotNull(updatedBook.getBorrowedAt());
        assertNotNull(updatedBook.getReturnDue());
        verify(bookInfoRepository).save(any(BookInfo.class));
    }

    @Test
    void updateBookStatus_shouldThrowExceptionWhenBookNotFound() {
        when(bookInfoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> bookInfoService.updateBookStatus(1L, BookInfoStatus.BORROWED));
        verify(bookInfoRepository, never()).save(any(BookInfo.class));
    }

    @Test
    void deleteBookInfo_shouldSoftDeleteBook() throws ObjectNotFoundException {
        when(bookInfoRepository.findById(1L)).thenReturn(Optional.of(bookInfo));
        bookInfo.setBookInfoStatus(BookInfoStatus.DELETED);

        bookInfoService.deleteBookInfo(1L);

        verify(bookInfoRepository).save(any(BookInfo.class));
        assertEquals(BookInfoStatus.DELETED, bookInfo.getBookInfoStatus());
    }

    @Test
    void deleteBookInfo_shouldThrowExceptionWhenBookNotFound() {
        when(bookInfoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> bookInfoService.deleteBookInfo(1L));
        verify(bookInfoRepository, never()).save(any(BookInfo.class));
    }
}
