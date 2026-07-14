package com.alumni.portal.controller;

import com.alumni.portal.service.AlumniService;
import com.alumni.portal.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    MessageService messageService;

    @Autowired
    AlumniService alumniService;

    // Inbox — show all alumni to message
    @GetMapping("")
    public String inbox(Model model, Authentication auth) {
        String currentUser = auth.getName();
        model.addAttribute("alumniList", alumniService.getAllAlumni());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("unreadCount",
            messageService.getUnreadCount(currentUser));
        return "messages/inbox";
    }

    // Chat with specific alumni
    @GetMapping("/chat/{email}")
    public String chat(@PathVariable String email,
                       Model model,
                       Authentication auth) {
        String currentUser = auth.getName();
        messageService.markAsRead(email, currentUser);
        model.addAttribute("messages",
            messageService.getConversation(currentUser, email));
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("receiver", email);
        model.addAttribute("receiverAlumni",
            alumniService.getAlumniByEmail(email));
        return "messages/chat";
    }

    // Send message
    @PostMapping("/send")
    public String sendMessage(@RequestParam String receiverEmail,
                              @RequestParam String content,
                              Authentication auth) {
        String currentUser = auth.getName();
        messageService.sendMessage(currentUser, receiverEmail, content);
        return "redirect:/messages/chat/" + receiverEmail;
    }
    
    @GetMapping("/unread-count")
    @ResponseBody
    public java.util.Map<String, Long> unreadCount(
            org.springframework.security.core.Authentication auth) {
        if (auth == null) {
            java.util.Map<String, Long> r = new java.util.HashMap<>();
            r.put("count", 0L);
            return r;
        }
        long count = messageService.getUnreadCount(auth.getName());
        java.util.Map<String, Long> result = new java.util.HashMap<>();
        result.put("count", count);
        return result;
    }
}