package com.alumni.portal.service;

import com.alumni.portal.model.Alumni;
import com.alumni.portal.repository.AlumniRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PointsService {

    @Autowired
    AlumniRepository alumniRepository;

    public void addPoints(String email, int amount) {
        Alumni a = alumniRepository.findByEmail(email);
        if (a != null) {
            int current = a.getPoints() == null ? 0 : a.getPoints();
            a.setPoints(current + amount);
            updateStreak(a);
            alumniRepository.save(a);
        }
    }

    private void updateStreak(Alumni a) {
        String today = LocalDate.now().toString();
        String last = a.getLastActiveDate();
        if (today.equals(last)) {
            return; // already counted today
        }
        if (last != null && LocalDate.parse(last).plusDays(1).toString().equals(today)) {
            a.setStreakDays((a.getStreakDays() == null ? 0 : a.getStreakDays()) + 1);
        } else {
            a.setStreakDays(1);
        }
        a.setLastActiveDate(today);
    }

    public List<Alumni> getLeaderboard() {
        return alumniRepository.findAll().stream()
            .sorted((x, y) -> {
                int px = x.getPoints() == null ? 0 : x.getPoints();
                int py = y.getPoints() == null ? 0 : y.getPoints();
                return py - px;
            })
            .limit(20)
            .collect(Collectors.toList());
    }

    public String getBadge(int points) {
        if (points >= 200) return "🏆 Legend";
        if (points >= 100) return "🥇 Champion";
        if (points >= 50) return "🥈 Achiever";
        if (points >= 20) return "🥉 Rising Star";
        return "🌱 Newcomer";
    }
}