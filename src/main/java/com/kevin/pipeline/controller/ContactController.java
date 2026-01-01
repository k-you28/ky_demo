package com.kevin.pipeline.controller;

import com.kevin.pipeline.service.IngestService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.kevin.pipeline.entity.IngestRecord;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;


@Controller
public class ContactController {

    private final IngestService ingestService;
    public ContactController(IngestService ingestService) {
        this.ingestService = ingestService;
    }

    @GetMapping("/contactUs")
    public String showForm(HttpSession session,
                           Model model) {
        String key = UUID.randomUUID().toString();
        //session.setAttribute("IDEMPOTENCY_KEY", key);
        model.addAttribute("idempotencyKey", key);
        //session.removeAttribute("IDEMPOTENCY_KEY");
        return "contactUs";
    }

    @PostMapping("/contactUs")
    public String handleSubmit(@RequestParam("idempotencyKey") String RequestKey,
                               HttpServletRequest request,
                               @RequestParam("name") String name,
                               @RequestParam("message") String message,
                               Model model) {

        String ip = request.getRemoteAddr();
        System.out.println("REQUEST KEY: " + RequestKey);
        IngestRecord record = ingestService.ingest(RequestKey, ip, name, message);
        return "redirect:/contactUs?success=true";
    }
}
