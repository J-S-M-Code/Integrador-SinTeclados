package infrastructure.persistence.repository.interfaces;

import infrastructure.persistence.entities.ProjectEntity;
import infrastructure.persistence.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//import java.util.Optional;

@Repository
public interface ITaskRepository extends JpaRepository<TaskEntity, Long> {
    boolean existsByTitleAndProject(String title, ProjectEntity project);
}
