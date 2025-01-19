package com.modsen.commonmodels.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class RequestDTO implements Serializable {
    private String isbn;
    private Long userId;
}
