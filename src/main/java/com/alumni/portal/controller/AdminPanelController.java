package com.alumni.portal.controller;

import com.alumni.portal.model.Event;
import com.alumni.portal.model.Alumni;
import com.alumni.portal.service.AlumniService;
import com.alumni.portal.service.EventService;
import com.alumni.portal.service.JobPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminPanelController {

    @Autowired AlumniService alumniService;
    @Autowired JobPostService jobPostService;
    @Autowired EventService eventService;

    @GetMapping("/control")
    public String controlPanel(Model model) {
        model.addAttribute("totalAlumni",
            alumniService.getAlumniOnly().size());
        model.addAttribute("totalStudents",
            alumniService.getStudentsOnly().size());
        model.addAttribute("totalJobs",
            jobPostService.getAllJobs().size());
        model.addAttribute("allEvents",
            eventService.getAllEvents());
        model.addAttribute("upcomingEvents",
            eventService.getAllEvents());
        model.addAttribute("event", new Event());

        List<Alumni> people = alumniService.getAlumniOnly();
        people.addAll(alumniService.getStudentsOnly());
        model.addAttribute("allPeople", people);

        return "admin/control";
    }

    @PostMapping("/control/add-event")
    public String addEvent(@ModelAttribute Event event) {
        eventService.addEvent(event);
        return "redirect:/admin/control";
    }

    @GetMapping("/control/delete-event/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "redirect:/admin/control";
    }

    @GetMapping("/control/delete-person/{id}")
    public String deletePerson(@PathVariable Long id) {
        alumniService.deleteAlumni(id);
        return "redirect:/admin/control";
    }
}