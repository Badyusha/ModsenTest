package com.modsen.booktrackerservice.controllers;

import com.modsen.booktrackerservice.repositories.BookInfoRepository;
import com.modsen.booktrackerservice.services.BookInfoService;
import com.modsen.commonmodels.enums.entityAttributes.BookInfoStatus;
import com.modsen.commonmodels.models.dtos.BookInfoDto;
import com.modsen.commonmodels.models.entities.BookInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/book-tracker-service/v1/books")
@Tag(name = "Book Info Controller", description = "APIs for managing books info in tracker service")
@RestController
public class BookInfoController {

    private final BookInfoRepository bookInfoRepository;
    private final BookInfoService bookInfoService;

    @Operation(summary = "Creates new book info with provided BookInfoDto")
    @PostMapping
    public ResponseEntity<BookInfo> createBookInfo(@RequestBody BookInfoDto bookInfoDto) {
        BookInfo bookInfo = bookInfoService.fillInBookInfo(bookInfoDto);
        if(bookInfo == null) {
            return ResponseEntity.notFound().build();
        }

        BookInfo savedBookInfo = bookInfoRepository.save(bookInfo);
        return ResponseEntity.ok(savedBookInfo);
    }

    @Operation(summary = "Shows all available books info")
    @GetMapping("/available")
    public ResponseEntity<List<BookInfo>> getAvailableBooks() {
        List<BookInfo> availableBooks = bookInfoRepository.findByBookInfoStatus(BookInfoStatus.AVAILABLE);
        return ResponseEntity.ok(availableBooks);
    }

    @Operation(summary = "Updates book infos with provided id as a path param and BookInfoStatus")
    @PutMapping("/{id}")
    public ResponseEntity<BookInfo> updateBookStatus(@PathVariable Long id, @RequestParam BookInfoStatus bookInfoStatus) {
        return bookInfoRepository.findById(id)
                .map(bookInfo -> {
                    bookInfo.setBookInfoStatus(bookInfoStatus);
                    if (bookInfoStatus.equals(BookInfoStatus.BORROWED)) {
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

    @Operation(summary = "Soft delete book info with provided id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookInfo(@PathVariable Long id) {
        if (bookInfoService.softDeleteBookInfo(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
