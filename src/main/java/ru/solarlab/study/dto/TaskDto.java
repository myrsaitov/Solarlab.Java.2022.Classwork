package ru.solarlab.study.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Транспортный объект задачи")
public class TaskDto {
    @Schema(description = "Идентификатор")
    private Integer id;

    @Schema(description = "Наименование")
    private String name;

    @Schema(description = "Владелец")
    private String owner;

    @Schema(description = "Дата начала")
    private OffsetDateTime startedAt;

    @Schema(description = "Статус")
    private Status status;
}
