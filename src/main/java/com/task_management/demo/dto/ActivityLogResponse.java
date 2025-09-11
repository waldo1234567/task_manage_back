package com.task_management.demo.dto;

import org.hibernate.validator.constraints.UUID;

import java.time.Instant;

public class ActivityLogResponse {
    private UUID id;
    private UUID taskId;
    private String action;
    private String actorAuth0Id;
    private String details;
    private Instant createdAt;

    public ActivityLogResponse() {}

    // getters & setters...
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTaskId() { return taskId; }
    public void setTaskId(UUID taskId) { this.taskId = taskId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getActorAuth0Id() { return actorAuth0Id; }
    public void setActorAuth0Id(String actorAuth0Id) { this.actorAuth0Id = actorAuth0Id; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
