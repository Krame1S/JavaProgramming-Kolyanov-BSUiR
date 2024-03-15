package com.lab1.isthesiteup.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.lab1.isthesiteup.services.UserService;

import jakarta.servlet.http.HttpSession;


@Controller
@SessionAttributes("userId")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }   
    @GetMapping("/")
    public String showRegisterForm(Model model) {
        return "register";
    }    

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }

    @GetMapping("/registration-successful")
    public String showRegistrationSuccessForm(Model model) {
        return "registration-successful";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("login") String login, @RequestParam("password") String password, Model model)  {
        if (!userService.createUser(login, password)) {
            model.addAttribute("error", "User with this login already exists");
            return "register";
        }
        model.addAttribute("login", login);
        model.addAttribute("password", password);
        return "/registration-successful";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("login") String login, @RequestParam("password") String password, HttpSession session, Model model) {
        Long userId = userService.checkUser(login, password);
        if (userId == null) {
            model.addAttribute("error", "Incorrect login or password");
            return "/login";
        }
        session.setAttribute("userId", userId); 
        return "redirect:/main-page/" + userId;
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        return "redirect:/login";
    }

}
