package com.lab1.isthesiteup.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.lab1.isthesiteup.entities.ServerEntity;
import com.lab1.isthesiteup.services.CheckService;
import com.lab1.isthesiteup.services.ServerService;

@Controller
public class ServerController {

    private final ServerService serverService;
    private final CheckService checkService;


    public ServerController(ServerService serverService, CheckService checkService) {
        this.serverService = serverService;
        this.checkService = checkService;
    }

    @GetMapping("/")
    public String server(Model model) {
        model.addAttribute("servers", serverService.getAllServers());
        model.addAttribute("checks", checkService.getAllChecks());
        return "server";
    }

    @PostMapping("/server")
    public String addServer(@ModelAttribute ServerEntity server, Model model, RedirectAttributes redirectAttributes) {
        try {
            serverService.addServer(server);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            // Add error message to the model and redirect back to the form
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }
    
    
    @PutMapping("/server/{id}")
    public String updateServer(@PathVariable Long id, @ModelAttribute ServerEntity server, Model model) {
        try {
            serverService.updateServer(id, server);
            model.addAttribute("servers", serverService.getAllServers());
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update server: " + e.getMessage());
            return "redirect:/";
        }
    }



    @DeleteMapping("/server/{id}")
    public String deleteServer(@PathVariable Long id, Model model) {
        serverService.deleteServer(id);
        return "redirect:/";
    }

}





























// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.SessionAttributes;
// import com.lab1.isthesiteup.services.ServerService;

// import java.util.List;

// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.PostMapping;

// @Controller
// @SessionAttributes("userId") 
// public class ServerController {

//     private final ServerService urlCheckService;

//     public ServerController(ServerService urlCheckService) {
//         this.urlCheckService = urlCheckService;
//     }

//     @GetMapping("/main-page/{userId}")
//     public String showCheckForm(@PathVariable Long userId, Model model) {
//         model.addAttribute("userId", userId); 
//         return "check";
//     }

//     @PostMapping("/check/{userId}")
//     public String checkUrl(@PathVariable Long userId, @RequestParam String url, Model model) {
//     UrlCheckEntity result = urlCheckService.checkUrl(url, userId);
//         model.addAttribute("message", result.getStatus());
//         model.addAttribute("url", result.getUrl());
//         model.addAttribute("time", result.getTime());
//         return "check";
//     }
   

//     private String showUrlChecksForPeriod(@PathVariable Long userId,String period, Model model) {
//         List<UrlCheckEntity> urlChecks;
//         switch (period) {
//             case "today":
//                 urlChecks = urlCheckService.getUrlChecksForToday();
//                 break;
//             case "yesterday":
//                 urlChecks = urlCheckService.getUrlChecksForYesterday();
//                 break;
//             case "this-week":
//                 urlChecks = urlCheckService.getUrlChecksForThisWeek();
//                 break;
//             default:
//                 throw new IllegalArgumentException("Invalid period: " + period);
//         }
//         model.addAttribute("allUrlChecks", urlChecks);
//         model.addAttribute("period", period);
//         model.addAttribute("userId", userId);
           
//         return "all-checks";
//     }

//     @GetMapping("/{userId}/{period}")
//     public String showUrlChecks(@PathVariable Long userId, @PathVariable String period, Model model) { 
//         return showUrlChecksForPeriod(userId, period, model);
//     }


//     @DeleteMapping("/all-checks/{id}/{period}")
//     public String deleteUrlCheck(@PathVariable Long id, @PathVariable String period) {
//         urlCheckService.deleteUrlCheck(id);
//         return "redirect:/" + period;
//     }
    
// }
