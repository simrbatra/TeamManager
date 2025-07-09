package com.task.TeamManager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private LocalDateTime dueDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "assigned_to_id")
    private Long assignedToId;

    public enum ETaskStatus {
        TO_DO,
        IN_PROGRESS,
        DONE,
        BLOCKED
    }

    public enum ETaskPriority {
        LOW,
        MEDIUM,
        HIGH,
        URGENT
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ETaskStatus status = ETaskStatus.TO_DO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ETaskPriority priority = ETaskPriority.MEDIUM;
}
