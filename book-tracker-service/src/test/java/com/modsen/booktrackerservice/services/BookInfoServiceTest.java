package com.modsen.booktrackerservice.services;

import com.modsen.booktrackerservice.enums.attributes.BookInfoStatus;
import com.modsen.booktrackerservice.enums.attributes.BorrowHistoryStatus;
import com.modsen.booktrackerservice.mappers.BookInfoDTOMapper;
import com.modsen.booktrackerservice.mappers.BookInfoMapper;
import com.modsen.booktrackerservice.models.dtos.BookInfoDTO;
import com.modsen.booktrackerservice.models.entities.BookInfo;
import com.modsen.booktrackerservice.models.entities.BorrowHistory;
import com.modsen.booktrackerservice.repositories.BookInfoRepository;
import com.modsen.booktrackerservice.repositories.BorrowHistoryRepository;
import com.modsen.commonmodels.exceptions.ObjectNotFoundException;
import com.modsen.commonmodels.exceptions.PermissionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookInfoServiceTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private BookInfoMapper bookInfoMapper;

    @Mock
    private BookInfoDTOMapper bookInfoDTOMapper;

    @Mock
    private BookInfoRepository bookInfoRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private BorrowHistoryRepository borrowHistoryRepository;

    @InjectMocks
    private BookInfoService bookInfoService;

    private BookInfoDTO bookInfoDTO;
    private BookInfo bookInfo;
    private BorrowHistory borrowHistory;

    @BeforeEach
    void setUp() {
        bookInfoDTO = new BookInfoDTO();
        bookInfoDTO.setIsbn("1234567890");

        bookInfo = new BookInfo();
        bookInfo.setIsbn("1234567890");
        bookInfo.setBookInfoStatus(BookInfoStatus.AVAILABLE);

        borrowHistory = new BorrowHistory(1L, "1234567890");
        borrowHistory.setBorrowHistoryStatus(BorrowHistoryStatus.BORROWED);
    }

    @Test
    void createBook_success() {
        when(bookInfoDTOMapper.toBookInfo(bookInfoDTO)).thenReturn(bookInfo);
        when(bookInfoRepository.save(bookInfo)).thenReturn(bookInfo);
        when(bookInfoMapper.toBookInfoDTO(bookInfo)).thenReturn(bookInfoDTO);

        BookInfoDTO result = bookInfoService.createBook(bookInfoDTO);

        verify(bookInfoRepository).save(bookInfo);
        assertEquals(bookInfoDTO, result);
    }

    @Test
    void getAvailableBooks_success() {
        when(bookInfoRepository.findByBookInfoStatus(BookInfoStatus.AVAILABLE)).thenReturn(List.of(bookInfo));
        when(bookInfoMapper.toBookInfoDTO(bookInfo)).thenReturn(bookInfoDTO);

        List<BookInfoDTO> result = bookInfoService.getAvailableBooks();

        assertEquals(1, result.size());
        assertEquals(bookInfoDTO, result.get(0));
    }

    @Test
    void borrowBook_success() throws ObjectNotFoundException {
        // Arrange
        String token = "validToken";
        String isbn = "1234567890";
        Long userId = 1L;

        BookInfo bookInfo = new BookInfo();
        bookInfo.setIsbn(isbn);
        bookInfo.setBookInfoStatus(BookInfoStatus.AVAILABLE);

        BookInfo savedBookInfo = new BookInfo();
        savedBookInfo.setIsbn(isbn);
        savedBookInfo.setBookInfoStatus(BookInfoStatus.BORROWED);

        BookInfoDTO bookInfoDTO = new BookInfoDTO();

        when(tokenService.extractUserId(token)).thenReturn(userId);
        when(borrowHistoryRepository.isUserBorrowBook(userId, isbn)).thenReturn(false);
        when(bookInfoRepository.findByIsbn(isbn)).thenReturn(Optional.of(bookInfo));
        when(bookInfoRepository.save(bookInfo)).thenReturn(savedBookInfo);
        when(bookInfoMapper.toBookInfoDTO(savedBookInfo)).thenReturn(bookInfoDTO);

        BookInfoDTO result = bookInfoService.borrowBook(token, isbn);

        verify(borrowHistoryRepository).save(any(BorrowHistory.class));
        verify(bookInfoRepository).save(bookInfo);
        assertEquals(bookInfoDTO, result);
    }

    @Test
    void borrowBook_bookAlreadyBorrowed() {
        when(tokenService.extractUserId(any())).thenReturn(1L);
        when(borrowHistoryRepository.isUserBorrowBook(1L, "1234567890")).thenReturn(true);

        assertThrows(PermissionException.class, () -> bookInfoService.borrowBook("token", "1234567890"));
    }

    @Test
    void returnBook_success() throws ObjectNotFoundException {
        // Arrange
        String token = "validToken";
        String isbn = "1234567890";
        Long userId = 1L;

        BorrowHistory borrowHistory = new BorrowHistory();
        borrowHistory.setUserId(userId);
        borrowHistory.setIsbn(isbn);
        borrowHistory.setBorrowHistoryStatus(BorrowHistoryStatus.BORROWED);

        BookInfo bookInfo = new BookInfo();
        bookInfo.setIsbn(isbn);
        bookInfo.setBookInfoStatus(BookInfoStatus.BORROWED);

        BookInfo savedBookInfo = new BookInfo();
        savedBookInfo.setIsbn(isbn);
        savedBookInfo.setBookInfoStatus(BookInfoStatus.AVAILABLE);

        BookInfoDTO bookInfoDTO = new BookInfoDTO();

        when(tokenService.extractUserId(token)).thenReturn(userId);
        when(borrowHistoryRepository.findByUserIdAndIsbn(userId, isbn)).thenReturn(Optional.of(borrowHistory));
        when(bookInfoRepository.findByIsbn(isbn)).thenReturn(Optional.of(bookInfo));
        when(bookInfoRepository.save(bookInfo)).thenReturn(savedBookInfo);
        when(bookInfoMapper.toBookInfoDTO(savedBookInfo)).thenReturn(bookInfoDTO);

        // Act
        BookInfoDTO result = bookInfoService.returnBook(token, isbn);

        // Assert
        verify(borrowHistoryRepository).save(borrowHistory);
        verify(bookInfoRepository).save(bookInfo);
        assertEquals(bookInfoDTO, result);
    }

    @Test
    void returnBook_notFound() {
        when(tokenService.extractUserId(any())).thenReturn(1L);
        when(borrowHistoryRepository.findByUserIdAndIsbn(1L, "1234567890"))
                .thenReturn(Optional.empty());

        assertThrows(PermissionException.class, () -> bookInfoService.returnBook("token", "1234567890"));
    }

    @Test
    void deleteBookInfo_success() throws ObjectNotFoundException {
        when(bookInfoRepository.findByIsbn("1234567890")).thenReturn(Optional.of(bookInfo));

        bookInfoService.deleteBookInfo("1234567890");

        verify(kafkaTemplate).send(eq("book-deletion-response-topic"), eq("1234567890"));
        verify(bookInfoRepository).save(bookInfo);
        assertEquals(BookInfoStatus.DELETED, bookInfo.getBookInfoStatus());
    }

    @Test
    void deleteBookInfo_bookNotFound() {
        when(bookInfoRepository.findByIsbn("1234567890")).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> bookInfoService.deleteBookInfo("1234567890"));
    }
}
