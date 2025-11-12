package domain.repository;

import domain.model.Project;
import domain.model.Task;

public interface TaskRepository {

    boolean existByTitleAndProject(String title, Project project);
    Task save(Task task);

}
