package com.task.TeamManager.Repository;

import com.task.TeamManager.model.Projects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Projects, Long> {

    // Find projects by project manager
    List<Projects> findByProjectManagerId(Long managerId);

    // Get all projects with pagination
    Page<Projects> findAll(Pageable pageable);

    // Find projects by name containing a string (case-insensitive)
    Page<Projects> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Count active projects (endDate is null or in the future)
    @Query("SELECT COUNT(p) FROM Projects p WHERE p.endDate IS NULL OR p.endDate > :now")
    long countActiveProjects(LocalDateTime now);

    long countByEndDateIsNullOrEndDateAfter(LocalDate date);

    // Find all projects ordered by creation date (recent projects first)
    List<Projects> findAllByOrderByCreatedAtDesc();
}
