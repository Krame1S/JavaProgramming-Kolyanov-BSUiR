package com.lab1.isthesiteup.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleValidationExceptions(Exception ex) {
        logger.error("Validation error: {}", ex.getMessage());
        ModelAndView modelAndView = new ModelAndView("error-400"); 
        modelAndView.addObject("errorMessage", "Validation error: " + ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleInternalServerError(Throwable ex) {
        logger.error("Internal server error: {}", ex.getMessage());
        ModelAndView modelAndView = new ModelAndView("error-500"); 
        modelAndView.addObject("errorMessage", "Internal server error");
        return modelAndView;
    }
}