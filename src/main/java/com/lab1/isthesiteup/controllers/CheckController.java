package com.lab1.isthesiteup.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.lab1.isthesiteup.entities.CheckEntity;
import com.lab1.isthesiteup.services.CheckService;

import java.util.List;

@Controller
public class CheckController {

    private final CheckService checkService;
    private final Logger logger = LoggerFactory.getLogger(CheckController.class);

    public CheckController(CheckService checkService) {
        this.checkService = checkService;
    }

    @GetMapping("/checks")
    public String getAllChecks(Model model) {
        try {
            logger.info("Accessing all checks");
            List<CheckEntity> checks = checkService.getAllChecks();
            model.addAttribute("checks", checks);
            return "check";
        } catch (Exception e) {
            logger.error("Failed to get all checks", e);
            handleException(e, model);
            return "error";
        }
    }

    @PostMapping("/check")
    public String checkServerStatus(@RequestParam String url, Model model) {
        try {
            logger.info("Checking server status for URL: {}", url);
            CheckEntity check = checkService.getServerStatus(url);
            CheckEntity savedCheck = checkService.saveCheckEntity(check);
            model.addAttribute("url", savedCheck.getUrl());
            model.addAttribute("status", savedCheck.getStatus());
            return "checkurl";
        } catch (Exception e) {
            logger.error("Failed to check server status", e);
            handleException(e, model);
            return "error";
        }
    }
    
    @PutMapping("/check/update/{id}")
    public String updateCheck(@PathVariable Long id, @ModelAttribute CheckEntity checkEntity, Model model) {
        try {
            logger.info("Updating check with ID: {}", id);
            checkService.updateCheck(id, checkEntity);
            return "redirect:/checks/update";
        } catch (Exception e) {
            logger.error("Failed to update check", e);
            handleException(e, model);
            return "error";
        }
    }
    
    @GetMapping("/checks/update")
    public String showFormForUpdateCheck(Model model) {
        try {
            logger.info("Accessing form for updating checks");
            List<CheckEntity> checks = checkService.getAllChecks();
            model.addAttribute("checks", checks);
            return "update-checks";
        } catch (Exception e) {
            logger.error("Failed to access form for updating checks", e);
            handleException(e, model);
            return "error";
        }
    }

    @DeleteMapping("/check/delete/{id}")
    public String deleteCheck(@PathVariable Long id, Model model) {
        try {
            //logger.info("Deleting check with ID: {}", id);
            checkService.deleteCheck(id);
            return "redirect:/checks";
        } catch (Exception e) {
            logger.error("Failed to delete check", e);
            handleException(e, model);
            return "error";
        }
    }

    private void handleException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
    }
}
