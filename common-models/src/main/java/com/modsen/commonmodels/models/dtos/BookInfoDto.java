package com.modsen.commonmodels.models.dtos;

import com.modsen.commonmodels.enums.attributes.BookInfoStatus;
import com.modsen.commonmodels.exceptions.ObjectNotFoundException;
import com.modsen.commonmodels.models.entities.BookInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class BookInfoDto implements Serializable {
    private Long bookId;

    public BookInfo toBookInfo() throws ObjectNotFoundException {
        Long bookId = this.getBookId();
        if (bookId == null) {
            throw new ObjectNotFoundException("BookInfo cannot be created. Book id is missing!");
        }

        return new BookInfo(bookId, BookInfoStatus.AVAILABLE, null, null);
    }
}
