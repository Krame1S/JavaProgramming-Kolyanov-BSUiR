package com.lab1.isthesiteup.controllers;

import com.lab1.isthesiteup.dto.BulkUpdateRequest;
import com.lab1.isthesiteup.entities.Check;
import com.lab1.isthesiteup.services.CheckService;
import com.lab1.isthesiteup.services.CounterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CheckController {

    private final CheckService checkService;
    private final CounterService counterService;
    private static final Logger logger = LoggerFactory.getLogger(CheckController.class);

    public CheckController(CheckService checkService, CounterService counterService) {
        this.checkService = checkService;
        this.counterService = counterService;
    }

    @GetMapping("/checks")
    public ResponseEntity<List<Check>> getAllChecks() {
        logger.info("Getting all checks");
        counterService.increment();
        List<Check> checks = checkService.getAllChecks();
        return ResponseEntity.ok(checks);
    }

    @PostMapping("/check")
    public ResponseEntity<Check> checkServerStatus(@RequestParam String url) {
        url = url.replaceAll("[\n\r]", "_");
        logger.info("Checking server status for URL: {}", url);
        counterService.increment();
        if (url == null || url.isEmpty()) {
            logger.error("Invalid URL provided");
            return ResponseEntity.badRequest().build();
        }
        Check check = checkService.getServerStatus(url);
        Check savedCheck = checkService.saveCheck(check);
        return ResponseEntity.ok(savedCheck);
    }

    @PutMapping("/check/update/{id}")
    public ResponseEntity<Void> updateCheck(@PathVariable Long id, @RequestBody Check check) {
        logger.info("Updating check. ID: {}, Check: {}", id, check);
        if (check == null) {
            logger.error("Invalid check details provided");
            return ResponseEntity.badRequest().build();
        }
        checkService.updateCheck(id, check);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/checks/update")
    public ResponseEntity<List<Check>> showFormForUpdateCheck() {
        logger.info("Showing form for updating checks");
        List<Check> checks = checkService.getAllChecks();
        return ResponseEntity.ok(checks);
    }

    @DeleteMapping("/check/delete/{id}")
    public ResponseEntity<Void> deleteCheck(@PathVariable Long id) {
        logger.info("Deleting check. ID: {}", id);
        checkService.deleteCheck(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/checks/bulk-update")
    public ResponseEntity<Void> bulkUpdateServerStatusThroughChecks(@RequestBody BulkUpdateRequest request) {
        checkService.bulkUpdateServerStatusThroughChecks(request.getServerIds(), request.getNewStatus());
        return ResponseEntity.ok().build();
    }
}
