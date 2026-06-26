package com.alumni.portal.controller;

import com.alumni.portal.model.Alumni;
import com.alumni.portal.service.AlumniService;
import com.alumni.portal.service.JobPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AlumniService alumniService;

    @Autowired
    JobPostService jobPostService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Alumni> allAlumni = alumniService.getAlumniOnly();
        List<Alumni> allStudents = alumniService.getStudentsOnly();

        // Counts
        model.addAttribute("totalAlumni", allAlumni.size());
        model.addAttribute("totalStudents", allStudents.size());
        model.addAttribute("totalJobs",
            jobPostService.getAllJobs().size());

        // Alumni by company
        Map<String, Long> byCompany = allAlumni.stream()
            .filter(a -> a.getCompany() != null
                      && !a.getCompany().isEmpty())
            .collect(Collectors.groupingBy(
                Alumni::getCompany, Collectors.counting()));
        model.addAttribute("byCompany", byCompany);

        // Alumni by graduation year
        Map<String, Long> byYear = allAlumni.stream()
            .filter(a -> a.getGraduationYear() != null
                      && !a.getGraduationYear().isEmpty())
            .collect(Collectors.groupingBy(
                Alumni::getGraduationYear, Collectors.counting()));
        model.addAttribute("byYear", byYear);

        // Alumni by job title
        Map<String, Long> byJob = allAlumni.stream()
            .filter(a -> a.getCurrentJob() != null
                      && !a.getCurrentJob().isEmpty())
            .collect(Collectors.groupingBy(
                Alumni::getCurrentJob, Collectors.counting()));
        model.addAttribute("byJob", byJob);

        // Students by department
        Map<String, Long> byDept = allStudents.stream()
            .filter(a -> a.getDepartment() != null
                      && !a.getDepartment().isEmpty())
            .collect(Collectors.groupingBy(
                Alumni::getDepartment, Collectors.counting()));
        model.addAttribute("byDept", byDept);

        // Students by year
        Map<String, Long> byStudentYear = allStudents.stream()
            .filter(a -> a.getCurrentYear() != null
                      && !a.getCurrentYear().isEmpty())
            .collect(Collectors.groupingBy(
                Alumni::getCurrentYear, Collectors.counting()));
        model.addAttribute("byStudentYear", byStudentYear);

        // Recent alumni
        model.addAttribute("recentAlumni",
            allAlumni.stream()
                .skip(Math.max(0, allAlumni.size() - 5))
                .collect(Collectors.toList()));

        // Recent students
        model.addAttribute("recentStudents",
            allStudents.stream()
                .skip(Math.max(0, allStudents.size() - 5))
                .collect(Collectors.toList()));

        return "admin/dashboard";
    }
}