package ru.solarlab.study.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.solarlab.study.dto.Status;
import ru.solarlab.study.dto.TaskCreateDto;
import ru.solarlab.study.dto.TaskDto;
import ru.solarlab.study.dto.TaskUpdateDto;
import ru.solarlab.study.entity.Task;
import ru.solarlab.study.mapper.TaskMapper;
import ru.solarlab.study.mapper.TaskMapperImpl;
import ru.solarlab.study.repository.TaskRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    private static final Integer LIMIT = 20;
    private static final Integer DEFAULT_LIMIT = 10;
    public static final int TASK_ID = 1;
    public static final String TASK_NAME = "task";
    public static final OffsetDateTime TASK_STARTED_AT = OffsetDateTime.now();
    public static final Status TASK_STATUS = Status.NEW;

    @Mock
    private TaskRepository taskRepository;

    @Spy
    private TaskMapper taskMapper = new TaskMapperImpl();

    @InjectMocks
    private TaskService taskService;

    @Test
    void testGetTasksWithLimit() {
        Mockito.when(taskRepository.findAll(PageRequest.of(0, LIMIT))).thenReturn(getPageWithTasks());

        final List<TaskDto> actual = taskService.getTasks(LIMIT);

        final TaskDto taskDto = getTaskDto(false);
        assertEquals(List.of(taskDto), actual);
    }

    @Test
    void testGetTasksWithoutLimit() {
        Mockito.when(taskRepository.findAll(PageRequest.of(0, DEFAULT_LIMIT))).thenReturn(getPageWithTasks());

        final List<TaskDto> actual = taskService.getTasks(null);

        final TaskDto taskDto = getTaskDto(false);
        assertEquals(List.of(taskDto), actual);
    }

    @Test
    void testGetByNameLike() {
        final String nameForLike = "name for like";
        Mockito.when(taskRepository.findByNameLike(nameForLike)).thenReturn(List.of(getTask(false)));

        final List<TaskDto> actual = taskService.getByNameLike(nameForLike);

        final TaskDto taskDto = getTaskDto(false);
        assertEquals(List.of(taskDto), actual);
    }

    @Test
    void testGetById() {
        Mockito.when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(getTask(false)));

        final TaskDto actual = taskService.getById(TASK_ID);

        final TaskDto taskDto = getTaskDto(false);
        assertEquals(taskDto, actual);
    }

    @Test
    void testUpdate() {
        final TaskDto actual = taskService.update(TASK_ID, getTaskUpdateDto());

        final TaskDto taskDto = getTaskDto(false);
        assertEquals(taskDto, actual);
        Mockito.verify(taskRepository).save(getTask(false));
    }

    @Test
    void testDeleteById() {
        taskService.deleteById(TASK_ID);
        Mockito.verify(taskRepository).deleteById(TASK_ID);
    }

    @Test
    void testCreate() {
        final TaskDto actual = taskService.create(getTaskCreateDto());

        final TaskDto taskDto = getTaskDto(true);
        assertEquals(taskDto, actual);
        Mockito.verify(taskRepository).save(getTask(true));
    }

    @Test
    void testGetAllTasks() {
        Mockito.when(taskRepository.findAll()).thenReturn(getPageWithTasks());

        final List<TaskDto> actual = taskService.getTasks();

        final TaskDto taskDto = getTaskDto(false);
        assertEquals(List.of(taskDto), actual);
    }

    private Page<Task> getPageWithTasks() {
        return new PageImpl<>(List.of(getTask(false)));
    }

    private Task getTask(boolean nullId) {
        return new Task(nullId ? null : TASK_ID, TASK_NAME, TASK_STARTED_AT, TASK_STATUS);
    }

    private TaskDto getTaskDto(boolean nullId) {
        return TaskDto.builder()
                .id(nullId ? null : TASK_ID)
                .name(TASK_NAME)
                .startedAt(TASK_STARTED_AT)
                .status(TASK_STATUS)
                .build();
    }

    private TaskUpdateDto getTaskUpdateDto() {
        return TaskUpdateDto.builder()
                .name(TASK_NAME)
                .startedAt(TASK_STARTED_AT)
                .status(TASK_STATUS)
                .build();
    }

    private TaskCreateDto getTaskCreateDto() {
        return TaskCreateDto.builder()
                .name(TASK_NAME)
                .startedAt(TASK_STARTED_AT)
                .build();
    }
}