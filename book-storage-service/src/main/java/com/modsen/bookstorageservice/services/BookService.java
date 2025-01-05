package com.modsen.bookstorageservice.services;

//import com.modsen.bookstorageservice.kafka.KafkaProducer;
//import com.modsen.commonmodels.models.dtos.BookInfoDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;

//@RequiredArgsConstructor
//@Component
//public class BookService {
//    private final BookTrackerClient bookTrackerClient;
//
//    public void notifyBookTrackerService(long bookId) {
//        BookInfoDto bookInfoDto = new BookInfoDto(bookId);
//        bookTrackerClient.createBookInBookTrackerService(bookInfoDto);
//    }
//}

//@RequiredArgsConstructor
//@Component
//public class BookService {
//    private final KafkaProducer kafkaProducer;
//
//    public void notifyBookTrackerService(long bookId) {
//        BookInfoDto bookInfoDto = new BookInfoDto(bookId);
//        kafkaProducer.sendBookInfo("book-info-topic", bookInfoDto);
//    }
//}
