package ru.solarlab.study.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.solarlab.study.dto.TaskCreateDto;
import ru.solarlab.study.dto.TaskDto;
import ru.solarlab.study.dto.TaskUpdateDto;
import ru.solarlab.study.dto.UserDto;
import ru.solarlab.study.entity.Task;
import ru.solarlab.study.exception.CannotEditOtherTasksException;
import ru.solarlab.study.exception.UserCannotDeleteTasksException;
import ru.solarlab.study.mapper.TaskMapper;
import ru.solarlab.study.repository.TaskRepository;
import ru.solarlab.study.repository.TaskRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Data
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private static final int DEFAULT_PAGE_SIZE = 10;

    public List<TaskDto> getTasks(Integer limit) {
        return taskRepository.findAll(PageRequest.of(0, limit == null ? DEFAULT_PAGE_SIZE : limit)).stream()
                .map(taskMapper::taskToTaskDto).collect(Collectors.toList());
    }

    public List<TaskDto> getByNameLike(String nameLike) {
        return taskRepository.findByNameLike(nameLike).stream()
                .map(taskMapper::taskToTaskDto).collect(Collectors.toList());
    }

    public TaskDto getById(Integer taskId) {
        return taskMapper.taskToTaskDto(taskRepository.findById(taskId).orElse(null));
    }

    public TaskDto update(Integer taskId, TaskUpdateDto request) {
        checkCurrentUserUpdatePermission(taskId);
        Task task = taskMapper.taskUpdateRequestToTaskView(request, taskId, getCurrentUser().getUsername());
        taskRepository.save(task);
        return taskMapper.taskToTaskDto(task);
    }

    public void deleteById(Integer taskId) {
        taskRepository.deleteById(taskId);
    }

    public TaskDto create(TaskCreateDto request) {
        Task task = taskMapper.toTask(request, getCurrentUser().getUsername());
        taskRepository.save(task);
        return taskMapper.taskToTaskDto(task);
    }

    public List<TaskDto> getTasks() {
        return StreamSupport.stream(taskRepository.findAll().spliterator(), false)
                .map(taskMapper::taskToTaskDto).collect(Collectors.toList());
    }

    private void checkCurrentUserUpdatePermission(Integer taskId) {
        UserDto currentUser = getCurrentUser();

        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent() && !optionalTask.get().getOwner().equals(currentUser.getUsername()) && !currentUser.getRole().equals("ADMIN")) {
            throw new CannotEditOtherTasksException();
        }

    }

    private UserDto getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String username = securityContext.getAuthentication().getPrincipal().toString();
        String role = securityContext.getAuthentication().getAuthorities().stream().findAny().get().getAuthority();
        return new UserDto(username, role);
    }
}
