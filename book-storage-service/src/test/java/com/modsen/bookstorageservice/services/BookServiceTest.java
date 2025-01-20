package com.modsen.bookstorageservice.services;

import com.modsen.bookstorageservice.enums.attributes.CreationStatus;
import com.modsen.bookstorageservice.mappers.BookDTOMapper;
import com.modsen.bookstorageservice.mappers.BookMapper;
import com.modsen.bookstorageservice.models.dtos.BookDTO;
import com.modsen.bookstorageservice.models.entities.Book;
import com.modsen.bookstorageservice.repositories.BookRepository;
import com.modsen.commonmodels.exceptions.ObjectNotFoundException;
import com.modsen.commonmodels.exceptions.UnableToCastObjectToDTO;
import com.modsen.commonmodels.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookDTOMapper bookDTOMapper;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ValidationService validationService;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private BookService bookService;

    private BookDTO bookDTO;
    private Book book;

    @BeforeEach
    void setUp() {
        bookDTO = new BookDTO();
        bookDTO.setIsbn("1234567890");
        bookDTO.setCreationStatus(CreationStatus.EXISTS);

        book = new Book();
        book.setIsbn("1234567890");
        book.setCreationStatus(CreationStatus.EXISTS);
    }

    @Test
    void createBook_success() throws ValidationException {
        doNothing().when(validationService).validate(bookDTO);
        when(bookDTOMapper.toBook(bookDTO)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toBookDTO(book)).thenReturn(bookDTO);

        BookDTO result = bookService.createBook(bookDTO);

        verify(validationService).validate(bookDTO);
        verify(kafkaTemplate).send(eq("book-creation-topic"), eq("1234567890"));
        assertEquals(bookDTO, result);
    }

    @Test
    void updateBook_success() throws ObjectNotFoundException {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookDTOMapper.toBook(bookDTO)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toBookDTO(book)).thenReturn(bookDTO);

        BookDTO result = bookService.updateBook(1L, bookDTO);

        assertEquals(bookDTO, result);
    }

    @Test
    void updateBook_notFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> bookService.updateBook(1L, bookDTO));
    }

    @Test
    void getAllBooks_success() {
        when(bookRepository.findAll()).thenReturn(List.of(book));
        when(bookMapper.toBookDTO(book)).thenReturn(bookDTO);

        List<BookDTO> result = bookService.getAllBooks();

        assertEquals(1, result.size());
        assertEquals(bookDTO, result.get(0));
    }

    @Test
    void getBookByIsbn_success() {
        when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.of(book));
        when(bookMapper.toBookDTO(book)).thenReturn(bookDTO);

        BookDTO result = bookService.getBookByIsbn("1234567890");

        assertEquals(bookDTO, result);
    }

    @Test
    void getBookByIsbn_notFound() {
        when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.empty());

        assertThrows(UnableToCastObjectToDTO.class, () -> bookService.getBookByIsbn("1234567890"));
    }

    @Test
    void deleteBook_success() {
        bookService.deleteBook("1234567890");

        verify(kafkaTemplate).send(eq("book-deletion-topic"), eq("1234567890"));
    }

    @Test
    void softDeleteBook_success() throws ObjectNotFoundException {
        when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.of(book));

        bookService.softDeleteBook("1234567890");

        assertEquals(CreationStatus.DELETED, book.getCreationStatus());
        verify(bookRepository).save(book);
    }

    @Test
    void softDeleteBook_notFound() {
        when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> bookService.softDeleteBook("1234567890"));
    }
}
