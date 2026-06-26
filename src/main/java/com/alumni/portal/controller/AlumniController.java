package com.alumni.portal.controller;
import java.util.List;
import com.alumni.portal.model.Alumni;
import com.alumni.portal.model.JobPost;
import com.alumni.portal.service.AlumniService;
import com.alumni.portal.service.EventService;
import com.alumni.portal.service.JobPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class AlumniController {

    @Autowired AlumniService alumniService;
    @Autowired JobPostService jobPostService;
    @Autowired EventService eventService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // Landing Page
    @GetMapping("/")
    public String landingPage() {
        return "landing";
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        List<Alumni> alumni = alumniService.getAlumniOnly();
        List<Alumni> students = alumniService.getStudentsOnly();

        model.addAttribute("totalAlumni", alumni.size());
        model.addAttribute("totalStudents", students.size());
        model.addAttribute("totalJobs", jobPostService.getAllJobs().size());
        model.addAttribute("totalEvents", eventService.getAllEvents().size());

        // Recent 3 alumni
        model.addAttribute("recentAlumni",
            alumni.stream()
                .skip(Math.max(0, alumni.size() - 3))
                .collect(java.util.stream.Collectors.toList()));

        // Recent 3 students
        model.addAttribute("recentStudents",
            students.stream()
                .skip(Math.max(0, students.size() - 3))
                .collect(java.util.stream.Collectors.toList()));

        return "home";
    }

    // Login
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Alumni Registration
    @GetMapping("/register")
    public String showAlumniForm(Model model) {
        model.addAttribute("alumni", new Alumni());
        return "register";
    }

    @PostMapping("/register")
    public String submitAlumniForm(@ModelAttribute Alumni alumni,
                             @RequestParam("photo") MultipartFile photo)
                             throws Exception {
        alumni.setRole("ALUMNI");
        alumniService.register(alumni, photo);
        return "redirect:/login?registered";
    }

    // Student Registration
    @GetMapping("/register-student")
    public String showStudentForm(Model model) {
        model.addAttribute("alumni", new Alumni());
        return "register-student";
    }

    @PostMapping("/register-student")
    public String submitStudentForm(@ModelAttribute Alumni alumni,
                              @RequestParam("photo") MultipartFile photo)
                              throws Exception {
        alumni.setRole("STUDENT");
        alumniService.register(alumni, photo);
        return "redirect:/login?registered";
    }

    // Alumni Directory
    @GetMapping("/directory")
    public String directory(
            @RequestParam(required = false) String keyword,
            Model model) {
        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("alumniList",
                alumniService.searchAlumni(keyword));
        } else {
            model.addAttribute("alumniList",
                alumniService.getAlumniOnly());
        }
        model.addAttribute("keyword", keyword);
        return "directory";
    }

    // Student Directory
    @GetMapping("/students")
    public String students(Model model) {
        model.addAttribute("studentList",
            alumniService.getStudentsOnly());
        return "students";
    }

    // View Profile
    @Autowired
    com.alumni.portal.service.AIService aiService;

    @GetMapping("/profile/{id}")
    public String viewProfile(@PathVariable Long id, Model model,
                              Authentication auth) {
        Alumni profile = alumniService.getAlumniById(id);
        Alumni currentUser = alumniService
            .getAlumniByEmail(auth.getName());
        model.addAttribute("alumni", profile);
        model.addAttribute("currentUser", currentUser);

        // Auto-generate bio if alumni has job info
        if (profile.getRole() != null
                && profile.getRole().equals("ALUMNI")
                && profile.getCurrentJob() != null
                && !profile.getCurrentJob().isEmpty()) {
            try {
                String bioPrompt = "Write a short 3-sentence professional "
                    + "LinkedIn-style bio for " + profile.getFullName()
                    + " who works as " + profile.getCurrentJob()
                    + " at " + profile.getCompany()
                    + " and graduated in " + profile.getGraduationYear()
                    + ". Make it engaging and professional.";
                model.addAttribute("autoBio", aiService.callAI(bioPrompt));

                String careerPrompt = "Give 3 short career tips for "
                    + profile.getFullName()
                    + " who is a " + profile.getCurrentJob()
                    + " at " + profile.getCompany()
                    + ". Format as 3 numbered points. Keep each tip under 2 sentences.";
                model.addAttribute("autoCareer",
                    aiService.callAI(careerPrompt));
            } catch (Exception e) {
                model.addAttribute("autoBio", null);
                model.addAttribute("autoCareer", null);
            }
        }

        // Auto-generate for students
        if (profile.getRole() != null
                && profile.getRole().equals("STUDENT")
                && profile.getDepartment() != null) {
            try {
                String careerPrompt = "Give 3 short career tips for a "
                    + profile.getCurrentYear() + " student studying "
                    + profile.getDepartment()
                    + " with skills in " + profile.getSkills()
                    + ". Format as 3 numbered points. Keep each under 2 sentences.";
                model.addAttribute("autoCareer",
                    aiService.callAI(careerPrompt));
            } catch (Exception e) {
                model.addAttribute("autoCareer", null);
            }
        }

        return "profile";
    }

    // Serve Photos
    @GetMapping("/photos/{filename}")
    public ResponseEntity<Resource> servePhoto(
            @PathVariable String filename) throws Exception {
        Path filePath = Paths.get(uploadDir).resolve(filename);
        Resource resource = new UrlResource(filePath.toUri());
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(resource);
    }

    // Edit Alumni
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model,
                                org.springframework.security.core.Authentication auth) {
        Alumni target = alumniService.getAlumniById(id);
        if (!target.getEmail().equals(auth.getName())) {
            return "redirect:/profile/" + id + "?denied";
        }
        model.addAttribute("alumni", target);
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String updateAlumni(@PathVariable Long id,
                               @ModelAttribute Alumni alumni,
                               @RequestParam("photo") MultipartFile photo,
                               org.springframework.security.core.Authentication auth)
                               throws Exception {
        Alumni existing = alumniService.getAlumniById(id);
        if (!existing.getEmail().equals(auth.getName())) {
            return "redirect:/profile/" + id + "?denied";
        }
        alumni.setId(id);
        alumni.setRole(existing.getRole());
        alumniService.updateAlumni(alumni, photo);
        return "redirect:/profile/" + id;
    }

    // Job Board
    @GetMapping("/jobs")
    public String jobBoard(Model model) {
        model.addAttribute("jobList", jobPostService.getAllJobs());
        model.addAttribute("jobPost", new JobPost());
        return "jobs";
    }

    @PostMapping("/jobs")
    public String postJob(@ModelAttribute JobPost jobPost) {
        jobPostService.addJob(jobPost);
        return "redirect:/jobs";
    }

    @GetMapping("/jobs/delete/{id}")
    public String deleteJob(@PathVariable Long id) {
        jobPostService.deleteJob(id);
        return "redirect:/jobs";
    }
    @GetMapping("/post-login")
    public String postLogin(org.springframework.security.core.Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return "redirect:/admin/control";
        }
        return "redirect:/home";
    }
    @GetMapping("/my-account")
    public String myAccount(org.springframework.security.core.Authentication auth) {
        Alumni me = alumniService.getAlumniByEmail(auth.getName());
        return "redirect:/profile/" + me.getId();
    }
    
}