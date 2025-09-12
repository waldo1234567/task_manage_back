package com.task_management.demo.controller;

import com.task_management.demo.entity.BoardColumnEntity;
import com.task_management.demo.service.BoardColumnService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class BoardController {
    private final BoardColumnService boardService;

    public BoardController(BoardColumnService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("/projects/{projectId}/columns")
    @ResponseStatus(HttpStatus.CREATED)
    public BoardColumnEntity createColumn(@PathVariable("projectId") UUID projectId,
                                          @Valid @RequestBody CreateColumnRequest req,
                                          @AuthenticationPrincipal Jwt jwt) {
        // you might want to check permissions (only creators or project members)
        return boardService.createColumn(projectId, req.getName(), req.getPositionIndex());
    }

    @GetMapping("/projects/{projectId}/columns")
    public List<BoardColumnEntity> listColumns(@PathVariable("projectId") UUID projectId) {
        return boardService.listColumns(projectId);
    }

    @GetMapping("/projects/{projectId}/board")
    public List<Map<String, Object>> getBoard(@PathVariable("projectId") UUID projectId) {
        return boardService.getBoard(projectId);
    }

    public static class CreateColumnRequest{
        private String name;
        private Integer positionIndex;

        public CreateColumnRequest(){}
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Integer getPositionIndex() { return positionIndex; }
        public void setPositionIndex(Integer positionIndex) { this.positionIndex = positionIndex; }
    }
}
