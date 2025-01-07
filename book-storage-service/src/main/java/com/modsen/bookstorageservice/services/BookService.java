package com.modsen.bookstorageservice.services;

import com.modsen.bookstorageservice.repositories.BookRepository;
import com.modsen.commonmodels.enums.attributes.CreationStatus;
import com.modsen.commonmodels.enums.kafka.KafkaTopic;
import com.modsen.commonmodels.exceptions.ObjectNotFoundException;
import com.modsen.commonmodels.models.entities.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    public Book createBook(Book book) {
        book.setCreationStatus(CreationStatus.EXISTS);
        Book savedBook = bookRepository.save(book);

        kafkaTemplate.send(KafkaTopic.Constants.CREATION_TOPIC_VALUE, savedBook.getId().toString());

        return savedBook;
    }

    public Book updateBook(Long id, Book book) throws ObjectNotFoundException {
        return bookRepository.findById(id)
                .map(existingBook -> {
                    existingBook.copy(book);
                    return bookRepository.save(existingBook);
                })
                .orElseThrow(() ->
                    new ObjectNotFoundException("Book cannot be updated. Book with provided id does not exist"));
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

        kafkaTemplate.send(KafkaTopic.Constants.DELETION_TOPIC_VALUE, bookId.toString());

        return true;
    }

}
