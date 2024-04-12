package com.lab1.isthesiteup.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.lab1.isthesiteup.dto.BulkUpdateRequest;
import com.lab1.isthesiteup.entities.Check;
import com.lab1.isthesiteup.services.CheckService;
import com.lab1.isthesiteup.services.CounterService;

import java.util.List;

@Controller
public class CheckController {

    private final CheckService checkService;
    private final CounterService counterService;
    private static final Logger logger = LoggerFactory.getLogger(CheckController.class);

    public CheckController(CheckService checkService, CounterService counterService) {
        this.checkService = checkService;
        this.counterService = counterService;
    }

    @GetMapping("/checks")
    @Operation(summary = "Get all checks")
    public String getAllChecks(Model model) {
        logger.info("Getting all checks");
        counterService.increment();
        List<Check> checks = checkService.getAllChecks();
        model.addAttribute("checks", checks);
        return "check";
    }

    @PostMapping("/check")
    @Operation(summary = "Check server status for a given URL")
    public String checkServerStatus(@RequestParam @Parameter(description = "URL to check") String url, Model model) {
        url = url.replaceAll("[\n\r]", "_");
        logger.info("Checking server status for URL: {}", url);
        if (url == null || url.isEmpty()) {
            logger.error("Invalid URL provided");
            throw new IllegalArgumentException("URL cannot be empty");
        }
        Check check = checkService.getServerStatus(url);
        Check savedCheck = checkService.saveCheck(check);
        model.addAttribute("url", savedCheck.getUrl());
        model.addAttribute("status", savedCheck.getStatus());

        return "checkurl";
    }

    @PutMapping("/check/update/{id}")
    @Operation(summary = "Update a check by ID")
    public String updateCheck(@PathVariable Long id, @ModelAttribute Check check, Model model) {
        logger.info("Updating check. ID: {}, Check: {}", id, check);
        if (check == null) {
            logger.error("Invalid check details provided");
            throw new IllegalArgumentException("Invalid check details");
        }
        checkService.updateCheck(id, check);
        return "redirect:/checks/update";
    }

    @GetMapping("/checks/update")
    @Operation(summary = "Show form for updating checks")
    public String showFormForUpdateCheck(Model model) {
        logger.info("Showing form for updating checks");
        List<Check> checks = checkService.getAllChecks();
        model.addAttribute("checks", checks);
        return "update-checks";
    }

    @DeleteMapping("/check/delete/{id}")
    @Operation(summary = "Delete a check by ID")
    public String deleteCheck(@PathVariable Long id, Model model) {
        logger.info("Deleting check. ID: {}", id);
        checkService.deleteCheck(id);
        return "redirect:/checks";
    }

    @PostMapping("/checks/bulk-update")
    public ResponseEntity<Void> bulkUpdateServerStatusThroughChecks(@RequestBody BulkUpdateRequest request) {
        checkService.bulkUpdateServerStatusThroughChecks(request.getServerIds(), request.getNewStatus());
        return ResponseEntity.ok().build();
    }
    
}