package ru.solarlab.study.mapper;

import org.apache.commons.lang3.RandomUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.solarlab.study.dto.Status;
import ru.solarlab.study.dto.TaskDto;
import ru.solarlab.study.dto.TaskCreateDto;
import ru.solarlab.study.dto.TaskUpdateDto;
import ru.solarlab.study.entity.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    TaskDto taskToTaskDto(Task entity);

    Task taskUpdateRequestToTaskView(TaskUpdateDto dto);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "status", ignore = true),
//            @Mapping(target="id", expression = "java(generateId())"),
//            @Mapping(target = "status", constant = "NEW")
    })
    Task toTask(TaskCreateDto dto, int id);

    @AfterMapping
    default void afterMappingFromCreate(@MappingTarget Task target, TaskCreateDto source) {
        target.setStatus(Status.NEW);
    }

    default int generateId() {
        return RandomUtils.nextInt();
    }
}
