package com.task_management.demo.dto;

import com.task_management.demo.entity.TaskStatus;

import java.time.Instant;

public class UpdateTaskRequest {
    private String title;
    private String description;
    private TaskStatus status;
    private String assigneeAuth0Id;
    private Instant dueDate;
    private Integer priority;

    public UpdateTaskRequest() {}

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
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
}