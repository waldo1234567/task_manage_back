package com.task_management.demo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tasks")
public class TaskEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.TODO;

    // store Auth0 user id (sub)
    @Column(name = "assignee_auth0_id", length = 128)
    private String assigneeAuth0Id;

    @Column(name = "due_date")
    private Instant dueDate;
    @Column(nullable = false)
    private int priority = 3;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "column_id")
    private BoardColumnEntity column;

    @Column(name = "position_index")
    private Double positionIndex;
    @Version
    private Long version;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;



    // Constructors
    public TaskEntity() {}

    public TaskEntity(ProjectEntity project, String title, String description, TaskStatus status, String assigneeAuth0Id, Instant dueDate, int priority) {
        this.project = project;
        this.title = title;
        this.description = description;
        this.status = status;
        this.assigneeAuth0Id = assigneeAuth0Id;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    // Getters & setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public ProjectEntity getProject() { return project; }
    public void setProject(ProjectEntity project) { this.project = project; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
    public String getAssigneeAuth0Id() { return assigneeAuth0Id; }
    public void setAssigneeAuth0Id(String assigneeAuth0Id) { this.assigneeAuth0Id = assigneeAuth0Id; }
    public Instant getDueDate() { return dueDate; }
    public void setDueDate(Instant dueDate) { this.dueDate = dueDate; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public BoardColumnEntity getColumn() { return column; }
    public void setColumn(BoardColumnEntity column) { this.column = column; }
    public Double getPositionIndex() { return positionIndex; }
    public void setPositionIndex(Double positionIndex) { this.positionIndex = positionIndex; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
