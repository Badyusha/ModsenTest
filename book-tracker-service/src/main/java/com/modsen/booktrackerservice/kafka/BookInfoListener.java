package com.modsen.booktrackerservice.kafka;

import com.modsen.booktrackerservice.repositories.BookInfoRepository;
import com.modsen.booktrackerservice.services.BookInfoService;
import com.modsen.commonmodels.enums.kafka.KafkaTopic;
import com.modsen.commonmodels.models.dtos.BookInfoDto;
import com.modsen.commonmodels.models.entities.BookInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookInfoListener {
    private final BookInfoRepository bookInfoRepository;
    private final BookInfoService bookInfoService;

    @KafkaListener(topics = KafkaTopic.Constants.CREATION_TOPIC_VALUE, groupId = "book-tracker-service")
    public void listenForCreation(String bookId) {
        Long bookIdLong = Long.parseLong(bookId);

        BookInfoDto bookInfoDto = new BookInfoDto(bookIdLong);
        BookInfo bookInfo = bookInfoService.fillInBookInfo(bookInfoDto);

        if (bookInfo != null) {
            bookInfoRepository.save(bookInfo);
        }
    }

    @KafkaListener(topics = KafkaTopic.Constants.DELETION_TOPIC_VALUE, groupId = "book-tracker-service")
    public void listenForDeletion(String bookId) {
        Long bookIdLong = Long.parseLong(bookId);

        if(bookInfoRepository.existsByBookId(bookIdLong)) {
            bookInfoRepository.deleteByBookId(bookIdLong);
        }
    }
}

