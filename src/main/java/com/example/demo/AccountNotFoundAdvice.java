package com.example.demo;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
class AccountNotFoundAdvice {

    @ResponseBody // signals that this advice is rendered straight into the response body.
    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String AccountNotFoundHandler(AccountNotFoundException ex) {
        return ex.getMessage();
    }
}
