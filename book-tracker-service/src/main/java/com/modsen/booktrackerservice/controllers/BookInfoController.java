package com.modsen.booktrackerservice.controllers;

import com.modsen.booktrackerservice.repositories.BookInfoRepository;
import com.modsen.booktrackerservice.services.BookInfoService;
import com.modsen.commonmodels.enums.attributes.BookInfoStatus;
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

    private final BookInfoService bookInfoService;

    @Operation(summary = "Create new book info with provided BookInfoDto")
    @PostMapping
    public ResponseEntity<BookInfo> createBookInfo(@RequestBody BookInfoDto bookInfoDto) {
        return bookInfoService.getCreateBookInfoResponseEntity(bookInfoDto);
    }

    @Operation(summary = "Show all available books info")
    @GetMapping("/available")
    public ResponseEntity<List<BookInfo>> getAvailableBooks() {
        return bookInfoService.getAvailableBooksResponseEntity();
    }

    @Operation(summary = "Update book infos with provided id as a path param and BookInfoStatus")
    @PutMapping("/{id}")
    public ResponseEntity<BookInfo> updateBookStatus(@PathVariable Long id, @RequestParam BookInfoStatus bookInfoStatus) {
        return bookInfoService.getUpdateBookStatusResponseEntity(id, bookInfoStatus);
    }

    @Operation(summary = "Soft delete book info with provided id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookInfo(@PathVariable Long id) {
        return bookInfoService.getDeleteBookInfoResponseEntity(id);
    }
}
