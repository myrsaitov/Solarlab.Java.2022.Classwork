package ru.solarlab.study.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.solarlab.study.dto.TaskDto;
import ru.solarlab.study.dto.TaskCreateDto;
import ru.solarlab.study.dto.TaskUpdateDto;
import ru.solarlab.study.service.TaskService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "TaskController", description = "API контролера по работе с задачами")
@Validated
public class TaskController {

    private final TaskService taskService;

    @Operation(description = "Получение списка задач")
    @GetMapping(
            value = "/v1/tasks",
            produces = {"application/json"}
    )
    public ResponseEntity<List<TaskDto>> getTasks(
            @Parameter(description = "Количество элементов на странице")
            @Min(0) @Max(20) @RequestParam(value = "limit", required = false) Integer limit) {
        return ResponseEntity.ok(taskService.getTasks(limit));
    }

    @Operation(description = "Получение списка задач по имени")
    @GetMapping(
            value = "/v1/tasks-by-name-like",
            produces = {"application/json"}
    )
    public ResponseEntity<List<TaskDto>> getTasksByNameLike(
            @Parameter(description = "Строка поиска")
            @RequestParam(value = "nameLike") String nameLike) {
        return ResponseEntity.ok(taskService.getByNameLike('%'+ nameLike +'%'));
    }

    @Operation(description = "Получение задачи")
    @GetMapping(
            value = "/v1/tasks/{taskId}",
            produces = {"application/json"}
    )
    public ResponseEntity<TaskDto> getTask(
            @Parameter(description = "Идентификатор задачи", required = true)
            @PositiveOrZero @PathVariable("taskId") int taskId) {
        return ResponseEntity.ok(taskService.getById(taskId));
    }

    @Operation(description = "Обновление задачи")
    @PutMapping(
            value = "/v1/tasks/{taskId}",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public ResponseEntity<TaskDto> updateTask(
            @Parameter(description = "Идентификатор задачи", required = true)
            @PositiveOrZero @PathVariable("taskId") int taskId,
            @Parameter(description = "Запрос на обновление задачи")
            @Valid @RequestBody(required = false) TaskUpdateDto request) {
        return ResponseEntity.ok(taskService.update(taskId, request));
    }


    @Operation(description = "Удаление задачи")
    @DeleteMapping(
            value = "/v1/tasks/{taskId}"
    )
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "Идентификатор задачи для удаления", required = true)
            @PositiveOrZero @PathVariable("taskId") int taskId) {
        taskService.deleteById(taskId);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "Создание задачи")
    @PostMapping(
            value = "/v1/tasks",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public ResponseEntity<TaskDto> createTask(
            @Parameter(description = "Запрос на создание задачи")
            @Valid @RequestBody TaskCreateDto request) {
        return new ResponseEntity<>(taskService.create(request), HttpStatus.CREATED);
    }

}