package com.task_management.demo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "board_columns")
public class BoardColumnEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    @Column(nullable = false)
    private String name; // e.g., "Ideas", "Backlog", "In Progress", "Done"

    @Column(name = "position_index")
    private Integer positionIndex; // ordering of columns (0,1,2...)

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    public BoardColumnEntity() {}

    public BoardColumnEntity(ProjectEntity project, String name, Integer positionIndex) {
        this.project = project;
        this.name = name;
        this.positionIndex = positionIndex;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public ProjectEntity getProject() { return project; }
    public void setProject(ProjectEntity project) { this.project = project; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getPositionIndex() { return positionIndex; }
    public void setPositionIndex(Integer positionIndex) { this.positionIndex = positionIndex; }
    public Instant getCreatedAt() { return createdAt; }


}
