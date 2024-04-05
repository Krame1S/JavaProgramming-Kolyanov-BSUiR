package com.lab1.isthesiteup.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.lab1.isthesiteup.entities.Check;
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
        List<Check> checks = checkService.getAllChecks();
        model.addAttribute("checks", checks);
        return "check";
    }

    @PostMapping("/check")
    public String checkServerStatus(@RequestParam String url, Model model) {
        // Get the check with the status and associated server
        Check check = checkService.getServerStatus(url);

        // Save the check
        Check savedCheck = checkService.saveCheck(check);

        // Add the URL and status to the model
        model.addAttribute("url", savedCheck.getUrl());
        model.addAttribute("status", savedCheck.getStatus());

        return "checkurl";
    }



    @PutMapping("/check/update/{id}")
    public String updateCheck(@PathVariable Long id, @ModelAttribute Check check, Model model) {
        checkService.updateCheck(id, check);
        return "redirect:/checks/update";
    }

    @GetMapping("/checks/update")
    public String showFormForUpdateCheck(Model model) {
        List<Check> checks = checkService.getAllChecks();
        model.addAttribute("checks", checks);
        return "update-checks";
    }

    @DeleteMapping("/check/delete/{id}")
    public String deleteCheck(@PathVariable Long id, Model model) {
        checkService.deleteCheck(id);
        return "redirect:/checks";
    }
}
