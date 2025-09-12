package com.task_management.demo.controller;

import com.task_management.demo.dto.CreateProjectRequest;
import com.task_management.demo.dto.ProjectResponse;
import com.task_management.demo.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    //create new project (requires auth)
    @PostMapping("/projects")
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse createProject(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateProjectRequest req
    ){
        String actorSub = jwt != null ? jwt.getSubject() : null;
        return projectService.createProject(req, actorSub);
    }

    //list all project
    @GetMapping("/projects")
    public List<ProjectResponse> listProjects(){
        return projectService.listProjects();
    }

    //find project by id
    @GetMapping("/projects/{projectId}")
    public ProjectResponse findById(
            @PathVariable("projectId") UUID projectId
    ){
        return projectService.getProjectById(projectId);
    }

    @GetMapping("/me/projects")
    public List<ProjectResponse> listMyProjects(@AuthenticationPrincipal Jwt jwt) {
        String auth0Id = jwt != null ? jwt.getSubject() : null;
        return projectService.listProjectsByCreator(auth0Id);
    }
}
