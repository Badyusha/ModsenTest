package com.modsen.bookstorageservice.repositories;

import com.modsen.bookstorageservice.models.entities.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties", properties = "spring.liquibase.enabled=false")
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("book_storage")
            .withUsername("root")
            .withPassword("root");

    @BeforeAll
    public static void setUp() {
        mysqlContainer.start();

        System.setProperty("spring.datasource.url", mysqlContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", mysqlContainer.getUsername());
        System.setProperty("spring.datasource.password", mysqlContainer.getPassword());
    }

    @Test
    public void testFindByIsbn() {
        Book book = new Book();
        book.setId(100L);
        book.setIsbn("1234567890");
        book.setTitle("Test Book");
        book.setAuthor("Test Author");

        bookRepository.save(book);

        Optional<Book> foundBook = bookRepository.findByIsbn("1234567890");

        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getIsbn()).isEqualTo("1234567890");
        assertThat(foundBook.get().getTitle()).isEqualTo("Test Book");
    }
}
