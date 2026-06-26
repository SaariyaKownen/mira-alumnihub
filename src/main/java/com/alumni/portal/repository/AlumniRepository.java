package com.alumni.portal.repository;

import com.alumni.portal.model.Alumni;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AlumniRepository extends JpaRepository<Alumni, Long> {

    Alumni findByEmail(String email);

    List<Alumni> findByRole(String role);

    @Query("SELECT a FROM Alumni a WHERE " +
           "(LOWER(a.fullName) LIKE LOWER(CONCAT('%',:k,'%')) OR " +
           "LOWER(a.company) LIKE LOWER(CONCAT('%',:k,'%')) OR " +
           "LOWER(a.graduationYear) LIKE LOWER(CONCAT('%',:k,'%'))) " +
           "AND a.role = 'ALUMNI'")
    List<Alumni> searchAlumni(@Param("k") String keyword);
}