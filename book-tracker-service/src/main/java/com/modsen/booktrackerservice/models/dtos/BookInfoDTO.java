package com.modsen.booktrackerservice.models.dtos;

import com.modsen.booktrackerservice.enums.attributes.BookInfoStatus;
import com.modsen.commonmodels.Constants;
import com.modsen.commonmodels.models.dtos.RequestDTO;
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
    private Long userId;
    private BookInfoStatus bookInfoStatus;
    private LocalDateTime borrowedAt;
    private LocalDateTime returnDue;

    public void fillIn() {
        this.bookInfoStatus = BookInfoStatus.AVAILABLE;
        this.userId = null;
        this.borrowedAt = null;
        this.returnDue = null;
    }
}
