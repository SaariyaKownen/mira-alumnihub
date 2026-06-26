package com.alumni.portal.controller;

import com.alumni.portal.model.Alumni;
import com.alumni.portal.service.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class LeaderboardController {

    @Autowired
    PointsService pointsService;

    @GetMapping("/leaderboard")
    public String leaderboard(Model model) {
        List<Alumni> top = pointsService.getLeaderboard();
        model.addAttribute("leaders", top);
        model.addAttribute("pointsService", pointsService);
        return "leaderboard";
    }
}