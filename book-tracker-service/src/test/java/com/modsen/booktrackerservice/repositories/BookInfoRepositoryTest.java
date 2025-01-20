package com.modsen.booktrackerservice.repositories;

import com.modsen.booktrackerservice.enums.attributes.BookInfoStatus;
import com.modsen.booktrackerservice.models.entities.BookInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties", properties = "spring.liquibase.enabled=false")
public class BookInfoRepositoryTest {

    @Autowired
    private BookInfoRepository bookInfoRepository;

    private BookInfo bookInfo1;
    private BookInfo bookInfo2;

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

        bookInfo1 = new BookInfo();
        bookInfo1.setIsbn("1234567890");
        bookInfo1.setBookInfoStatus(BookInfoStatus.AVAILABLE);

        bookInfo2 = new BookInfo();
        bookInfo2.setIsbn("0987654321");
        bookInfo2.setBookInfoStatus(BookInfoStatus.BORROWED);

        bookInfoRepository.save(bookInfo1);
        bookInfoRepository.save(bookInfo2);
    }

    @Test
    public void testFindByBookInfoStatus() {
        List<BookInfo> availableBooks = bookInfoRepository.findByBookInfoStatus(BookInfoStatus.AVAILABLE);

        assertThat(availableBooks).hasSize(1);
        assertThat(availableBooks.get(0).getIsbn()).isEqualTo("1234567890");
        assertThat(availableBooks.get(0).getBookInfoStatus()).isEqualTo(BookInfoStatus.AVAILABLE);

        List<BookInfo> borrowedBooks = bookInfoRepository.findByBookInfoStatus(BookInfoStatus.BORROWED);

        assertThat(borrowedBooks).hasSize(1);
        assertThat(borrowedBooks.get(0).getIsbn()).isEqualTo("0987654321");
        assertThat(borrowedBooks.get(0).getBookInfoStatus()).isEqualTo(BookInfoStatus.BORROWED);
    }

    @Test
    public void testFindByIsbn() {
        Optional<BookInfo> foundBookInfo = bookInfoRepository.findByIsbn("1234567890");

        assertThat(foundBookInfo).isPresent();
        assertThat(foundBookInfo.get().getIsbn()).isEqualTo("1234567890");
        assertThat(foundBookInfo.get().getBookInfoStatus()).isEqualTo(BookInfoStatus.AVAILABLE);

        Optional<BookInfo> notFoundBookInfo = bookInfoRepository.findByIsbn("1111111111");

        assertThat(notFoundBookInfo).isNotPresent();
    }
}

