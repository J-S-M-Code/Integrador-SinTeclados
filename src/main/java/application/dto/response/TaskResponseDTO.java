package application.dto.response;

import domain.model.Project;
import domain.model.TaskStatus;

public record TaskResponseDTO(
        Long id,
        String title,
        Project project,
        Integer estimatedHours,
        String assignee,
        TaskStatus status

){}
