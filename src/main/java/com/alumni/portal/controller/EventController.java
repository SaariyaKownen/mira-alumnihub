package com.alumni.portal.controller;

import com.alumni.portal.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    EventService eventService;

    @GetMapping("")
    public String events(Model model) {
        model.addAttribute("upcomingEvents",
            eventService.getAllEvents());
        model.addAttribute("allEvents",
            eventService.getAllEvents());
        return "events";
    }
}