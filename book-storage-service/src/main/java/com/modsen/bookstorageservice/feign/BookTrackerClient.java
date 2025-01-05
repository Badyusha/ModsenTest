package com.modsen.bookstorageservice.feign;

import com.modsen.commonmodels.models.dtos.BookInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "book-tracker-service",url = "http://localhost:8081/book-tracker-service/v1/books")
public interface BookTrackerClient {
    @PostMapping
    void createBookInBookTrackerService(@RequestBody BookInfoDto bookInfoDto);
}

