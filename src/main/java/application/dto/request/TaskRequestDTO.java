package application.dto.request;

import domain.model.Project;
import domain.model.Task;
import domain.model.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TaskRequestDTO(
        Long id,
        @NotBlank(message = "El Titulo es obligatorio")
        String title,
        Project project,
        @NotNull(message = "El/La cesionario/a debe ser obligatorio")
        @NotNull(message = "Las horas estimadas son requeridas")
        @Min(value = 1, message = "Las horas estimadas deben ser mayor a 0")
        Integer estimatedHours,
        String assignee,
        @NotNull(message = "El estado es requerido")
        TaskStatus status,
        @NotNull(message = "La fecha de fin es requerida")
        @FutureOrPresent(message = "La fecha de fin debe ser hoy o en el futuro")
        LocalDateTime finishedAt,
        LocalDateTime createdAt

) {}
