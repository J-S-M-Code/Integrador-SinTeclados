package domain.repository;

import domain.model.Project;
import jakarta.validation.constraints.NotBlank;

public interface ProjectRepository {
    boolean existsByName(@NotBlank(message = "El nombre es requerido") String name);

    Project save(Project newProject);
}
