package ru.solarlab.study.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import ru.solarlab.study.dto.Status;
import ru.solarlab.study.dto.TaskCreateDto;
import ru.solarlab.study.dto.TaskDto;
import ru.solarlab.study.dto.TaskUpdateDto;
import ru.solarlab.study.errors.ValidationErrorResponse;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TaskControllerTest extends BaseIT {
    private static final TaskDto MIGRATION_TASK_DTO = getTaskDto(1, "FromMigrationTask", null, Status.NEW);
    public static final String STARTED_AT_FIELD = "startedAt";
    public static final String TASKS_URL = "v1/tasks/";
    public static final String TASKS_BY_NAME_URL = "v1/tasks-by-name-like/";

    @Test
    void getTaskByIdSuccess() {
        TaskDto response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(TASKS_URL + MIGRATION_TASK_DTO.getId()).then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(TaskDto.class);

        assertThat(response)
                .usingRecursiveComparison()
                .ignoringFields(STARTED_AT_FIELD)
                .isEqualTo(MIGRATION_TASK_DTO);
    }

    @Test
    void getTaskByNameLikeSuccess() {
        List<TaskDto> response = RestAssured.given()
                .contentType(ContentType.JSON)
                .param("nameLike", "Migration")
                .when().get(TASKS_BY_NAME_URL).then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .extract().jsonPath().getList(".", TaskDto.class);

        assertThat(response)
                .usingRecursiveComparison()
                .ignoringFields(STARTED_AT_FIELD)
                .ignoringCollectionOrder()
                .isEqualTo(List.of(MIGRATION_TASK_DTO));
    }

    @Test
    void getTaskByNameLikeNoItems() {
        List<TaskDto> response = RestAssured.given()
                .contentType(ContentType.JSON)
                .param("nameLike", "not found")
                .when().get(TASKS_BY_NAME_URL).then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .extract().jsonPath().getList(".", TaskDto.class);

        assertThat(response).isEmpty();
    }

    @Test
    void getTasksSuccess() {
        List<TaskDto> response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(TASKS_URL).then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .extract().jsonPath().getList(".", TaskDto.class);

        assertThat(response)
                .usingRecursiveComparison()
                .ignoringFields(STARTED_AT_FIELD)
                .ignoringCollectionOrder()
                .isEqualTo(List.of(MIGRATION_TASK_DTO));
    }

    @Test
    void createTaskSuccess() {
        final TaskCreateDto createTaskDto = getCreateTaskDto("Task for create");

        TaskDto response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createTaskDto)
                .when().post(TASKS_URL).then()
                .assertThat().statusCode(HttpStatus.SC_CREATED)
                .extract().as(TaskDto.class);

        assertThat(response)
                .usingRecursiveComparison()
                .ignoringFields(STARTED_AT_FIELD)
                .ignoringCollectionOrder()
                .isEqualTo(getTaskDto(response.getId(), createTaskDto.getName(), createTaskDto.getStartedAt(), Status.NEW));
    }

    @Test
    void createTaskInvalidName() {
        final TaskCreateDto createTaskDto = getCreateTaskDto("invalid task name");

        ValidationErrorResponse response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createTaskDto)
                .when().post(TASKS_URL).then()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().as(ValidationErrorResponse.class);

        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(getValidationErrorResponse("name: Должно быть с заглавной буквы"));
    }

    @Test
    void updateTaskSuccess() {
        final TaskCreateDto createTaskDto = getCreateTaskDto("Task for update");

        TaskDto newTask = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createTaskDto)
                .when().post(TASKS_URL).then()
                .assertThat().statusCode(HttpStatus.SC_CREATED)
                .extract().as(TaskDto.class);

        final TaskUpdateDto updateTaskDto = getTaskUpdateDto();
        TaskDto updatedTask = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updateTaskDto)
                .when().put(TASKS_URL + newTask.getId()).then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(TaskDto.class);

        assertThat(updatedTask)
                .usingRecursiveComparison()
                .ignoringFields(STARTED_AT_FIELD)
                .ignoringCollectionOrder()
                .isEqualTo(getTaskDto(newTask.getId(), updateTaskDto.getName(), updateTaskDto.getStartedAt(), updateTaskDto.getStatus()));
    }

    @Test
    void deleteTaskSuccess() {
        final TaskCreateDto createTaskDto = getCreateTaskDto("Task for delete");

        TaskDto newTask = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createTaskDto)
                .when().post(TASKS_URL).then()
                .assertThat().statusCode(HttpStatus.SC_CREATED)
                .extract().as(TaskDto.class);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().delete(TASKS_URL + newTask.getId()).then()
                .assertThat().statusCode(HttpStatus.SC_NO_CONTENT);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(TASKS_URL + newTask.getId()).then()
                .assertThat().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    private static TaskCreateDto getCreateTaskDto(String name) {
        return TaskCreateDto.builder()
                .name(name)
                .startedAt(OffsetDateTime.now())
                .build();
    }

    private static TaskUpdateDto getTaskUpdateDto() {
        return TaskUpdateDto.builder()
                .name("Updated Test task")
                .startedAt(OffsetDateTime.now())
                .status(Status.COMPLETED)
                .build();
    }

    private static TaskDto getTaskDto(Integer id, String name, OffsetDateTime startedAt, Status status) {
        return TaskDto.builder()
                .id(id)
                .name(name)
                .startedAt(startedAt)
                .status(status)
                .build();
    }

    private static ValidationErrorResponse getValidationErrorResponse(String error) {
        return new ValidationErrorResponse(List.of(error));
    }
}