package domain.repository;

import domain.model.Task;

import java.util.Optional;

public interface TaskRepository {

    Optional<Task> findById(Long taskId);

}
