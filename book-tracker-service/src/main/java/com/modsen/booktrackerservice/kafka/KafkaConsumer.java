package com.modsen.booktrackerservice.kafka;

import com.modsen.booktrackerservice.repositories.BookInfoRepository;
import com.modsen.booktrackerservice.services.BookInfoService;
import com.modsen.commonmodels.models.dtos.BookInfoDto;
import com.modsen.commonmodels.models.entities.BookInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

//@Component
//@RequiredArgsConstructor
//public class KafkaConsumer {
//    private final BookInfoService bookInfoService;
//    private final BookInfoRepository bookInfoRepository;
//
//    @KafkaListener(topics = "book-info-topic", groupId = "book-tracker-group")
//    public void listen(BookInfoDto bookInfoDto) {
//        BookInfo bookInfo = bookInfoService.fillInBookInfo(bookInfoDto);
//        bookInfoRepository.save(bookInfo);
//    }
//}

