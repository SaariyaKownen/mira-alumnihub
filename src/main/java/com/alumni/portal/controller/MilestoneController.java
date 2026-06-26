package com.alumni.portal.controller;

import com.alumni.portal.model.Milestone;
import com.alumni.portal.repository.MilestoneRepository;
import com.alumni.portal.service.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
@RequestMapping("/milestones")
public class MilestoneController {

    @Autowired
    MilestoneRepository milestoneRepository;

    @Autowired
    PointsService pointsService;

    @GetMapping("")
    public String wall(Model model) {
        model.addAttribute("milestones", milestoneRepository.findAllByOrderByPostedDateDesc());
        model.addAttribute("milestone", new Milestone());
        return "milestones";
    }

 
    @PostMapping("/add")
    public String addMilestone(@ModelAttribute Milestone milestone, Authentication auth) {
        milestone.setPosterEmail(auth.getName());
        milestone.setPosterName(auth.getName().split("@")[0]);
        milestone.setPostedDate(LocalDate.now());
        milestoneRepository.save(milestone);
        pointsService.addPoints(auth.getName(), 8);
        return "redirect:/milestones?posted";
    }
    @GetMapping("/delete/{id}")
    public String deleteMilestone(@PathVariable Long id, Authentication auth) {
        Milestone m = milestoneRepository.findById(id).orElse(null);
        if (m != null && m.getPosterEmail().equals(auth.getName())) {
            milestoneRepository.deleteById(id);
        }
        return "redirect:/milestones";
    }
}