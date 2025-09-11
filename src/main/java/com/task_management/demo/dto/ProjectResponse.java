package com.task_management.demo.dto;

import java.time.Instant;
import java.util.UUID;

public class ProjectResponse {
    private UUID id;
    private String name;
    private String description;

    private Instant created_at;

    public ProjectResponse(){}

    public ProjectResponse(UUID id, String name, String description, Instant created_at){
        this.id = id; this.name = name; this.description = description; this.created_at = created_at;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Instant getCreatedAt() { return created_at; }
    public void setCreatedAt(Instant created_at) { this.created_at = created_at; }
}
