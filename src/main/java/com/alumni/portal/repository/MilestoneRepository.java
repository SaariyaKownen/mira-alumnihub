package com.alumni.portal.repository;

import com.alumni.portal.model.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
    List<Milestone> findAllByOrderByPostedDateDesc();
}