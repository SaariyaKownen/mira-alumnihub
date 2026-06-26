package com.alumni.portal.controller;

import com.alumni.portal.model.Alumni;
import com.alumni.portal.service.AIService;
import com.alumni.portal.service.AlumniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ai")
public class AIController {

    @Autowired
    AIService aiService;

    @Autowired
    AlumniService alumniService;

    @GetMapping("/career")
    public String careerPage(Model model, Authentication auth) {
        Alumni alumni = alumniService.getAlumniByEmail(auth.getName());
        model.addAttribute("alumni", alumni);
        return "ai/career";
    }

    @PostMapping("/career")
    public String getCareerAdvice(Authentication auth, Model model) {
        Alumni alumni = alumniService.getAlumniByEmail(auth.getName());
        String advice = aiService.getCareerAdvice(
            alumni.getFullName(), alumni.getCurrentJob(),
            alumni.getCompany(), alumni.getGraduationYear());
        model.addAttribute("alumni", alumni);
        model.addAttribute("advice", advice);
        return "ai/career";
    }

    @GetMapping("/bio")
    public String bioPage(Model model, Authentication auth) {
        Alumni alumni = alumniService.getAlumniByEmail(auth.getName());
        model.addAttribute("alumni", alumni);
        return "ai/bio";
    }

    @PostMapping("/bio")
    public String generateBio(Authentication auth, Model model) {
        Alumni alumni = alumniService.getAlumniByEmail(auth.getName());
        String bio = aiService.generateBio(
            alumni.getFullName(), alumni.getCurrentJob(),
            alumni.getCompany(), alumni.getGraduationYear());
        model.addAttribute("alumni", alumni);
        model.addAttribute("bio", bio);
        return "ai/bio";
    }

    @GetMapping("/message-assist")
    public String messageAssistPage(Model model, Authentication auth) {
        Alumni sender = alumniService.getAlumniByEmail(auth.getName());
        model.addAttribute("sender", sender);
        model.addAttribute("alumniList", alumniService.getAllAlumni());
        return "ai/message-assist";
    }

    @PostMapping("/message-assist")
    public String generateMessage(
            @RequestParam String receiverEmail,
            @RequestParam String purpose,
            Authentication auth, Model model) {
        Alumni sender = alumniService.getAlumniByEmail(auth.getName());
        Alumni receiver = alumniService.getAlumniByEmail(receiverEmail);
        String message = aiService.generateMessage(
            sender.getFullName(), receiver.getFullName(),
            receiver.getCurrentJob(), purpose);
        model.addAttribute("sender", sender);
        model.addAttribute("alumniList", alumniService.getAllAlumni());
        model.addAttribute("generatedMessage", message);
        model.addAttribute("selectedReceiver", receiverEmail);
        return "ai/message-assist";
    }

    @GetMapping("/search")
    public String smartSearchPage(Model model) {
        return "ai/smart-search";
    }

    @PostMapping("/search")
    public String smartSearch(@RequestParam String query, Model model) {
        List<Alumni> allAlumni = alumniService.getAllAlumni();
        String alumniData = allAlumni.stream()
            .map(a -> a.getFullName() + " (" + a.getEmail() + ") - " +
                      a.getCurrentJob() + " at " + a.getCompany() +
                      ", graduated " + a.getGraduationYear())
            .collect(Collectors.joining("; "));
        String result = aiService.smartSearch(query, alumniData);
        model.addAttribute("query", query);
        model.addAttribute("result", result);
        return "ai/smart-search";
    }
}