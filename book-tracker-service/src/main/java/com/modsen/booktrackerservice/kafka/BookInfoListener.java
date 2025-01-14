package com.modsen.booktrackerservice.kafka;

import com.modsen.booktrackerservice.models.dtos.BookInfoDTO;
import com.modsen.booktrackerservice.services.BookInfoService;
import com.modsen.commonmodels.enums.kafka.KafkaTopic;
import com.modsen.commonmodels.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookInfoListener {
    private final BookInfoService bookInfoService;

    @KafkaListener(topics = KafkaTopic.Constants.CREATION_TOPIC_VALUE, groupId = "book-tracker-service")
    public BookInfoDTO listenForCreation(String isbn) {
        BookInfoDTO bookInfoDTO = new BookInfoDTO();
        bookInfoDTO.setIsbn(isbn);
        return bookInfoService.createBook(bookInfoDTO);
    }

    @KafkaListener(topics = KafkaTopic.Constants.DELETION_TOPIC_VALUE, groupId = "book-tracker-service")
    public ResponseEntity<Void> listenForDeletion(String isbn) {
        try {
            bookInfoService.deleteBookInfo(isbn);
            return ResponseEntity.ok().build();
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

