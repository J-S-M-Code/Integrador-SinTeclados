package application.dto.request;

import domain.model.ProjectStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ProjectRequestDTO(
    @NotBlank(message = "Name should not be empty.")
    String name,
    @NotNull(message = "")
    LocalDate startDate,
    @NotNull(message = "La fecha de fin es requerida")
    @FutureOrPresent(message = "La fecha de fin debe ser hoy o en el futuro")
    LocalDate endDate,
    @NotNull(message = "El estado es requerido")
    ProjectStatus status,
    String description
) { }
