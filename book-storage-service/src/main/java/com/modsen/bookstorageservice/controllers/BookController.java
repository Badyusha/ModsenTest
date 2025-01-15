package com.modsen.bookstorageservice.controllers;

import com.modsen.bookstorageservice.models.dtos.BookDTO;
import com.modsen.bookstorageservice.services.BookService;
import com.modsen.commonmodels.exceptions.ObjectNotFoundException;
import com.modsen.commonmodels.exceptions.ValidationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/book-storage-service/v1/books")
@Tag(name = "Book Controller", description = "APIs for managing books in storage service")
@RestController
public class BookController {

    private final BookService bookService;

    @Operation(summary = "Create new book and send request to book-tracker-service via Kafka")
    @PreAuthorize("hasRole('PUBLISHER')")
    @PostMapping
    public BookDTO createBook(@RequestBody BookDTO bookDTO) {
        try {
            return bookService.createBook(bookDTO);
        } catch (ValidationException e) {
            throw new RuntimeException("Unable to create book! Some fields are not valid!");
        }
    }

    @Operation(summary = "Show all books")
    @GetMapping
    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks();
    }

    @Operation(summary = "Show book with provided ISBN")
    @GetMapping("/isbn/{isbn}")
    public BookDTO getBookByIsbn(@PathVariable String isbn) {
        return bookService.getBookByIsbn(isbn);
    }

    @Operation(summary = "Show book with provided id")
    @GetMapping("/{id}")
    public BookDTO getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @Operation(summary = "Update book with provided id as a path param and request body")
    @PreAuthorize("hasRole('PUBLISHER')")
    @PutMapping("/{id}")
    public BookDTO updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
        try {
            return bookService.updateBook(id, bookDTO);
        } catch (ObjectNotFoundException e) {
            throw new RuntimeException("Unable to update book. Book with provided id does not exist");
        }
    }

    @Operation(summary = "Soft delete book with provided id")
    @PreAuthorize("hasRole('PUBLISHER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBookInfo(id);
            return ResponseEntity.ok().build();
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
