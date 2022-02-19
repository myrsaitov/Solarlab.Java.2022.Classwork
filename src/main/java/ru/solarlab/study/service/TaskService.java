package ru.solarlab.study.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.solarlab.study.dto.Status;
import ru.solarlab.study.dto.TaskDto;
import ru.solarlab.study.entity.Task;
import ru.solarlab.study.mapper.TaskMapper;
import ru.solarlab.study.dto.TaskCreateDto;
import ru.solarlab.study.dto.TaskUpdateDto;

import javax.validation.ValidationException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Data
public class TaskService {

    private final TaskMapper taskMapper;

    public List<TaskDto> getTasks(Integer limit) {
        List<Task> list = getTasks();
        return list.stream()
                .limit(limit == null ? Integer.MAX_VALUE : limit)
                .map(taskMapper::taskToTaskDto)
                .collect(Collectors.toList());
    }

    public TaskDto getById(Integer taskId) {
        return taskMapper.taskToTaskDto(Task.builder()
                .id(taskId)
                .name("JustDoIt")
                .startedAt(OffsetDateTime.now())
                .status(Status.NEW)
                .build());
    }

    public TaskDto update(Integer taskId, TaskUpdateDto request) {
        if (!taskId.equals(request.getId())) throw new ValidationException("ID is not valid");

        Task task = taskMapper.taskUpdateRequestToTaskView(request);
        //update data in data storage by taskId

        return taskMapper.taskToTaskDto(task);
    }

    public void deleteById(Integer taskId) {
        //delete task from data storage by taskId
    }

    public TaskDto create(TaskCreateDto request) {
        Task task = taskMapper.toTask(request, RandomUtils.nextInt());
        //create task in data storage

        return taskMapper.taskToTaskDto(task);
    }

    private List<Task> getTasks() {
        return List.of(
                Task.builder()
                        .id(RandomUtils.nextInt())
                        .name("JustDoIt")
                        .startedAt(OffsetDateTime.now())
                        .status(Status.NEW)
                        .build(),
                Task.builder()
                        .id(RandomUtils.nextInt())
                        .name("JustDoIt2")
                        .startedAt(OffsetDateTime.now())
                        .status(Status.IN_PROGRESS)
                        .build());
    }
}
