package com.modsen.bookstorageservice.controllers;

import com.modsen.bookstorageservice.repositories.BookRepository;
import com.modsen.commonmodels.enums.kafka.KafkaTopic;
import com.modsen.commonmodels.models.entities.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book-storage-service/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book savedBook = bookRepository.save(book);

        // Send bookId to Kafka
        kafkaTemplate.send(KafkaTopic.Constants.CREATION_TOPIC_VALUE, savedBook.getId().toString());

        return ResponseEntity.ok(savedBook);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookRepository.findAll());
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        return bookRepository.findByIsbn(isbn)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);

            kafkaTemplate.send(KafkaTopic.Constants.DELETION_TOPIC_VALUE, id.toString());

            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
