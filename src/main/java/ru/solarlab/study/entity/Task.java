package ru.solarlab.study.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.solarlab.study.dto.Status;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private Integer id;
    private String name;
    private OffsetDateTime startedAt;
    private Status status;
}
