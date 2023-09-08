package com.sinha.resteazy;

import com.sinha.resteazy.entities.ErrorResponse;
import com.sinha.resteazy.exceptions.DuplicateEntryException;
import com.sinha.resteazy.exceptions.RestaurantNotFoundException;
import com.sinha.resteazy.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> restaurantExceptionHandler(RestaurantNotFoundException exception) {
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage(), System.currentTimeMillis());
        logger.error(this.getClass().toString(), exception);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> restaurantExceptionHandler(UserNotFoundException exception) {
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage(), System.currentTimeMillis());
        logger.error(this.getClass().toString(), exception);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> duplicateEntryExceptionHandler(DuplicateEntryException exception) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), System.currentTimeMillis());
        logger.error(this.getClass().toString(), exception);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
