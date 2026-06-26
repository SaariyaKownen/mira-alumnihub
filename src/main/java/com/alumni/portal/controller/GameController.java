package com.alumni.portal.controller;

import com.alumni.portal.model.Alumni;
import com.alumni.portal.service.AlumniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class GameController {

    @Autowired
    AlumniService alumniService;

    @GetMapping("/game")
    public String memoryGame(Model model) {
        List<Alumni> alumniList = alumniService.getAlumniOnly();
        List<Alumni> studentList = alumniService.getStudentsOnly();
        alumniList.addAll(studentList);
        model.addAttribute("people", alumniList);
        return "game";
    }
}