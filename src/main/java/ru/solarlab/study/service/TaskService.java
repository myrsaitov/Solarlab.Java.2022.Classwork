package ru.solarlab.study.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.solarlab.study.dto.Status;
import ru.solarlab.study.dto.TaskDto;

import javax.validation.ValidationException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Data
public class TaskService {

    public List<TaskDto> getTasks(Integer limit) {
        List<TaskDto> list = getTasks();
        return list.stream()
                        .limit(limit == null ? Integer.MAX_VALUE : limit)
                        .collect(Collectors.toList());
    }

    public TaskDto getById(Integer taskId) {
        return TaskDto.builder()
                .id(taskId)
                .name("justDoIt")
                .startedAt(OffsetDateTime.now())
                .status(Status.NEW)
                .build();
    }

    public TaskDto update(Integer taskId, TaskDto dto) {
        //update data in data storage by taskId
        if (!taskId.equals(dto.getId())) throw new ValidationException("ID is not valid");
        return dto;
    }

    private List<TaskDto> getTasks() {
        return List.of(
                TaskDto.builder()
                        .id(RandomUtils.nextInt())
                        .name("justDoIt")
                        .startedAt(OffsetDateTime.now())
                        .status(Status.NEW)
                        .build(),
                TaskDto.builder()
                        .id(RandomUtils.nextInt())
                        .name("justDoIt2")
                        .startedAt(OffsetDateTime.now())
                        .status(Status.IN_PROGRESS)
                        .build());
    }

    public void deleteById(Integer taskId) {
        //delete task from data storage by taskId
    }

    public TaskDto create(TaskDto dto) {
        dto.setId(RandomUtils.nextInt());
        return dto;
    }
}
