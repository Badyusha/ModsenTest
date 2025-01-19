package com.modsen.booktrackerservice.utils;

import com.modsen.booktrackerservice.enums.attributes.BookInfoStatus;
import com.modsen.booktrackerservice.models.entities.BookInfo;
import com.modsen.commonmodels.exceptions.NoSuchUserException;
import com.modsen.commonmodels.exceptions.PermissionException;

public class BookInfoUtil {

    public static void validateUpdateRequest(BookInfo bookInfo, Long userId) {
        BookInfoStatus bookInfoStatus = bookInfo.getBookInfoStatus();
        if(bookInfoStatus.equals(BookInfoStatus.DELETED)) {
            throw new PermissionException("Book with status DELETED can't be updated");
        }

        Long bookInfoUserId = bookInfo.getUserId();
        if(bookInfoUserId == null || !bookInfoUserId.equals(userId)) {
            throw new PermissionException("User with provided id can't update book status");
        }
    }
}
