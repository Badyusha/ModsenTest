package com.modsen.bookstorageservice.controllers;

import com.modsen.bookstorageservice.repositories.BookRepository;
import com.modsen.bookstorageservice.services.BookService;
import com.modsen.commonmodels.enums.entityAttributes.CreationStatus;
import com.modsen.commonmodels.enums.kafka.KafkaTopic;
import com.modsen.commonmodels.models.entities.Book;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/book-storage-service/v1/books")
@Tag(name = "Book Controller", description = "APIs for managing books in storage service")
@RestController
public class BookController {

    private final BookRepository bookRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final BookService bookService;

    @Operation(summary = "Creates new book and sends request to book-tracker-service via Kafka")
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        book.setCreationStatus(CreationStatus.EXISTS);
        Book savedBook = bookRepository.save(book);

        kafkaTemplate.send(KafkaTopic.Constants.CREATION_TOPIC_VALUE, savedBook.getId().toString());

        return ResponseEntity.ok(savedBook);
    }

    @Operation(summary = "Shows all books")
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookRepository.findAll());
    }

    @Operation(summary = "Shows book with provided ISBN")
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        return bookRepository.findByIsbn(isbn)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Shows book with provided id")
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Updates book with provided id as a path param and request body")
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookRepository.findById(id)
                .map(existingBook -> {
                    existingBook.setTitle(book.getTitle());
                    existingBook.setGenre(book.getGenre());
                    existingBook.setDescription(book.getDescription());
                    existingBook.setAuthor(book.getAuthor());
                    return ResponseEntity.ok(bookRepository.save(existingBook));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Soft delete book with provided id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if(bookService.softDeleteBookInfo(id)) {
            kafkaTemplate.send(KafkaTopic.Constants.DELETION_TOPIC_VALUE, id.toString());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
