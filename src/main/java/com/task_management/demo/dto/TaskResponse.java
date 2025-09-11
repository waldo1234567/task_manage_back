package com.task_management.demo.dto;

import com.task_management.demo.entity.TaskStatus;

import java.time.Instant;
import java.util.UUID;

public class TaskResponse {
    private UUID id;
    private UUID projectId;
    private String title;
    private String description;
    private TaskStatus status;
    private String assigneeAuth0Id;
    private Instant dueDate;
    private int priority;
    private Instant createdAt;
    private Instant updatedAt;

    public TaskResponse() {}

    public TaskResponse(UUID id, UUID projectId, String title, String description,
                        TaskStatus status, String assigneeAuth0Id,
                        Instant dueDate, int priority, Instant createdAt, Instant updatedAt) {
        this.id = id; this.projectId = projectId; this.title = title; this.description = description;
        this.status = status; this.assigneeAuth0Id = assigneeAuth0Id; this.dueDate = dueDate;
        this.priority = priority; this.createdAt = createdAt; this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getProjectId() { return projectId; }
    public void setProjectId(UUID projectId) { this.projectId = projectId; }
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
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
