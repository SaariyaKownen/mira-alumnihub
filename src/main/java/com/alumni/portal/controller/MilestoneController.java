package com.alumni.portal.controller;

import com.alumni.portal.model.Milestone;
import com.alumni.portal.repository.MilestoneRepository;
import com.alumni.portal.service.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

@Controller
@RequestMapping("/milestones")
public class MilestoneController {

    @Autowired
    MilestoneRepository milestoneRepository;

    @Autowired
    PointsService pointsService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("")
    public String wall(Model model, Authentication auth) {
        model.addAttribute("milestones",
            milestoneRepository.findAllByOrderByPostedDateDesc());
        model.addAttribute("milestone", new Milestone());
        model.addAttribute("currentUser",
            auth != null ? auth.getName() : "");
        return "milestones";
    }

    @PostMapping("/add")
    public String addMilestone(
            @ModelAttribute Milestone milestone,
            @RequestParam(value = "image", required = false)
                MultipartFile image,
            Authentication auth) throws Exception {

        milestone.setPosterEmail(auth.getName());
        milestone.setPosterName(auth.getName().split("@")[0]);
        milestone.setPostedDate(LocalDate.now());

        if (image != null && !image.isEmpty()) {
            String filename = System.currentTimeMillis()
                + "_" + image.getOriginalFilename()
                    .replaceAll("[^a-zA-Z0-9._-]", "_");
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            image.transferTo(uploadPath.resolve(filename).toFile());
            milestone.setImageUrl("/photos/" + filename);
        }

        milestoneRepository.save(milestone);
        pointsService.addPoints(auth.getName(), 8);
        return "redirect:/milestones?posted";
    }

    @GetMapping("/delete/{id}")
    public String deleteMilestone(@PathVariable Long id,
                                   Authentication auth) {
        Milestone m = milestoneRepository.findById(id).orElse(null);
        if (m != null && m.getPosterEmail().equals(auth.getName())) {
            milestoneRepository.deleteById(id);
        }
        return "redirect:/milestones";
    }
}