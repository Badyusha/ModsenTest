package com.modsen.booktrackerservice.repositories;

import com.modsen.booktrackerservice.models.entities.BorrowHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.containers.MySQLContainer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BorrowHistoryRepositoryTest {

    @Autowired
    private BorrowHistoryRepository borrowHistoryRepository;

    private BorrowHistory borrowHistory1;
    private BorrowHistory borrowHistory2;

    public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("book_tracker")
            .withUsername("root")
            .withPassword("root");

    @BeforeEach
    public void setUp() {
        mysqlContainer.start();

        System.setProperty("spring.datasource.url", mysqlContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", mysqlContainer.getUsername());
        System.setProperty("spring.datasource.password", mysqlContainer.getPassword());

        borrowHistory1 = new BorrowHistory();
        borrowHistory1.setUserId(1L);
        borrowHistory1.setIsbn("1234567890");

        borrowHistory2 = new BorrowHistory();
        borrowHistory2.setUserId(2L);
        borrowHistory2.setIsbn("0987654321");

        borrowHistoryRepository.save(borrowHistory1);
        borrowHistoryRepository.save(borrowHistory2);
    }

    @Test
    public void testFindByUserIdAndIsbn() {
        Optional<BorrowHistory> foundHistory = borrowHistoryRepository.findByUserIdAndIsbn(1L, "1234567890");

        assertThat(foundHistory).isPresent();
        assertThat(foundHistory.get().getUserId()).isEqualTo(1L);
        assertThat(foundHistory.get().getIsbn()).isEqualTo("1234567890");

        Optional<BorrowHistory> notFoundHistory = borrowHistoryRepository.findByUserIdAndIsbn(1L, "1111111111");

        assertThat(notFoundHistory).isNotPresent();
    }

    @Test
    public void testIsUserBorrowBook() {
        boolean isBorrowed = borrowHistoryRepository.isUserBorrowBook(1L, "1234567890");
        assertThat(isBorrowed).isTrue();

        boolean isNotBorrowed = borrowHistoryRepository.isUserBorrowBook(1L, "1111111111");
        assertThat(isNotBorrowed).isFalse();
    }
}
