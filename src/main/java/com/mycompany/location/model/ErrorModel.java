package com.mycompany.location.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class ErrorModel implements Serializable {
    private String errorCode;
    private String errorMessage;
}
