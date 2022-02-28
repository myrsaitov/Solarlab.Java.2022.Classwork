package ru.solarlab.study.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.solarlab.study.dto.Status;
import ru.solarlab.study.dto.TaskCreateDto;
import ru.solarlab.study.dto.TaskDto;
import ru.solarlab.study.dto.TaskUpdateDto;
import ru.solarlab.study.entity.Task;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface TaskMapper {

    TaskDto taskToTaskDto(Task entity);

    Task taskUpdateRequestToTaskView(TaskUpdateDto dto, Integer id);

    Task toTask(TaskCreateDto dto);

    @AfterMapping
    default void afterMappingFromCreate(@MappingTarget Task target, TaskCreateDto source) {
        target.setStatus(Status.NEW);
    }
}
