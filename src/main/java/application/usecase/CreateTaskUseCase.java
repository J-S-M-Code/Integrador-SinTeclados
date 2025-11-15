package application.usecase;

import application.dto.request.ProjectRequestDTO;
import application.dto.request.TaskRequestDTO;
import application.dto.response.TaskResponseDTO;
import application.mapper.TaskMapper;
import domain.model.ProjectStatus;
import domain.model.Task;
import domain.repository.ProjectRepository;
import domain.repository.TaskRepository;
import infrastructure.exception.BusinessRuleViolationsException;
import infrastructure.exception.DuplicateResourceException;
import infrastructure.exception.ResourceNotFoundException;

public class CreateTaskUseCase {
    /**
     * El caso de uso hace:
     * 1. Busca y valida que el Proyecto exista y no esté CERRADO.
     * 2. Valida que el título no esté duplicado en ese proyecto.
     * 3. Mapea el DTO a dominio (el mapper se encarga de llamar
     * a Task.create() y setear las fechas).
     * 4. Guarda la nueva tarea en el repo.
     * 5. Mapea el dominio a DTO para respuesta.
     */


    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectRepository projectRepository;

    public CreateTaskUseCase(TaskRepository taskRepository, TaskMapper taskMapper, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.projectRepository = projectRepository;
    }

    public TaskResponseDTO execute(ProjectRequestDTO requestDTO1, TaskRequestDTO requestDTO){

        /*1) Validar si existe el proyecto*/
        if(!projectRepository.existsByName(requestDTO1.name())){
            throw new ResourceNotFoundException("El proyecto no fue encontrado"+requestDTO1.name());
        }
        /*2)  Validar que el proyecto no este cerrado*/
        if(requestDTO1.status() == ProjectStatus.CLOSED){
            throw new BusinessRuleViolationsException("No se Puede agregar una tarea a un proyecto Cerrado (CLOSED).");
        }
        /*3) Validar si existe la tarea en el proyecto*/
        if (taskRepository.existByTitleAndProject(requestDTO.title(), requestDTO.project())) {
            throw new DuplicateResourceException("Ya existe una tarea con el mismo titulo.");
        }

        Task newTask = taskMapper.toDomain(requestDTO);
        newTask = taskRepository.save(newTask);

        return taskMapper.toResponseDTO(newTask);
    }




}
