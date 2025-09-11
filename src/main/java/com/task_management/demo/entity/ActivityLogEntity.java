package com.task_management.demo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "activity_log")
public class ActivityLogEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private TaskEntity task;

    @Column(nullable = false)
    private String action; // e.g., "task.created", "task.updated", "task.assigned"

    @Column(name = "actor_auth0_id", length = 128)
    private String actorAuth0Id;

    @Column(columnDefinition = "text")
    private String details; // JSON string or plain text

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    public ActivityLogEntity() {}

    public ActivityLogEntity(TaskEntity task, String action, String actorAuth0Id, String details) {
        this.task = task;
        this.action = action;
        this.actorAuth0Id = actorAuth0Id;
        this.details = details;
    }

    // Getters & setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public TaskEntity getTask() { return task; }
    public void setTask(TaskEntity task) { this.task = task; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getActorAuth0Id() { return actorAuth0Id; }
    public void setActorAuth0Id(String actorAuth0Id) { this.actorAuth0Id = actorAuth0Id; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public Instant getCreatedAt() { return createdAt; }

}
