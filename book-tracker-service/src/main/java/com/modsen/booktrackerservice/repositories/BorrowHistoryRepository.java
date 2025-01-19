package com.modsen.booktrackerservice.repositories;

import com.modsen.booktrackerservice.models.entities.BorrowHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowHistoryRepository extends JpaRepository<BorrowHistory, Long> {
    Optional<BorrowHistory> findByUserIdAndIsbn(Long userId, String isbn);

    default boolean isUserBorrowBook(Long userId, String isbn) {
        return findByUserIdAndIsbn(userId, isbn).isPresent();
    }
}
