package com.task_management.demo.controller;

import com.task_management.demo.service.PresenceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PresenceController {

    private final PresenceService presenceService;

    public PresenceController(PresenceService presenceService) {
        this.presenceService = presenceService;
    }

    @GetMapping("/projects/{projectId}/presence")
    public List<Map<String,String>> getPresence(@PathVariable String projectId) {
        return presenceService.listPresence(projectId);
    }
}
