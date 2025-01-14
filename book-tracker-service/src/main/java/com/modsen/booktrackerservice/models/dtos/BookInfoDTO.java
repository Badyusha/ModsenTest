package com.modsen.booktrackerservice.models.dtos;

import com.modsen.booktrackerservice.enums.attributes.BookInfoStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookInfoDTO {
    private Long id;
    private String isbn;
    private BookInfoStatus bookInfoStatus;
    private LocalDateTime borrowedAt;
    private LocalDateTime returnDue;
}
