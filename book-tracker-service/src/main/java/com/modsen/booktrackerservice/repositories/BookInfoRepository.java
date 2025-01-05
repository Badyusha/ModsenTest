package com.modsen.booktrackerservice.repositories;

import com.modsen.commonmodels.enums.entityAttributes.Status;
import com.modsen.commonmodels.models.entities.BookInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookInfoRepository extends JpaRepository<BookInfo, Long> {
    List<BookInfo> findByStatus(Status status);
    boolean existsByBookId(Long bookId);
    void deleteByBookId(Long bookId);
}
