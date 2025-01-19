package com.modsen.booktrackerservice.models.entities;

import com.modsen.booktrackerservice.enums.attributes.BorrowHistoryStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "borrow_history")
public class BorrowHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 10)
    private String isbn;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BorrowHistoryStatus borrowHistoryStatus;

    public BorrowHistory(Long userId, String isbn) {
        this.userId = userId;
        this.isbn = isbn;
        this.borrowHistoryStatus = BorrowHistoryStatus.BORROWED;
    }
}
