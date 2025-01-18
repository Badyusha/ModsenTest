package com.modsen.booktrackerservice.models.entities;

import com.modsen.booktrackerservice.enums.attributes.BookInfoStatus;
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

    public BookInfo(String isbn,
                    BookInfoStatus bookInfoStatus,
                    LocalDateTime borrowedAt,
                    LocalDateTime returnDue) {
        this.isbn = isbn;
        this.bookInfoStatus = bookInfoStatus;
        this.borrowedAt = borrowedAt;
        this.returnDue = returnDue;
    }
}
