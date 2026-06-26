package com.alumni.portal.service;

import com.alumni.portal.model.Alumni;
import com.alumni.portal.repository.AlumniRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class AlumniService {

    @Autowired
    AlumniRepository alumniRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public void register(Alumni alumni, MultipartFile photo) throws IOException {
        alumni.setPassword(passwordEncoder.encode(alumni.getPassword()));
        if (photo != null && !photo.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + photo.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Files.copy(photo.getInputStream(),
                      uploadPath.resolve(filename),
                      StandardCopyOption.REPLACE_EXISTING);
            alumni.setPhotoPath(filename);
        }
        alumniRepository.save(alumni);
    }

    public void updateAlumni(Alumni alumni, MultipartFile photo) throws IOException {
        Alumni existing = alumniRepository.findById(alumni.getId()).orElse(null);
        if (existing != null) {
            if (photo != null && !photo.isEmpty()) {
                String filename = UUID.randomUUID() + "_" + photo.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Files.copy(photo.getInputStream(),
                          uploadPath.resolve(filename),
                          StandardCopyOption.REPLACE_EXISTING);
                alumni.setPhotoPath(filename);
            } else {
                alumni.setPhotoPath(existing.getPhotoPath());
            }
            alumni.setPassword(existing.getPassword());
        }
        alumniRepository.save(alumni);
    }

    public List<Alumni> getAllAlumni() {
        return alumniRepository.findAll();
    }

    public List<Alumni> searchAlumni(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return alumniRepository.findAll();
        }
        return alumniRepository.searchAlumni(keyword);
    }

    public Alumni getAlumniById(Long id) {
        return alumniRepository.findById(id).orElse(null);
    }

    public void deleteAlumni(Long id) {
        alumniRepository.deleteById(id);
    }
    public Alumni getAlumniByEmail(String email) {
        return alumniRepository.findByEmail(email);
    }
    public List<Alumni> getAlumniOnly() {
        return alumniRepository.findByRole("ALUMNI");
    }

    public List<Alumni> getStudentsOnly() {
        return alumniRepository.findByRole("STUDENT");
    }
}