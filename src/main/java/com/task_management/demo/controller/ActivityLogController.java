package com.task_management.demo.controller;

import com.task_management.demo.dto.ActivityLogResponse;
import com.task_management.demo.dto.CreateActivityLogRequest;
import com.task_management.demo.service.ActivityLogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ActivityLogController {
    private final ActivityLogService activityLogService;

    public ActivityLogController(ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @GetMapping("/tasks/{taskId}/activity")
    public List<ActivityLogResponse> getTaskActivity(@PathVariable("taskId") UUID taskId) {
        return activityLogService.listByTaskId(taskId);
    }

    @GetMapping("/projects/{projectId}/activity")
    public List<ActivityLogResponse> getProjectActivity(@PathVariable("projectId") UUID projectId) {
        return activityLogService.listByProjectId(projectId);
    }

    @PostMapping("/tasks/{taskId}/activity")
    @ResponseStatus(HttpStatus.CREATED)
    public ActivityLogResponse createTaskActivity(@AuthenticationPrincipal Jwt jwt,
                                                  @PathVariable("taskId") UUID taskId,
                                                  @Valid @RequestBody CreateActivityLogRequest req) {
        String actor = jwt != null ? jwt.getSubject() : null;
        return activityLogService.createForTask(taskId, req, actor);
    }
}
