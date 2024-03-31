// package com.lab1.isthesiteup.exceptions;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.ControllerAdvice;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.client.HttpClientErrorException;
// import org.springframework.web.client.HttpServerErrorException;
// //import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// @ControllerAdvice
// public class GlobalExceptionHandler {

//     private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

//     // @ExceptionHandler(Exception.class)
//     // public String handleException(Exception e) {
//     //     logger.error("An exception occurred: ", e);
//     //     return "error";
//     // }

//     // @ExceptionHandler(IllegalArgumentException.class)
//     // public String handleIllegalArgumentException(IllegalArgumentException e, RedirectAttributes redirectAttributes) {
//     //     logger.warn("An IllegalArgumentException occurred: ", e);
//     //     redirectAttributes.addFlashAttribute("error", e.getMessage());
//     //     return "redirect:/checks";
//     // }

//     // @ExceptionHandler(HttpClientErrorException.class)
//     // public String handleClientErrorException(HttpClientErrorException e, Model model) {
//     //     logger.warn("A HttpClientErrorException occurred: ", e);
//     //     model.addAttribute("error", e.getMessage());
//     //     return "error";
//     // }

//     // @ExceptionHandler(HttpServerErrorException.class)
//     // public String handleServerErrorException(HttpServerErrorException e, Model model) {
//     //     logger.error("A HttpServerErrorException occurred: ", e);
//     //     model.addAttribute("error", e.getMessage());
//     //     return "error";
//     // }
// }