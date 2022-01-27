package ru.solarlab.study.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.solarlab.study.dto.TaskDto;
import ru.solarlab.study.service.TaskService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "TaskController", description = "The TaskController API")
public class TaskController {

    private final TaskService taskService;

    @Operation(description = "Get tasks")
    @GetMapping(
            value = "/v1/tasks",
            produces = { "application/json" }
    )
    public ResponseEntity<List<TaskDto>> getTasks(@NotNull @Parameter(description = "limit", required = true) @Valid @RequestParam(value = "limit", required = true) Integer limit) {
        return ResponseEntity.ok(taskService.getTasks(limit));
    }

    @Operation(description = "Get task")
    @GetMapping(
            value = "/v1/tasks/{taskId}",
            produces = { "application/json" }
    )
    public ResponseEntity<TaskDto> getTask(@Parameter(description = "The id of task", required = true) @PathVariable Integer taskId) {
        return ResponseEntity.ok(taskService.getById(taskId));
    }

    @Operation(description = "Update a task")
    @PutMapping(
            value = "/v1/tasks/{taskId}",
            produces = { "application/json" },
            consumes = { "application/json" }
    )
    public ResponseEntity<TaskDto> updateTask(@Parameter(description = "The id of task",required=true) @PathVariable("taskId") Integer taskId,@Parameter @Valid @RequestBody(required = false) TaskDto dto) {
        return ResponseEntity.ok(taskService.update(taskId, dto));
    }


    @Operation(description = "Delete a task")
    @DeleteMapping(
            value = "/v1/tasks/{taskId}"
    )
    public ResponseEntity<Void> deleteTask(@Parameter(description = "The id of task to delete",required=true) @PathVariable("taskId") Integer taskId) {
        taskService.deleteById(taskId);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "Create task")
    @PostMapping(
            value = "/v1/tasks",
            produces = { "application/json" },
            consumes = { "application/json" }

    )
    public ResponseEntity<TaskDto> createTask(@Parameter @Valid @RequestBody(required = false) TaskDto dto){
        return new ResponseEntity<>(taskService.create(dto), HttpStatus.CREATED);
    }




}