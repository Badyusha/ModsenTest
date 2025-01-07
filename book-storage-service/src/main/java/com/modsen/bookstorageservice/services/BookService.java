package com.modsen.bookstorageservice.services;

import com.modsen.bookstorageservice.repositories.BookRepository;
import com.modsen.commonmodels.enums.attributes.CreationStatus;
import com.modsen.commonmodels.enums.kafka.KafkaTopic;
import com.modsen.commonmodels.models.entities.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public ResponseEntity<Book> getCreateBookResponseEntity(Book book) {
        return ResponseEntity.ok(createBook(book));
    }

    public ResponseEntity<List<Book>> getAllBooksResponseEntity() {
        return ResponseEntity.ok(getAllBooks());
    }

    public ResponseEntity<Book> getBookByIsbnResponseEntity(String isbn) {
        return getBookByIsbn(isbn)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Book> getBookByIdResponseEntity(Long id) {
        return getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Book> getUpdateBookResponseEntity(Long id, Book book) {
        return bookRepository.findById(id)
                .map(existingBook -> {
                    existingBook.copy(book);
                    return ResponseEntity.ok(bookRepository.save(existingBook));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Void> getSoftDeleteBookResponseEntity(Long id) {
        if(softDeleteBookInfo(id)) {
            kafkaTemplate.send(KafkaTopic.Constants.DELETION_TOPIC_VALUE, id.toString());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    public Book createBook(Book book) {
        book.setCreationStatus(CreationStatus.EXISTS);
        Book savedBook = bookRepository.save(book);

        kafkaTemplate.send(KafkaTopic.Constants.CREATION_TOPIC_VALUE, savedBook.getId().toString());

        return savedBook;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public boolean softDeleteBookInfo(Long bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            return false;
        }

        Book book = bookOptional.get();
        book.setCreationStatus(CreationStatus.DELETED);
        bookRepository.save(book);

        return true;
    }

}
