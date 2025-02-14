package com.modsen.booktrackerservice.kafka;

import com.modsen.booktrackerservice.repositories.BookInfoRepository;
import com.modsen.booktrackerservice.services.BookInfoService;
import com.modsen.commonmodels.enums.kafka.KafkaTopic;
import com.modsen.commonmodels.exceptions.ObjectNotFoundException;
import com.modsen.commonmodels.models.dtos.BookInfoDto;
import com.modsen.commonmodels.models.entities.BookInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookInfoListener {
    private final BookInfoRepository bookInfoRepository;
    private final BookInfoService bookInfoService;

    @KafkaListener(topics = KafkaTopic.Constants.CREATION_TOPIC_VALUE, groupId = "book-tracker-service")
    public ResponseEntity<BookInfo> listenForCreation(String bookId) {
        Long bookIdLong = Long.parseLong(bookId);
        BookInfoDto bookInfoDto = new BookInfoDto(bookIdLong);
        try {
            BookInfo bookInfo = bookInfoService.createBook(bookInfoDto);
            return ResponseEntity.ok(bookInfo);
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @KafkaListener(topics = KafkaTopic.Constants.DELETION_TOPIC_VALUE, groupId = "book-tracker-service")
    public ResponseEntity<Void> listenForDeletion(String bookId) {
        Long bookIdLong = Long.parseLong(bookId);
        try {
            bookInfoService.deleteBookInfo(bookIdLong);
            return ResponseEntity.ok().build();
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

