package ru.solarlab.study.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.solarlab.study.dto.Status;
import ru.solarlab.study.validation.CapitalLetter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на обновление задачи")
public class TaskUpdateDto {
    @PositiveOrZero
    @Schema(description = "Идентификатор")
    private Integer id;

    @NotBlank
    @CapitalLetter
    @Schema(description = "Наименование")
    private String name;

    @Schema(description = "Дата начала")
    private OffsetDateTime startedAt;

    @NotNull
    @Schema(description = "Статус")
    private Status status;
}
