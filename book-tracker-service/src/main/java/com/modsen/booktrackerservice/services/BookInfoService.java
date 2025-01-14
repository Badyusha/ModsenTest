package com.modsen.booktrackerservice.services;

import com.modsen.booktrackerservice.enums.attributes.BookInfoStatus;
import com.modsen.booktrackerservice.mappers.BookInfoDTOMapper;
import com.modsen.booktrackerservice.mappers.BookInfoMapper;
import com.modsen.booktrackerservice.models.dtos.BookInfoDTO;
import com.modsen.booktrackerservice.models.entities.BookInfo;
import com.modsen.booktrackerservice.repositories.BookInfoRepository;
import com.modsen.commonmodels.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookInfoService {

    private final BookInfoRepository bookInfoRepository;
    private final BookInfoMapper bookInfoMapper;
    private final BookInfoDTOMapper bookInfoDTOMapper;

    public BookInfoDTO createBook(BookInfoDTO bookInfoDTO) {
        bookInfoDTO.setBookInfoStatus(BookInfoStatus.AVAILABLE);
        BookInfo bookInfo = bookInfoDTOMapper.toBookInfo(bookInfoDTO);
        BookInfo savedBook = bookInfoRepository.save(bookInfo);

        return bookInfoMapper.toBookInfoDTO(savedBook);
    }

    public List<BookInfoDTO> getAvailableBooks() {
        return bookInfoRepository.findByBookInfoStatus(BookInfoStatus.AVAILABLE).stream()
                .map(bookInfoMapper::toBookInfoDTO)
                .collect(Collectors.toList());
    }

    public BookInfoDTO updateBookStatus(Long id, BookInfoStatus bookInfoStatus) throws ObjectNotFoundException {
        return bookInfoRepository.findById(id)
                .map(bookInfoDTO -> {
                    bookInfoDTO.setBookInfoStatus(bookInfoStatus);
                    if (bookInfoStatus.equals(BookInfoStatus.BORROWED)) {
                        bookInfoDTO.setBorrowedAt(LocalDateTime.now());
                        bookInfoDTO.setReturnDue(LocalDateTime.now().plusWeeks(2));
                    } else {
                        bookInfoDTO.setBorrowedAt(null);
                        bookInfoDTO.setReturnDue(null);
                    }
                    BookInfo bookInfo = bookInfoRepository.save(bookInfoDTO);
                    return bookInfoMapper.toBookInfoDTO(bookInfo);
                })
                .orElseThrow(() ->
                new ObjectNotFoundException("Book info cannot be updated. Book info with provided id does not exist"));
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
        bookInfo.setBookInfoStatus(BookInfoStatus.DELETED);
        bookInfoRepository.save(bookInfo);
    }
}
