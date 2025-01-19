package com.modsen.booktrackerservice.services;

import com.modsen.booktrackerservice.enums.attributes.BookInfoStatus;
import com.modsen.booktrackerservice.enums.attributes.BorrowHistoryStatus;
import com.modsen.booktrackerservice.mappers.BookInfoDTOMapper;
import com.modsen.booktrackerservice.mappers.BookInfoMapper;
import com.modsen.booktrackerservice.models.dtos.BookInfoDTO;
import com.modsen.booktrackerservice.models.entities.BookInfo;
import com.modsen.booktrackerservice.models.entities.BorrowHistory;
import com.modsen.booktrackerservice.repositories.BookInfoRepository;
import com.modsen.booktrackerservice.repositories.BorrowHistoryRepository;
import com.modsen.commonmodels.Constants;
import com.modsen.commonmodels.enums.kafka.KafkaTopic;
import com.modsen.commonmodels.exceptions.ObjectNotFoundException;
import com.modsen.commonmodels.exceptions.PermissionException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookInfoService {

    private final TokenService tokenService;
    private final BookInfoMapper bookInfoMapper;
    private final BookInfoDTOMapper bookInfoDTOMapper;
    private final BookInfoRepository bookInfoRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final BorrowHistoryRepository borrowHistoryRepository;

    public BookInfoDTO createBook(BookInfoDTO bookInfoDTO) {
        bookInfoDTO.fillIn();
        BookInfo bookInfo = bookInfoDTOMapper.toBookInfo(bookInfoDTO);
        BookInfo savedBook = bookInfoRepository.save(bookInfo);

        return bookInfoMapper.toBookInfoDTO(savedBook);
    }

    public List<BookInfoDTO> getAvailableBooks() {
        return bookInfoRepository.findByBookInfoStatus(BookInfoStatus.AVAILABLE).stream()
                .map(bookInfoMapper::toBookInfoDTO)
                .collect(Collectors.toList());
    }

    public BookInfoDTO borrowBook(String token, String isbn) throws ObjectNotFoundException {
        Long userId = tokenService.extractUserId(token);
        if(borrowHistoryRepository.isUserBorrowBook(userId, isbn)) {
            throw new PermissionException("User can't borrow same book more than once");
        }

        borrowHistoryRepository.save(new BorrowHistory(userId, isbn));

        return bookInfoRepository.findByIsbn(isbn)
                .map(bookInfo -> {
                    if(bookInfo.getBookInfoStatus().equals(BookInfoStatus.DELETED)) {
                        throw new PermissionException("User can't borrow deleted book");
                    }
                    bookInfo.setBookInfoStatus(BookInfoStatus.BORROWED);
                    bookInfo.fillIn(userId);

                    BookInfo savedBookInfo = bookInfoRepository.save(bookInfo);
                    return bookInfoMapper.toBookInfoDTO(savedBookInfo);
                })
                .orElseThrow(() ->
                new ObjectNotFoundException("Book cannot be borrowed. Book with provided isbn does not exist"));
    }

    public BookInfoDTO returnBook(String token, String isbn) throws ObjectNotFoundException {
        Long userId = tokenService.extractUserId(token);
        BorrowHistory borrowHistory = borrowHistoryRepository.findByUserIdAndIsbn(userId, isbn)
                .filter(history -> history.getBorrowHistoryStatus().equals(BorrowHistoryStatus.BORROWED))
                .orElseThrow(() -> new PermissionException("""
                                                            Book with provided isbn does not exist in borrow history
                                                            or user trying to return book more than once"""));

        borrowHistory.setBorrowHistoryStatus(BorrowHistoryStatus.RETURNED);
        borrowHistoryRepository.save(borrowHistory);

        return bookInfoRepository.findByIsbn(isbn)
                .map(bookInfo -> {
                    bookInfo.setBookInfoStatus(BookInfoStatus.AVAILABLE);
                    bookInfo.fillIn(null);

                    BookInfo savedBookInfo = bookInfoRepository.save(bookInfo);
                    return bookInfoMapper.toBookInfoDTO(savedBookInfo);
                })
                .orElseThrow(() ->
                new ObjectNotFoundException("Book info cannot be returned. Book with provided isbn does not exist"));
    }

    public void deleteBookInfo(String isbn) throws ObjectNotFoundException {
        softDeleteBookInfo(isbn);
    }

    private void softDeleteBookInfo(String isbn) throws ObjectNotFoundException {
        Optional<BookInfo> bookInfoOptional = bookInfoRepository.findByIsbn(isbn);
        if (bookInfoOptional.isEmpty()) {
            throw new ObjectNotFoundException("Book info cannot be found. Book info with provided isbn does not exist");
        }

        BookInfo bookInfo = bookInfoOptional.get();
        if(bookInfo.getBookInfoStatus().equals(BookInfoStatus.BORROWED)) {
            sendDeletionResponseStatus(Constants.KAFKA_ERROR_RESPONSE);
            return;
        }

        sendDeletionResponseStatus(isbn);
        bookInfo.setBookInfoStatus(BookInfoStatus.DELETED);
        bookInfoRepository.save(bookInfo);
    }

    private void sendDeletionResponseStatus(String isbn) {
        kafkaTemplate.send(KafkaTopic.Constants.DELETION_RESPONSE_TOPIC_VALUE, isbn);
    }
}
