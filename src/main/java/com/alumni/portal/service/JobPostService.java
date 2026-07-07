package com.alumni.portal.service;

import com.alumni.portal.model.JobPost;
import com.alumni.portal.repository.JobPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class JobPostService {

    @Autowired
    JobPostRepository jobPostRepository;

    @Autowired
    PointsService pointsService;

    public void addJob(JobPost jobPost) {
        jobPost.setPostedDate(LocalDate.now());
        jobPostRepository.save(jobPost);
        pointsService.addPoints(jobPost.getPostedBy(), 10);
    }

    public List<JobPost> getAllJobs() {
        return jobPostRepository.findAllByOrderByPostedDateDesc();
    }

    public void deleteJob(Long id) {
        jobPostRepository.deleteById(id);
 
   }
    public com.alumni.portal.model.JobPost getJobById(Long id) {
        return jobPostRepository.findById(id).orElse(null);
    }
}