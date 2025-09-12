package com.task_management.demo.repository;

import com.task_management.demo.entity.BoardColumnEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BoardColumnRepository extends JpaRepository<BoardColumnEntity, UUID> {
    List<BoardColumnEntity> findByProjectIdOrderByPositionIndexAsc(UUID projectId);
}
