package com.odoo.RoomBooking.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class Advice {
    @ResponseBody
    @ExceptionHandler(CommonException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String errorHandler(CommonException ex) {
        return ex.getMessage();
    }
}
