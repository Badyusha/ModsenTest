package com.modsen.commonmodels.models.entities;

import com.modsen.commonmodels.enums.attributes.BookInfoStatus;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id",
                referencedColumnName = "id",
                nullable = false,
                insertable = false,
                updatable = false,
                unique = true)
    private Book book;

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookInfoStatus bookInfoStatus;

    @Column
    private LocalDateTime borrowedAt;

    @Column
    private LocalDateTime returnDue;

    public BookInfo(Long bookId, BookInfoStatus bookInfoStatus, LocalDateTime borrowedAt, LocalDateTime returnDue) {
        this.bookId = bookId;
        this.bookInfoStatus = bookInfoStatus;
        this.borrowedAt = borrowedAt;
        this.returnDue = returnDue;
    }
}
