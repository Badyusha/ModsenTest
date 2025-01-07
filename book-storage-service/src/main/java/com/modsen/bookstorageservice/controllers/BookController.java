package com.modsen.bookstorageservice.controllers;

import com.modsen.bookstorageservice.repositories.BookRepository;
import com.modsen.bookstorageservice.services.BookService;
import com.modsen.commonmodels.enums.attributes.CreationStatus;
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

    private final BookService bookService;

    @Operation(summary = "Create new book and send request to book-tracker-service via Kafka")
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        return bookService.getCreateBookResponseEntity(book);
    }

    @Operation(summary = "Show all books")
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return bookService.getAllBooksResponseEntity();
    }

    @Operation(summary = "Show book with provided ISBN")
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        return bookService.getBookByIsbnResponseEntity(isbn);
    }

    @Operation(summary = "Show book with provided id")
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookByIdResponseEntity(id);
    }

    @Operation(summary = "Update book with provided id as a path param and request body")
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.getUpdateBookResponseEntity(id, book);
    }

    @Operation(summary = "Soft delete book with provided id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        return bookService.getSoftDeleteBookResponseEntity(id);
    }
}
