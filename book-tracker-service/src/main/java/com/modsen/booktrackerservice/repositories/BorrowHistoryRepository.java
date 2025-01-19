package com.modsen.booktrackerservice.repositories;

import com.modsen.booktrackerservice.models.entities.BorrowHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowHistoryRepository extends JpaRepository<BorrowHistory, Long> {
}
