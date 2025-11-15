package infrastructure.persistence.repository.implementations;

import domain.model.Project;
import domain.model.Task;
import domain.repository.TaskRepository;
import infrastructure.persistence.entities.TaskEntity;
import infrastructure.persistence.mapper.PersistenceMapper;
import infrastructure.persistence.repository.interfaces.ITaskRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TaskRepositoryImp implements TaskRepository {

    private final ITaskRepository jpaRepository;
    private final PersistenceMapper mapper;

    public TaskRepositoryImp(ITaskRepository jpaRepository, PersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public boolean existByTitleAndProject(String title, Project project) {
        return jpaRepository.existsByTitleAndProject(title, mapper.toEntity(project));
    }

    @Override
    public Task save(Task task) {
        TaskEntity entityToSave = mapper.toEntity(task);
        TaskEntity savedEntity = jpaRepository.save(entityToSave);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Task> findById(Long id) {
        Optional<TaskEntity> optionalEntity = jpaRepository.findById(id);
        return optionalEntity.map(mapper::toDomain);
    }
}
