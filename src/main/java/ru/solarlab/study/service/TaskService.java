package ru.solarlab.study.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.solarlab.study.dto.TaskCreateDto;
import ru.solarlab.study.dto.TaskDto;
import ru.solarlab.study.dto.TaskUpdateDto;
import ru.solarlab.study.entity.Task;
import ru.solarlab.study.mapper.TaskMapper;
import ru.solarlab.study.repository.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
@Data
@CacheConfig(cacheNames = "taskCache")
public class TaskService {

    private final ObjectMapper objectMapper;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private static final int DEFAULT_PAGE_SIZE = 10;

    public List<TaskDto> getTasks(Integer limit) {
        log.info("Получаю список задач из БД");
        return taskRepository.findAll(PageRequest.of(0, limit == null ? DEFAULT_PAGE_SIZE : limit)).stream()
                .map(taskMapper::taskToTaskDto).collect(Collectors.toList());
    }

    public List<TaskDto> getByNameLike(String nameLike) {
        return taskRepository.findByNameLike(nameLike).stream()
                .map(taskMapper::taskToTaskDto).collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "tasksCache", key = "#root.methodName", cacheManager = "tasksCacheManager")
    public List<TaskDto> getTasks() {
        log.info("Вызов метода getTasks");
        return StreamSupport.stream(taskRepository.findAll().spliterator(), false)
                .map(taskMapper::taskToTaskDto).collect(Collectors.toList());
    }

    @SneakyThrows
    @Cacheable(key = "#taskId", unless = "#result == null")
    public TaskDto getById(Integer taskId) {
        log.info("Вызов метода getById {}", taskId);
        Thread.sleep(5000);
        return taskMapper.taskToTaskDto(taskRepository.findById(taskId).orElse(null));
    }

    @SneakyThrows
    @CachePut(key = "#result.id")
    public TaskDto create(TaskCreateDto request) {
        log.info("Вызов метода create {}", objectMapper.writeValueAsString(request));
        Task task = taskMapper.toTask(request);
        taskRepository.save(task);
        return taskMapper.taskToTaskDto(task);
    }

    @SneakyThrows
    @CachePut(key = "#taskId")
    public TaskDto update(Integer taskId, TaskUpdateDto request) {
        log.info("Вызов метода update {}: {}", taskId, objectMapper.writeValueAsString(request));
        Task task = taskMapper.taskUpdateRequestToTaskView(request, taskId);
        taskRepository.save(task);
        return taskMapper.taskToTaskDto(task);
    }

    @Caching(evict = {
            @CacheEvict(key = "#taskId"),
            @CacheEvict(cacheNames = "tasksCache", allEntries = true)
    })
    public void deleteById(Integer taskId) {
        log.info("Вызов метода deleteById {}", taskId);
        taskRepository.deleteById(taskId);
    }
}
