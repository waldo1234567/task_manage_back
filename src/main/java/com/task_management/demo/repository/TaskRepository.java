package com.task_management.demo.repository;

import com.task_management.demo.entity.TaskEntity;
import com.task_management.demo.entity.TaskStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {
    List<TaskEntity>findByProjectId(UUID projectId, Sort sort);

    List<TaskEntity>findByAssigneeAuth0Id(String assigneeAuth0Id);

    List<TaskEntity>findByStatus(TaskStatus status);
}
