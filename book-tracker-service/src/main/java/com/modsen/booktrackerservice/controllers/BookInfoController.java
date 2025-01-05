package com.modsen.booktrackerservice.controllers;

import com.modsen.booktrackerservice.repositories.BookInfoRepository;
import com.modsen.booktrackerservice.services.BookInfoService;
import com.modsen.commonmodels.enums.entityAttributes.Status;
import com.modsen.commonmodels.models.dtos.BookInfoDto;
import com.modsen.commonmodels.models.entities.BookInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/book-tracker-service/v1/books")
@RequiredArgsConstructor
public class BookInfoController {

    private final BookInfoRepository bookInfoRepository;
    private final BookInfoService bookInfoService;

    @PostMapping
    public ResponseEntity<BookInfo> createBookInfo(@RequestBody BookInfoDto bookInfoDto) {
        BookInfo bookInfo = bookInfoService.fillInBookInfo(bookInfoDto);
        if(bookInfo == null) {
            return ResponseEntity.notFound().build();
        }

        BookInfo savedBookInfo = bookInfoRepository.save(bookInfo);
        return ResponseEntity.ok(savedBookInfo);
    }

    @GetMapping("/available")
    public ResponseEntity<List<BookInfo>> getAvailableBooks() {
        List<BookInfo> availableBooks = bookInfoRepository.findByStatus(Status.AVAILABLE);
        return ResponseEntity.ok(availableBooks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookInfo> updateBookStatus(@PathVariable Long id, @RequestParam Status status) {
        return bookInfoRepository.findById(id)
                .map(bookInfo -> {
                    bookInfo.setStatus(status);
                    if (status.equals(Status.BORROWED)) {
                        bookInfo.setBorrowedAt(LocalDateTime.now());
                        bookInfo.setReturnDue(LocalDateTime.now().plusWeeks(2));
                    } else {
                        bookInfo.setBorrowedAt(null);
                        bookInfo.setReturnDue(null);
                    }
                    return ResponseEntity.ok(bookInfoRepository.save(bookInfo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookInfo(@PathVariable Long id) {
        if (bookInfoRepository.existsById(id)) {
            bookInfoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
