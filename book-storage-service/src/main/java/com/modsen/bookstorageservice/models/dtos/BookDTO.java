package com.modsen.bookstorageservice.models.dtos;

import com.modsen.bookstorageservice.enums.attributes.CreationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;
    private String isbn;
    private String title;
    private String genre;
    private String description;
    private String author;
    private CreationStatus creationStatus;
}
