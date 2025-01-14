package com.modsen.bookstorageservice.services;

import com.modsen.bookstorageservice.models.dtos.BookDTO;
import com.modsen.bookstorageservice.repositories.BookRepository;
import com.modsen.commonmodels.enums.attributes.CreationStatus;
import com.modsen.commonmodels.enums.kafka.KafkaTopic;
import com.modsen.commonmodels.exceptions.ObjectNotFoundException;
import com.modsen.commonmodels.models.entities.Book;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private BookService bookService;

    private BookDTO bookDTO;

    @BeforeEach
    void setup() {
        bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setIsbn("1234567");
        bookDTO.setCreationStatus(CreationStatus.EXISTS);
    }

    @Test
    void createBook_shouldSaveBookAndSendMessage() {
        when(bookRepository.save(any(Book.class))).thenReturn(bookDTO);

        BookDTO createdBook = bookService.createBook(bookDTO);

        assertNotNull(createdBook);
        assertEquals(CreationStatus.EXISTS, createdBook.getCreationStatus());
        verify(bookRepository).save(bookDTO);
        verify(kafkaTemplate).send(KafkaTopic.Constants.CREATION_TOPIC_VALUE, createdBook.getId().toString());
    }

    @Test
    void updateBook_shouldUpdateBookDetails() throws ObjectNotFoundException {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookDTO));
        when(bookRepository.save(any(Book.class))).thenReturn(bookDTO);

        BookDTO updatedBook = bookService.updateBook(1L, bookDTO);

        assertNotNull(updatedBook);
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(bookDTO);
    }

    @Test
    void updateBook_shouldThrowExceptionWhenBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> bookService.updateBook(1L, bookDTO));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void getAllBooks_shouldReturnAllBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(bookDTO));

        List<BookDTO> books = bookService.getAllBooks();

        assertNotNull(books);
        assertEquals(1, books.size());
        verify(bookRepository).findAll();
    }

    @Test
    void getBookByIsbn_shouldReturnBookIfFound() {
        when(bookRepository.findByIsbn("1234567")).thenReturn(Optional.of(bookDTO));

        Optional<Book> foundBook = bookService.getBookByIsbn("1234567");

        assertTrue(foundBook.isPresent());
        assertEquals("1234567", foundBook.get().getIsbn());
        verify(bookRepository).findByIsbn("1234567");
    }

    @Test
    void getBookByIsbn_shouldReturnEmptyIfNotFound() {
        when(bookRepository.findByIsbn("1234567")).thenReturn(Optional.empty());

        Optional<Book> foundBook = bookService.getBookByIsbn("1234567");

        assertTrue(foundBook.isEmpty());
        verify(bookRepository).findByIsbn("1234567");
    }

    @Test
    void getBookById_shouldReturnBookIfFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookDTO));

        Optional<Book> foundBook = bookService.getBookById(1L);

        assertTrue(foundBook.isPresent());
        assertEquals(1L, foundBook.get().getId());
        verify(bookRepository).findById(1L);
    }

    @Test
    void getBookById_shouldReturnEmptyIfNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Book> foundBook = bookService.getBookById(1L);

        assertTrue(foundBook.isEmpty());
        verify(bookRepository).findById(1L);
    }

    @Test
    void softDeleteBookInfo_shouldSetStatusToDeletedAndSendMessage() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookDTO));

        boolean result = bookService.softDeleteBookInfo(1L);

        assertTrue(result);
        assertEquals(CreationStatus.DELETED, bookDTO.getCreationStatus());
        verify(bookRepository).save(bookDTO);
        verify(kafkaTemplate).send(KafkaTopic.Constants.DELETION_TOPIC_VALUE, "1");
    }

    @Test
    void softDeleteBookInfo_shouldReturnFalseIfBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = bookService.softDeleteBookInfo(1L);

        assertFalse(result);
        verify(bookRepository, never()).save(any(Book.class));
        verify(kafkaTemplate, never()).send(anyString(), anyString());
    }
}
