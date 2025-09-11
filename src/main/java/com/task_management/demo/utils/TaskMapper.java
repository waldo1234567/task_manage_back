package com.task_management.demo.utils;

import com.task_management.demo.dto.TaskResponse;
import com.task_management.demo.entity.TaskEntity;

public final class TaskMapper {
    private TaskMapper(){}

    public static TaskResponse toDto(TaskEntity task){
        if (task == null) return null;
        TaskResponse resp = new TaskResponse();
        resp.setId(task.getId());
        resp.setProjectId(task.getProject() != null ? task.getProject().getId() : null);
        resp.setTitle(task.getTitle());
        resp.setDescription(task.getDescription());
        resp.setStatus(task.getStatus());
        resp.setAssigneeAuth0Id(task.getAssigneeAuth0Id());
        resp.setDueDate(task.getDueDate());
        resp.setPriority(task.getPriority());
        resp.setCreatedAt(task.getCreatedAt());
        resp.setUpdatedAt(task.getUpdatedAt());
        return resp;
    }
}
