package ru.solarlab.study.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.solarlab.study.dto.TaskCreateDto;
import ru.solarlab.study.dto.TaskDto;
import ru.solarlab.study.dto.TaskUpdateDto;
import ru.solarlab.study.entity.Task;
import ru.solarlab.study.exception.TaskNotFoundException;
import ru.solarlab.study.mapper.TaskMapper;
import ru.solarlab.study.repository.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Data
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final ObjectMapper objectMapper;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private static final int DEFAULT_PAGE_SIZE = 10;

    public List<TaskDto> getTasks(Integer limit) {
        log.info("Получаем информацию о задачах из БД");
        return taskRepository.findAll(PageRequest.of(0, limit == null ? DEFAULT_PAGE_SIZE : limit)).stream()
                .map(taskMapper::taskToTaskDto).collect(Collectors.toList());
    }

    public List<TaskDto> getByNameLike(String nameLike) {
        return taskRepository.findByNameLike(nameLike).stream()
                .map(taskMapper::taskToTaskDto).collect(Collectors.toList());
    }

    public TaskDto getById(Integer taskId) {
        log.info("Получаем информацию о задаче из БД: {}", kv("id", taskId));
        try {
            Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
            return taskMapper.taskToTaskDto(task);
        } catch (Exception ex) {
            log.error("Задача с {} не найдена", kv("id", taskId), ex);
            throw ex;
        }
    }

    @SneakyThrows
    public TaskDto update(Integer taskId, TaskUpdateDto request) {
        log.info("Обновляем информацию о задаче в БД: {}.", kv("id", taskId), kv("data", request));
        Task task = taskMapper.taskUpdateRequestToTaskView(request, taskId);
        taskRepository.save(task);
        return taskMapper.taskToTaskDto(task);
    }

    public void deleteById(Integer taskId) {
        log.info("Удаляем информацию о задаче из БД: {}", kv("id", taskId));
        taskRepository.deleteById(taskId);
    }

    @SneakyThrows
    public TaskDto create(TaskCreateDto request) {
        log.info("Сохраняем информацию о новой задаче в БД.", kv("data", request));
        Task task = taskMapper.toTask(request);
        taskRepository.save(task);
        return taskMapper.taskToTaskDto(task);
    }

    public List<TaskDto> getTasks() {
        return StreamSupport.stream(taskRepository.findAll().spliterator(), false)
                .map(taskMapper::taskToTaskDto).collect(Collectors.toList());
    }
}
