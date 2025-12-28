package com.kevin.pipeline.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ContactController {

    @GetMapping("/contactUs")
    public String showForm() {
        System.out.println("AHHHH");
        return "contactUs";
    }

    @PostMapping("/contactUs")
    public String handleSubmit(
            @RequestParam String name,
            @RequestParam String message,
            Model model
    ) {
        // For now, just echo back what was submitted
        model.addAttribute("name", name);
        model.addAttribute("message", message);

        return "contactUs";
    }

}
