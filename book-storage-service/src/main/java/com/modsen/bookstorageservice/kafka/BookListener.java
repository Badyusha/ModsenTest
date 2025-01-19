package com.modsen.bookstorageservice.kafka;

import com.modsen.bookstorageservice.services.BookService;
import com.modsen.commonmodels.Constants;
import com.modsen.commonmodels.enums.kafka.KafkaTopic;
import com.modsen.commonmodels.exceptions.ObjectNotFoundException;
import com.modsen.commonmodels.exceptions.PermissionException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookListener {

    private final BookService bookService;

    @KafkaListener(topics = KafkaTopic.Constants.DELETION_RESPONSE_TOPIC_VALUE, groupId = "book-storage-service-group")
    public ResponseEntity<Void> listenForDeletionStatus(String isbn) {
        if(isbn.equals(Constants.KAFKA_ERROR_RESPONSE)) {
            throw new PermissionException("Unable to delete book: user borrowed it");
        }

        try {
            bookService.softDeleteBook(isbn);
            return ResponseEntity.ok().build();
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

