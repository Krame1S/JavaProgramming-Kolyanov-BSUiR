package com.lab1.isthesiteup.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.lab1.isthesiteup.entities.CheckEntity;
import com.lab1.isthesiteup.services.CheckService;

import java.util.List;

@Controller
public class CheckController {

    private final CheckService checkService;

    public CheckController(CheckService checkService) {
        this.checkService = checkService;
    }

    @GetMapping("/checks")
    public String getAllChecks(Model model) {
        List<CheckEntity> checks = checkService.getAllChecks();
        model.addAttribute("checks", checks);
        return "check";
    }

    @PostMapping("/check")
    public String checkServerStatus(@RequestParam String url, Model model) {
        CheckEntity check = checkService.checkServerStatus(url);
        model.addAttribute("url", check.getUrl());
        model.addAttribute("status", check.getStatus()); 
        return "checkurl";
    }

    @PutMapping("/check/edit/{id}")
    public String updateCheck(@PathVariable Long id, @ModelAttribute CheckEntity checkEntity, Model model) {
        checkService.updateCheck(id, checkEntity);
        return "redirect:/checks";
    }

    @DeleteMapping("/check/delete/{id}")
    public String deleteCheck(@PathVariable Long id, Model model) {
        checkService.deleteCheck(id);
        return "redirect:/checks";
    }
}
