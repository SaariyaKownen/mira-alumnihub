package com.alumni.portal.repository;

import com.alumni.portal.model.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {
    List<JobPost> findAllByOrderByPostedDateDesc();
}