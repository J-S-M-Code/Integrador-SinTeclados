package infrastructure.controller;

import application.dto.request.ProjectRequestDTO;
import application.dto.response.ProjectResponseDTO;
import application.dto.response.TaskResponseDTO;
import application.usecase.CreateProjectUseCase;
import application.usecase.FindTaskUseCase;
import domain.model.TaskStatus;
import domain.repository.ProjectRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

//A implementar

@RestController
@RequestMapping("/projects")
public class ProjectController {

    // Casos de uso que se van a utilizar para responder a los post y get
    private final CreateProjectUseCase createProjectUseCase;
    private final FindTaskUseCase findTaskUseCase;

    // Inyectores de caso de uso por medio del constructor
    public ProjectController(CreateProjectUseCase createProjectUseCase,
                             FindTaskUseCase findTaskUseCase) {
        this.createProjectUseCase = createProjectUseCase;
        this.findTaskUseCase = findTaskUseCase;
    }

    /**
     * Endpoint para la creacion de un nuevo proyect
     */
    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(@Valid @RequestBody ProjectRequestDTO projectRequestDTO) {
        // Llamada al caso de uso
        ProjectResponseDTO responseDTO = createProjectUseCase.execute(projectRequestDTO);
        // Construimos el URI del nuevo recurso
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDTO.id())
                .toUri();
        // Develve un 201 Created con la URL y el DTO de respuesta
        return ResponseEntity.created(location).body(responseDTO);
    }

    /**
     * Endpoint para buscar las tareas que cumplan con un estado determinado
     */
    @GetMapping("/tasks") // responde a un get a /projects/tasks
    public ResponseEntity<List<TaskResponseDTO>> getTasksByStatus(@RequestParam("status") TaskStatus status) {
        List<TaskResponseDTO> responseDTOs = findTaskUseCase.execute(status);
        return ResponseEntity.ok(responseDTOs); //Retorna 200 Ok con la lista
    }



}
