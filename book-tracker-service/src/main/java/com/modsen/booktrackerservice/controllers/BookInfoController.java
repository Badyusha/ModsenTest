package com.modsen.booktrackerservice.controllers;

import com.modsen.booktrackerservice.enums.attributes.BookInfoStatus;
import com.modsen.booktrackerservice.models.dtos.BookInfoDTO;
import com.modsen.booktrackerservice.services.BookInfoService;
import com.modsen.commonmodels.exceptions.ObjectNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/book-tracker-service/v1/books")
@Tag(name = "Book Info Controller", description = "APIs for managing books info in tracker service")
@RestController
public class BookInfoController {

    private final BookInfoService bookInfoService;

    @Operation(summary = "Create new book info with provided BookInfoDto")
    @PostMapping
    public BookInfoDTO createBookInfo(@RequestBody BookInfoDTO bookInfoDTO) {
        return bookInfoService.createBook(bookInfoDTO);
    }

    @Operation(summary = "Show all available books info")
    @GetMapping("/available")
    public List<BookInfoDTO> getAvailableBooks() {
        return bookInfoService.getAvailableBooks();
    }

    @Operation(summary = "Update book infos with provided id as a path param and BookInfoStatus")
    @PutMapping("/{id}")
    public BookInfoDTO updateBookStatus(@RequestHeader("Authorization") String token,
                                        @PathVariable Long id,
                                        @RequestParam BookInfoStatus bookInfoStatus) {
        try {
            return bookInfoService.updateBookStatus(id, bookInfoStatus, token);
        } catch (ObjectNotFoundException e) {
            throw new RuntimeException("Unable to update book info. Book with provided id does not exist");
        }
    }

    @Operation(summary = "Soft delete book info with provided ISBN")
    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> deleteBookInfo(@PathVariable String isbn) {
        try {
            bookInfoService.deleteBookInfo(isbn);
            return ResponseEntity.ok().build();
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
