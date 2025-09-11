package com.task_management.demo.dto;

import java.time.Instant;
import java.util.UUID;

public class CreateTaskRequest {
    private UUID projectId;
    private String title;
    private String description;
    private String assigneeAuth0Id;
    private Instant dueDate;
    private Integer priority;

    public CreateTaskRequest(){}

    public UUID getProjectId() { return projectId; }
    public void setProjectId(UUID projectId) { this.projectId = projectId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getAssigneeAuth0Id() { return assigneeAuth0Id; }
    public void setAssigneeAuth0Id(String assigneeAuth0Id) { this.assigneeAuth0Id = assigneeAuth0Id; }
    public Instant getDueDate() { return dueDate; }
    public void setDueDate(Instant dueDate) { this.dueDate = dueDate; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
}
