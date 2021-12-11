package com.mycompany.location.exception;

import com.mycompany.location.model.ErrorModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BusinessException extends Exception {
    private final List<ErrorModel> ERROR_LIST;
}
