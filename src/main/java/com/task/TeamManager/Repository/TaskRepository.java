package com.task.TeamManager.Repository;

import com.task.TeamManager.model.Task;
import com.task.TeamManager.model.Task.ETaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // Find tasks by assigned user, with pagination
    Page<Task> findByAssignedToId(Long userId, Pageable pageable);

    // Find tasks by project, with pagination
    Page<Task> findByProjectId(Long projectId, Pageable pageable);

    // Find tasks by assigned user and status, with pagination
    Page<Task> findByAssignedToIdAndStatus(Long userId, ETaskStatus status, Pageable pageable);

    // Find tasks by project and status, with pagination
    Page<Task> findByProjectIdAndStatus(Long projectId, ETaskStatus status, Pageable pageable);

    // Find tasks by due date before or on a specific date
    List<Task> findByDueDateBeforeOrDueDateEquals(LocalDateTime dueDate, LocalDateTime sameDate);

    // Get all tasks with pagination
    Page<Task> findAll(Pageable pageable);

    // Find tasks by status
    List<Task> findByStatus(ETaskStatus status);

    // Count tasks by status
    long countByStatus(ETaskStatus status);

    // Count tasks due before or on a specific date
    long countByDueDateBeforeOrDueDateEquals(LocalDateTime dueDate, LocalDateTime sameDate);

    // Find all tasks ordered by creation date (recent tasks)
    List<Task> findAllByOrderByCreatedAtDesc();
}
