package com.task_management.demo.repository;

import com.task_management.demo.entity.ActivityLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ActivityLogRepository extends JpaRepository<ActivityLogEntity, UUID> {
    List<ActivityLogEntity> findByTaskIdOrderByCreatedAtDesc(UUID taskId);

    @Query("SELECT a FROM ActivityLogEntity a WHERE a.task.project.id = :projectId ORDER BY a.createdAt DESC")
    List<ActivityLogEntity> findByTaskProjectIdOrderByCreatedAtDesc(UUID projectId);

}
