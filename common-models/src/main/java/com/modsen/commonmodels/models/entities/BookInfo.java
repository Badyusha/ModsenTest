package com.modsen.commonmodels.models.entities;

import com.modsen.commonmodels.enums.entityAttributes.Status;
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private LocalDateTime borrowedAt;

    @Column
    private LocalDateTime returnDue;

    public BookInfo(Long bookId, Status status, LocalDateTime borrowedAt, LocalDateTime returnDue) {
        this.bookId = bookId;
        this.status = status;
        this.borrowedAt = borrowedAt;
        this.returnDue = returnDue;
    }
}
