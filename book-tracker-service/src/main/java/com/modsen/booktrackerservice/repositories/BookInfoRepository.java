package com.modsen.booktrackerservice.repositories;

import com.modsen.booktrackerservice.enums.attributes.BookInfoStatus;
import com.modsen.booktrackerservice.models.entities.BookInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookInfoRepository extends JpaRepository<BookInfo, Long> {
    List<BookInfo> findByBookInfoStatus(BookInfoStatus bookInfoStatus);
    Optional<BookInfo> findByIsbn(String isbn);
//    Optional<BookInfo> findByBookId(Long bookId);
//    boolean existsByBookId(Long bookId);
//    void deleteByBookId(Long bookId);
}
