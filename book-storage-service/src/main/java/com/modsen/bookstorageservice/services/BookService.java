package com.modsen.bookstorageservice.services;

import com.modsen.bookstorageservice.enums.attributes.CreationStatus;
import com.modsen.bookstorageservice.mappers.BookDTOMapper;
import com.modsen.bookstorageservice.mappers.BookMapper;
import com.modsen.bookstorageservice.models.dtos.BookDTO;
import com.modsen.bookstorageservice.models.entities.Book;
import com.modsen.bookstorageservice.repositories.BookRepository;
import com.modsen.commonmodels.enums.kafka.KafkaTopic;
import com.modsen.commonmodels.exceptions.ObjectNotFoundException;
import com.modsen.commonmodels.exceptions.UnableToCastObjectToDTO;
import com.modsen.commonmodels.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;
    private final BookDTOMapper bookDTOMapper;
    private final BookRepository bookRepository;
    private final ValidationService validationService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public BookDTO createBook(BookDTO bookDTO) throws ValidationException {
        validationService.validate(bookDTO);

        bookDTO.setCreationStatus(CreationStatus.EXISTS);
        Book book = bookDTOMapper.toBook(bookDTO);
        Book savedBook = bookRepository.save(book);

        kafkaTemplate.send(KafkaTopic.Constants.CREATION_TOPIC_VALUE, savedBook.getIsbn());

        return bookMapper.toBookDTO(savedBook);
    }

    public BookDTO updateBook(Long id, BookDTO bookDTO) throws ObjectNotFoundException {
        return bookRepository.findById(id)
                .map(existingBook -> {
                    Book book = bookDTOMapper.toBook(bookDTO);
                    existingBook.copy(book);
                    Book savedBook = bookRepository.save(existingBook);
                    return bookMapper.toBookDTO(savedBook);
                })
                .orElseThrow(() ->
                    new ObjectNotFoundException("Book cannot be updated. Book with provided id does not exist"));
    }

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toBookDTO)
                .collect(Collectors.toList());
    }

    public BookDTO getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .map(bookMapper::toBookDTO)
                .orElseThrow(() -> new UnableToCastObjectToDTO("Book with provided ISBN does not exist"));
    }

    public BookDTO getBookById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toBookDTO)
                .orElseThrow(() -> new UnableToCastObjectToDTO("Book with provided ISBN does not exist"));
    }

    public void deleteBook(String isbn) {
        sendDeletionRequestToBookTracker(isbn);
    }

    public void softDeleteBook(String isbn) throws ObjectNotFoundException {
        Optional<Book> bookOptional = bookRepository.findByIsbn(isbn);
        if (bookOptional.isEmpty()) {
            throw new ObjectNotFoundException("Book cannot be found. Book with provided id does not exist");
        }

        Book book = bookOptional.get();
        book.setCreationStatus(CreationStatus.DELETED);
        bookRepository.save(book);
    }

    private void sendDeletionRequestToBookTracker(String isbn) {
         kafkaTemplate.send(KafkaTopic.Constants.DELETION_TOPIC_VALUE, isbn);
    }
}
