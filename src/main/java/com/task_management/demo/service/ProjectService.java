package com.task_management.demo.service;

import com.task_management.demo.dto.CreateProjectRequest;
import com.task_management.demo.dto.ProjectResponse;
import com.task_management.demo.entity.ProjectEntity;
import com.task_management.demo.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }

    @Transactional
    public ProjectResponse createProject(CreateProjectRequest req, String actorAuth0Id){
        ProjectEntity project = new ProjectEntity();
        project.setName(req.getName());
        project.setDescription(req.getDescription());
        project.setCreatedByAuth0Id(actorAuth0Id);
        ProjectEntity saved = projectRepository.save(project);
        return toProjectDto(saved);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> listProjects(){
        List<ProjectEntity> projects = projectRepository.findAll();
        return projects.stream().map(this::toProjectDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(UUID projectId){
        ProjectEntity p = projectRepository.findById(projectId)
                .orElseThrow(()-> new ResponseStatusException(NOT_FOUND, "project not found"));
        return toProjectDto(p);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> listProjectsByCreator(String auth0Id) {
        List<ProjectEntity> projects = projectRepository.findByCreatedByAuth0Id(auth0Id);
        return projects.stream()
                .map(this::toProjectDto)
                .collect(Collectors.toList());
    }
    private ProjectResponse toProjectDto(ProjectEntity project){
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreatedAt(),
                project.getCreatedByAuth0Id()
        );
    }
}
