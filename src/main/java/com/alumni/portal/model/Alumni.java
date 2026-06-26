package com.alumni.portal.model;

import jakarta.persistence.*;

@Entity
@Table(name = "alumni")
public class Alumni {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String password;
    private String graduationYear;
    private String currentJob;
    private String company;
    private String photoPath;
    private String role; // "ALUMNI" or "STUDENT"
    private String department;
    private String currentYear; // for students: 1st, 2nd, 3rd, 4th year
    private String skills;
    private String bio;
    private Integer points = 0;
    private Integer streakDays = 0;
    private String lastActiveDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getGraduationYear() { return graduationYear; }
    public void setGraduationYear(String graduationYear) { this.graduationYear = graduationYear; }

    public String getCurrentJob() { return currentJob; }
    public void setCurrentJob(String currentJob) { this.currentJob = currentJob; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getCurrentYear() { return currentYear; }
    public void setCurrentYear(String currentYear) { this.currentYear = currentYear; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }

    public Integer getStreakDays() { return streakDays; }
    public void setStreakDays(Integer streakDays) { this.streakDays = streakDays; }

    public String getLastActiveDate() { return lastActiveDate; }
    public void setLastActiveDate(String lastActiveDate) { this.lastActiveDate = lastActiveDate; }
}