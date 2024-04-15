package com.dashboardapi.demo.error;

import com.dashboardapi.demo.entity.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@ResponseStatus
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(DashboardRecordNotFoundException.class)
    public ResponseEntity<ErrorMessage> dashboardRecordNotFoundException(DashboardRecordNotFoundException
                                                                                     dashboardRecordNotFoundException) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND,
                                        dashboardRecordNotFoundException.getMessage(),
                                        dashboardRecordNotFoundException.getCause());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(errorMessage);
    }
}
