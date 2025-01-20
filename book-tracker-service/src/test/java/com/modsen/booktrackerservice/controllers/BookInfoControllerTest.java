package com.modsen.booktrackerservice.controllers;

import com.modsen.booktrackerservice.models.dtos.BookInfoDTO;
import com.modsen.booktrackerservice.services.BookInfoService;
import com.modsen.commonmodels.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookInfoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookInfoService bookInfoService;

    @InjectMocks
    private BookInfoController bookInfoController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookInfoController).build();
    }

    @Test
    void testCreateBookInfo() throws Exception {
        BookInfoDTO bookInfoDTO = new BookInfoDTO(1L, "12345", null, null, null, null);
        doReturn(bookInfoDTO).when(bookInfoService).createBook(any(BookInfoDTO.class));

        mockMvc.perform(post("/book-tracker-service/v1/books")
                        .contentType("application/json")
                        .content("{\"id\": 1, \"isbn\": \"12345\", \"userId\": null, \"bookInfoStatus\": null, \"borrowedAt\": null, \"returnDue\": null}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.isbn").value("12345"))
                .andExpect(jsonPath("$.userId").doesNotExist())
                .andExpect(jsonPath("$.bookInfoStatus").doesNotExist())
                .andExpect(jsonPath("$.borrowedAt").doesNotExist())
                .andExpect(jsonPath("$.returnDue").doesNotExist());
    }

    @Test
    void testGetAvailableBooks() throws Exception {
        BookInfoDTO bookInfoDTO = new BookInfoDTO(1L, "12345", null, null, null, null);
        when(bookInfoService.getAvailableBooks()).thenReturn(List.of(bookInfoDTO));

        mockMvc.perform(get("/book-tracker-service/v1/books/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].isbn").value("12345"))
                .andExpect(jsonPath("$[0].userId").doesNotExist())
                .andExpect(jsonPath("$[0].bookInfoStatus").doesNotExist())
                .andExpect(jsonPath("$[0].borrowedAt").doesNotExist())
                .andExpect(jsonPath("$[0].returnDue").doesNotExist());
    }

    @Test
    void testBorrowBook() throws Exception {
        String token = "Bearer some-token";
        BookInfoDTO bookInfoDTO = new BookInfoDTO(1L, "12345", 100L, null, LocalDateTime.now(), LocalDateTime.now().plusDays(14));
        when(bookInfoService.borrowBook(eq(token), eq("12345"))).thenReturn(bookInfoDTO);

        mockMvc.perform(put("/book-tracker-service/v1/books/12345/borrow")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.isbn").value("12345"))
                .andExpect(jsonPath("$.userId").value(100))
                .andExpect(jsonPath("$.borrowedAt").isNotEmpty())
                .andExpect(jsonPath("$.returnDue").isNotEmpty());
    }

    @Test
    void testReturnBook() throws Exception {
        String token = "Bearer some-token";
        BookInfoDTO bookInfoDTO = new BookInfoDTO(1L, "12345", null, null, null, null);
        when(bookInfoService.returnBook(eq(token), eq("12345"))).thenReturn(bookInfoDTO);

        mockMvc.perform(put("/book-tracker-service/v1/books/12345/return")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.isbn").value("12345"))
                .andExpect(jsonPath("$.userId").doesNotExist())
                .andExpect(jsonPath("$.borrowedAt").doesNotExist())
                .andExpect(jsonPath("$.returnDue").doesNotExist());
    }

    @Test
    void testDeleteBookInfo() throws Exception {
        doNothing().when(bookInfoService).deleteBookInfo("12345");

        mockMvc.perform(delete("/book-tracker-service/v1/books/12345"))
                .andExpect(status().isOk());

        verify(bookInfoService, times(1)).deleteBookInfo("12345");
    }

    @Test
    void testDeleteBookInfo_NotFound() throws Exception {
        doThrow(new ObjectNotFoundException("Book not found")).when(bookInfoService).deleteBookInfo("12345");

        mockMvc.perform(delete("/book-tracker-service/v1/books/12345"))
                .andExpect(status().isNotFound());
    }
}
