package com.alumni.portal.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "milestones")
public class Milestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String posterEmail;
    private String posterName;
    private String title;
    private String description;
    private LocalDate postedDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPosterEmail() { return posterEmail; }
    public void setPosterEmail(String posterEmail) { this.posterEmail = posterEmail; }

    public String getPosterName() { return posterName; }
    public void setPosterName(String posterName) { this.posterName = posterName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getPostedDate() { return postedDate; }
    public void setPostedDate(LocalDate postedDate) { this.postedDate = postedDate; }
}