package application.dto.response;

import domain.model.Project;
import domain.model.TaskStatus;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public record TaskResponseDTO(
        Long id,
        String title,
        Project project,
        Integer estimatedHours,
        String assignee,
        TaskStatus status,
        LocalDateTime createdAt,
        LocalDateTime finishedAt

){}
