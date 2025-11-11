package application.usecase;

import application.mapper.ProjectMapper;
import domain.repository.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateProjectUseCase {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    // Colocamos las dependencias del repo y el mapper
    public CreateProjectUseCase(ProjectRepository projectRepository,
                                ProjectMapper projectMapper){
        this.projectMapper = projectMapper;
        this.projectRepository = projectRepository;
    }

}
