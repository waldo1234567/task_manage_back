package com.task_management.demo.service;

import com.task_management.demo.dto.CreateTaskRequest;
import com.task_management.demo.dto.TaskResponse;
import com.task_management.demo.dto.UpdateTaskRequest;
import com.task_management.demo.entity.ActivityLogEntity;
import com.task_management.demo.entity.ProjectEntity;
import com.task_management.demo.entity.TaskEntity;
import com.task_management.demo.repository.ActivityLogRepository;
import com.task_management.demo.repository.ProjectRepository;
import com.task_management.demo.repository.TaskRepository;
import com.task_management.demo.utils.TaskMapper;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final ActivityLogRepository activityLogRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public TaskService(
            TaskRepository taskRepository,
            ProjectRepository projectRepository,
            ActivityLogRepository activityLogRepository,
            SimpMessagingTemplate simpMessagingTemplate
    ){
        this.taskRepository=taskRepository;
        this.projectRepository=projectRepository;
        this.activityLogRepository=activityLogRepository;
        this.simpMessagingTemplate=simpMessagingTemplate;
    }


    @Transactional
    public TaskResponse createTask(CreateTaskRequest req, String actorAuth0Id){
        ProjectEntity project = null;
        if(req.getProjectId() != null){
            project = projectRepository.findById(req.getProjectId())
                    .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "project not found"));
        }

        TaskEntity task = new TaskEntity();
        task.setProject(project);
        task.setTitle(req.getTitle());
        task.setDescription(req.getDescription());
        if(req.getPriority() != null) task.setPriority(req.getPriority());
        task.setAssigneeAuth0Id(req.getAssigneeAuth0Id());
        task.setDueDate(req.getDueDate() != null ? req.getDueDate() : null);

        TaskEntity saved = taskRepository.save(task);

        //activity log
        ActivityLogEntity log = new ActivityLogEntity(saved, "task.created", actorAuth0Id,
                String.format("{\"title\":\"%s\",\"assignee\":\"%s\"}", saved.getTitle(), saved.getAssigneeAuth0Id()));
        activityLogRepository.save(log);

        if(saved.getProject() != null && saved.getProject().getId() != null){
            String topic = "/topic/project." + saved.getProject().getId().toString();
            var payload = new java.util.HashMap<String, Object>();
            payload.put("type", "task.created");
            payload.put("taskId", saved.getId());
            payload.put("task", TaskMapper.toDto(saved));
            payload.put("actor", actorAuth0Id);
            simpMessagingTemplate.convertAndSend(topic, payload);
        }

        return TaskMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(UUID taskId){
        TaskEntity t = taskRepository.findById(taskId).orElseThrow(()-> new ResponseStatusException(NOT_FOUND, "task not found"));
        return TaskMapper.toDto(t);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> findTaskByProject(UUID projectId){
        List<TaskEntity> tasks = taskRepository.findByProjectId(projectId, Sort.by(Sort.Direction.ASC, "createdAt"));
        return tasks.stream().map(TaskMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public TaskResponse updateTask(UUID taskId, UpdateTaskRequest req, String actorAuth0Id){
        TaskEntity t = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "task not found"));
        boolean changed = false;

        if (req.getTitle() != null && !req.getTitle().equals(t.getTitle())) {
            t.setTitle(req.getTitle());
            changed = true;
        }
        if (req.getDescription() != null && !req.getDescription().equals(t.getDescription())) {
            t.setDescription(req.getDescription());
            changed = true;
        }
        if (req.getStatus() != null && req.getStatus() != t.getStatus()) {
            t.setStatus(req.getStatus());
            changed = true;
        }
        if (req.getAssigneeAuth0Id() != null && !req.getAssigneeAuth0Id().equals(t.getAssigneeAuth0Id())) {
            t.setAssigneeAuth0Id(req.getAssigneeAuth0Id());
            changed = true;
        }
        if (req.getDueDate() != null && !req.getDueDate().equals(t.getDueDate())) {
            t.setDueDate(req.getDueDate());
            changed = true;
        }
        if (req.getPriority() != null && req.getPriority() != t.getPriority()) {
            t.setPriority(req.getPriority());
            changed = true;
        }

        TaskEntity saved = taskRepository.save(t);

        if(changed){
            ActivityLogEntity log = new ActivityLogEntity(saved, "task.updated", actorAuth0Id,
                    String.format("{\"changes\":\"updated by %s\",\"timestamp\":\"%s\"}", actorAuth0Id, Instant.now().toString()));
            activityLogRepository.save(log);

            //broadcast update
            if (saved.getProject() != null && saved.getProject().getId() != null) {
                String topic = "/topic/project." + saved.getProject().getId().toString();
                var payload = new java.util.HashMap<String, Object>();
                payload.put("type", "task.updated");
                payload.put("taskId", saved.getId());
                payload.put("task", TaskMapper.toDto(saved));
                payload.put("actor", actorAuth0Id);
                simpMessagingTemplate.convertAndSend(topic, payload);
            }
        }
        return TaskMapper.toDto(saved);
    }

}
