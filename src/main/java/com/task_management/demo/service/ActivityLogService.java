package com.task_management.demo.service;

import com.task_management.demo.dto.ActivityLogResponse;
import com.task_management.demo.dto.CreateActivityLogRequest;
import com.task_management.demo.entity.ActivityLogEntity;
import com.task_management.demo.entity.TaskEntity;
import com.task_management.demo.repository.ActivityLogRepository;
import com.task_management.demo.repository.TaskRepository;
import com.task_management.demo.utils.ActivityLogMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ActivityLogService {
    private final ActivityLogRepository activityLogRepository;
    private final TaskRepository taskRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ActivityLogService(ActivityLogRepository activityLogRepository,
                              TaskRepository taskRepository,
                              SimpMessagingTemplate messagingTemplate
    ) {
        this.activityLogRepository = activityLogRepository;
        this.taskRepository = taskRepository;
        this.messagingTemplate=messagingTemplate;
    }

    @Transactional(readOnly = true)
    public List<ActivityLogResponse> listByTaskId(UUID taskId){
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Task not found"));
        List<ActivityLogEntity> logs = activityLogRepository.findByTaskIdOrderByCreatedAtDesc(taskId);
        return logs.stream().map(ActivityLogMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ActivityLogResponse> listByProjectId(UUID projectId) {
        List<ActivityLogEntity> logs = activityLogRepository.findByTaskProjectIdOrderByCreatedAtDesc(projectId);
        return logs.stream().map(ActivityLogMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public ActivityLogResponse createForTask(UUID taskId, CreateActivityLogRequest req, String actorAuth0Id) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Task not found"));

        ActivityLogEntity log = new ActivityLogEntity();
        log.setTask(task);
        log.setAction(req.getAction() != null ? req.getAction() : "custom.note");
        log.setActorAuth0Id(actorAuth0Id);
        log.setDetails(req.getDetails());
        ActivityLogEntity saved = activityLogRepository.save(log);

        if(task.getProject() != null && task.getProject().getId() != null){
            String topic = "/topic/project." + task.getProject().getId().toString();
            var payload = new java.util.HashMap<String, Object>();
            payload.put("type", "activity.created");
            payload.put("activity", ActivityLogMapper.toDto(saved));
            payload.put("actor", actorAuth0Id);
            messagingTemplate.convertAndSend(topic, payload);
        }
        return ActivityLogMapper.toDto(saved);
    }
}
