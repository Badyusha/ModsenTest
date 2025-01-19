package com.modsen.booktrackerservice.models.entities;

import com.modsen.booktrackerservice.enums.attributes.BookInfoStatus;
import com.modsen.commonmodels.Constants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "book_info")
public class BookInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String isbn;

    @Column
    private Long userId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookInfoStatus bookInfoStatus;

    @Column
    private LocalDateTime borrowedAt;

    @Column
    private LocalDateTime returnDue;

    public void fillIn(Long userId) {
        this.userId = userId;
        if(userId == null) {
            this.borrowedAt = this.returnDue = null;
        } else {
            LocalDateTime now = LocalDateTime.now();
            this.borrowedAt = now;
            this.returnDue = now.plusWeeks(Constants.WEEKS_AVAILABLE_FOR_BORROW);
        }
    }
}
