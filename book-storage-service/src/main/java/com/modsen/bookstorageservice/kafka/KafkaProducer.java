package com.modsen.bookstorageservice.kafka;

import com.modsen.commonmodels.models.dtos.BookInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

//@Component
//@RequiredArgsConstructor
//public class KafkaProducer {
//    private final KafkaTemplate<String, BookInfoDto> kafkaTemplate;
//
//    public void sendBookInfo(String topic, BookInfoDto bookInfoDto) {
//        kafkaTemplate.send(topic, bookInfoDto);
//    }
//}

