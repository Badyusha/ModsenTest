package com.modsen.bookstorageservice.controllers;

import com.modsen.bookstorageservice.enums.attributes.CreationStatus;
import com.modsen.bookstorageservice.models.dtos.BookDTO;
import com.modsen.bookstorageservice.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    void testCreateBook() throws Exception {
        BookDTO bookDTO = new BookDTO(1L, "12345", "Book Title", "Genre", "Description", "Author", CreationStatus.EXISTS);
        when(bookService.createBook(any(BookDTO.class))).thenReturn(bookDTO);

        mockMvc.perform(post("/book-storage-service/v1/books")
                        .contentType("application/json")
                        .content("{\"id\": 1, \"isbn\": \"12345\", \"title\": \"Book Title\", \"genre\": \"Genre\", \"description\": \"Description\", \"author\": \"Author\", \"creationStatus\": \"EXISTS\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.isbn").value("12345"))
                .andExpect(jsonPath("$.title").value("Book Title"))
                .andExpect(jsonPath("$.genre").value("Genre"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.author").value("Author"))
                .andExpect(jsonPath("$.creationStatus").value("EXISTS"));
    }

    @Test
    void testGetBookByIsbn() throws Exception {
        BookDTO bookDTO = new BookDTO(1L, "12345", "Book Title", "Genre", "Description", "Author", CreationStatus.EXISTS);
        when(bookService.getBookByIsbn("12345")).thenReturn(bookDTO);

        mockMvc.perform(get("/book-storage-service/v1/books/isbn/12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.isbn").value("12345"))
                .andExpect(jsonPath("$.title").value("Book Title"))
                .andExpect(jsonPath("$.genre").value("Genre"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.author").value("Author"))
                .andExpect(jsonPath("$.creationStatus").value("EXISTS"));
    }

    @Test
    void testGetBookById() throws Exception {
        BookDTO bookDTO = new BookDTO(1L, "12345", "Book Title", "Genre", "Description", "Author", CreationStatus.EXISTS);
        when(bookService.getBookById(1L)).thenReturn(bookDTO);

        mockMvc.perform(get("/book-storage-service/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.isbn").value("12345"))
                .andExpect(jsonPath("$.title").value("Book Title"))
                .andExpect(jsonPath("$.genre").value("Genre"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.author").value("Author"))
                .andExpect(jsonPath("$.creationStatus").value("EXISTS"));
    }

    @Test
    void testUpdateBook() throws Exception {
        BookDTO updatedBookDTO = new BookDTO(1L, "12345", "Updated Title", "Genre", "Updated Description", "Author", CreationStatus.EXISTS);
        when(bookService.updateBook(eq(1L), any(BookDTO.class))).thenReturn(updatedBookDTO);

        mockMvc.perform(put("/book-storage-service/v1/books/1")
                        .contentType("application/json")
                        .content("{\"id\": 1, \"isbn\": \"12345\", \"title\": \"Updated Title\", \"genre\": \"Genre\", \"description\": \"Updated Description\", \"author\": \"Author\", \"creationStatus\": \"EXISTS\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.isbn").value("12345"))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.genre").value("Genre"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.author").value("Author"))
                .andExpect(jsonPath("$.creationStatus").value("EXISTS"));
    }

    @Test
    void testDeleteBook() throws Exception {
        doNothing().when(bookService).deleteBook("12345");

        mockMvc.perform(delete("/book-storage-service/v1/books/12345"))
                .andExpect(status().isOk());
    }
}
