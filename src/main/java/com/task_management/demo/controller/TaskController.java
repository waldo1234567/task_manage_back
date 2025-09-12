package com.task_management.demo.controller;


import com.task_management.demo.dto.CreateTaskRequest;
import com.task_management.demo.dto.TaskResponse;
import com.task_management.demo.dto.UpdateTaskRequest;
import com.task_management.demo.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService=taskService;
    }

    //create task endpoint
    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateTaskRequest req
            ){
        String actorSub = jwt != null ? jwt.getSubject() : null;
        System.out.println("---------->actor sub " + actorSub);
        return taskService.createTask(req, actorSub);
    }

    //get details
    @GetMapping("/tasks/{taskId}")
    public TaskResponse getTask(@PathVariable("taskId")UUID taskId){
        return taskService.getTaskById(taskId);
    }

    //list by project
    @GetMapping("/tasks-by-project/{projectId}")
    public List<TaskResponse> getTaskByProjects(@PathVariable("projectId") UUID projectId){
        return taskService.findTaskByProject(projectId);
    }

    //update task
    @PutMapping("/tasks/{taskId}")
    public TaskResponse updateTask(@AuthenticationPrincipal Jwt jwt,
                                   @PathVariable("taskId") UUID taskId,
                                   @Valid @RequestBody UpdateTaskRequest req
                                   ){
        String actorSub = jwt != null ? jwt.getSubject(): null;
        return taskService.updateTask(taskId, req,actorSub);
    }

    @PostMapping("/tasks/{taskId}/move")
    public TaskResponse moveTask(@AuthenticationPrincipal Jwt jwt,
                                 @PathVariable("taskId") UUID taskId,
                                 @RequestBody MoveTaskRequest req) {
        String actorSub = jwt != null ? jwt.getSubject() : null;
        return taskService.moveTask(taskId, req.getToColumnId(), req.getNewPosition(), actorSub);
    }

    public static class MoveTaskRequest {
        private UUID toColumnId;
        private Double newPosition;
        public UUID getToColumnId() { return toColumnId; }
        public void setToColumnId(UUID toColumnId) { this.toColumnId = toColumnId; }
        public Double getNewPosition() { return newPosition; }
        public void setNewPosition(Double newPosition) { this.newPosition = newPosition; }
    }


}
