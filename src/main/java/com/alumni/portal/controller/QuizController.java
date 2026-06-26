package com.alumni.portal.controller;

import com.alumni.portal.service.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
public class QuizController {

    @Autowired
    PointsService pointsService;

    @GetMapping("/quiz")
    public String quizPage(Model model) {
        // Rotate question set daily using day-of-year
        int dayOfYear = LocalDate.now().getDayOfYear();
        int setIndex = dayOfYear % 10; // 10 different sets
        model.addAttribute("setIndex", setIndex);
        model.addAttribute("todayDate", LocalDate.now().toString());
        return "quiz";
    }

    @PostMapping("/quiz/award")
    @ResponseBody
    public String awardQuizPoints(@RequestParam int score,
                                   Authentication auth) {
        pointsService.addPoints(auth.getName(), score * 2);
        return "ok";
    }
}