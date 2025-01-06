package com.modsen.bookstorageservice.services;

import com.modsen.bookstorageservice.repositories.BookRepository;
import com.modsen.commonmodels.enums.entityAttributes.CreationStatus;
import com.modsen.commonmodels.models.entities.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public boolean softDeleteBookInfo(Long bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (!bookOptional.isPresent()) {
            return false;
        }

        Book book = bookOptional.get();
        book.setCreationStatus(CreationStatus.DELETED);
        bookRepository.save(book);

        return true;
    }

}
