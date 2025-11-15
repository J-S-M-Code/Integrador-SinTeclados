package domain.repository;

import domain.model.Project;
import domain.model.Task;

import java.lang.ScopedValue;
import java.util.Optional;

public interface TaskRepository {

    boolean existByTitleAndProject(String title, Project project);
    Task save(Task task);
    Optional<Task> findById(Long id);
}
