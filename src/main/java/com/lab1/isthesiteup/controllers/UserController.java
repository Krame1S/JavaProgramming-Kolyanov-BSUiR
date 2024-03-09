package com.lab1.isthesiteup.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lab1.isthesiteup.services.UserService;


@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }   
    @GetMapping("/")
    public String showRegisterForm(Model model) {
        return "register";
    }    

    @PostMapping("/register")
    public String registerUser(@RequestParam("login") String login, @RequestParam("password") String password, Model model)  {
        if (!userService.createUser(login, password)) {
            model.addAttribute("error", "User with this login already exists");
            return "/register";
        }
        model.addAttribute("login", login);
        model.addAttribute("password", password);
        return "/check";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("login") String login, @RequestParam("password") String password, Model model) {
        if (!userService.checkUser(login, password)) {
            model.addAttribute("error", "Incorrect login or password");
            return "/login";
        }
        model.addAttribute("login", login);
        model.addAttribute("password", password);
        return "/main-page";
    }    
}
