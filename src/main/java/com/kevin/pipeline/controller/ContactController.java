package com.kevin.pipeline.controller;

import com.kevin.pipeline.service.IngestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.kevin.pipeline.entity.IngestRecord;
import jakarta.servlet.http.HttpServletRequest;


@Controller
public class ContactController {

    private final IngestService ingestService;
    public ContactController(IngestService ingestService) {
        this.ingestService = ingestService;
    }

    @GetMapping("/contactUs")
    public String showForm() {
        //System.out.println("AHHHH");
        //IngestRecord record = ingestService.ingest(null, null, null, "GET REQUEST MADE");
        return "contactUs";
    }

    @PostMapping("/contactUs")
    public String handleSubmit(@RequestHeader(value="Idempotency-Key", required=false) String RequestKey, HttpServletRequest request, @RequestParam String name, @RequestParam String message, Model model) {
        // For now, just echo back what was submitted
        model.addAttribute("name", name);
        model.addAttribute("message", message);
        String ip = request.getRemoteAddr();
        IngestRecord record = ingestService.ingest(RequestKey, ip, name, message);

        return "contactUs";
        //return "redirect:/contactUs?success=true";
    }

}
