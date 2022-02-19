package ru.solarlab.study.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.solarlab.study.validation.CapitalLetter;

import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Запрос на создание задачи")
public class TaskCreateDto {
    @NotBlank
    @CapitalLetter
    @Schema(description = "Наименование")
    private String name;

    @Schema(description = "Дата начала")
    private OffsetDateTime startedAt;
}
