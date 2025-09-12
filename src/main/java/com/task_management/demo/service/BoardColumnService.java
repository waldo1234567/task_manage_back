package com.task_management.demo.service;

import com.task_management.demo.entity.BoardColumnEntity;
import com.task_management.demo.entity.ProjectEntity;
import com.task_management.demo.entity.TaskEntity;
import com.task_management.demo.repository.BoardColumnRepository;
import com.task_management.demo.repository.ProjectRepository;
import com.task_management.demo.repository.TaskRepository;
import com.task_management.demo.utils.TaskMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class BoardColumnService {
    private final BoardColumnRepository columnRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public BoardColumnService(BoardColumnRepository columnRepository,
                        ProjectRepository projectRepository,
                        TaskRepository taskRepository) {
        this.columnRepository = columnRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional
    public BoardColumnEntity createColumn(UUID projectId, String name, Integer positionIndex){
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Project not found"));

        //determine pos index if nul
        if(positionIndex == null){
            List<BoardColumnEntity> cols = columnRepository.findByProjectIdOrderByPositionIndexAsc(projectId);
            positionIndex = cols.isEmpty() ? 0: cols.get(cols.size() - 1).getPositionIndex() + 1;
        }

        BoardColumnEntity col = new BoardColumnEntity(project, name, positionIndex);
        return columnRepository.save(col);
    }

    @Transactional(readOnly = true)
    public List<BoardColumnEntity> listColumns(UUID projectId){
        return  columnRepository.findByProjectIdOrderByPositionIndexAsc(projectId);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getBoard(UUID projectId){
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Project not found"));

        List<BoardColumnEntity> cols = columnRepository.findByProjectIdOrderByPositionIndexAsc(projectId);
        List<Map<String, Object>> result = new ArrayList<>();
        for(BoardColumnEntity col : cols) {
            List<TaskEntity> tasks = taskRepository.findByColumnIdOrderByPositionIndexAsc(col.getId());
            Map<String, Object> colMap = new HashMap<>();
            colMap.put("column", col);
            colMap.put("tasks", tasks.stream().map(TaskMapper::toDto).collect(Collectors.toList()));
            result.add(colMap);
        }

        List<TaskEntity> unassigned = taskRepository.findByProjectIdOrderByPositionIndexAsc(projectId).stream()
                .filter(t -> t.getColumn() == null)
                .collect(Collectors.toList());

        if (!unassigned.isEmpty()) {
            Map<String, Object> unassignedMap = new HashMap<>();
            unassignedMap.put("column", null);
            unassignedMap.put("tasks", unassigned.stream().map(TaskMapper::toDto).collect(Collectors.toList()));
            result.add(unassignedMap);
        }
        return  result;
    }
}
