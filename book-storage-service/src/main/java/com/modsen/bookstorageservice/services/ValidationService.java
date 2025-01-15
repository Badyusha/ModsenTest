package com.modsen.bookstorageservice.services;

import com.modsen.bookstorageservice.models.dtos.BookDTO;
import com.modsen.commonmodels.exceptions.ValidationException;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    private final int ISBN_LENGTH = 10;

    public void validate(BookDTO bookDTO) throws ValidationException {
        if(validateStrings(bookDTO)) {
            throw new ValidationException("Some strings are blank !");
        }
        if(bookDTO.getIsbn().length() != ISBN_LENGTH) {
            throw new ValidationException("ISBN length should be 10 !");
        }
    }

    private boolean validateStrings(BookDTO bookDTO) {
        return bookDTO.getIsbn().isBlank() || bookDTO.getTitle().isBlank() ||
               bookDTO.getGenre().isBlank() || bookDTO.getDescription().isBlank() ||
               bookDTO.getAuthor().isBlank();
    }
}
