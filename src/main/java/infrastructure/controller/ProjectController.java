package infrastructure.controller;

import application.dto.request.ProjectRequestDTO;
import application.dto.request.TaskCommentRequestDTO;
import application.dto.response.CommentResponseDTO;
import application.dto.response.ProjectResponseDTO;
import application.dto.response.TaskResponseDTO;
import application.usecase.CreateProjectUseCase;
import application.usecase.FindTaskUseCase;
import domain.model.TaskStatus;
import domain.repository.ProjectRepository;
import application.dto.response.TaskWithCommentsResponseDTO;
import application.usecase.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

@RestController
public class ProjectController {

    // Casos de uso que se van a utilizar para responder a los post y get
    private final CreateProjectUseCase createProjectUseCase;
    private final FindTaskUseCase findTaskUseCase;
    private final AddCommentToTaskUseCase addCommentToTaskUseCase;
    private final GetTaskByIdUseCase getTaskByIdUseCase;

    public ProjectController(CreateProjectUseCase createProjectUseCase, GetTaskByIdUseCase getTaskByIdUseCase, AddCommentToTaskUseCase addCommentToTaskUseCase, FindTaskUseCase findTaskUseCase) {
        this.createProjectUseCase = createProjectUseCase;
        this.addCommentToTaskUseCase = addCommentToTaskUseCase;
        this.findTaskUseCase = findTaskUseCase;
        this.getTaskByIdUseCase = getTaskByIdUseCase;
    }

    // --- Endpoints de Proyectos ---

    /**
     * Endpoint: POST /projects
     * Crea un nuevo proyecto.
     */
    @PostMapping("/projects")
    public ResponseEntity<ProjectResponseDTO> createProject(@Valid @RequestBody ProjectRequestDTO request) {
        ProjectResponseDTO response = createProjectUseCase.execute(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    // --- Endpoints de Comentarios ---

    // projectId no es utilizado, pero es parte de la URL

    @PostMapping("/projects/{projectId}/tasks/{taskId}/comments")
    public ResponseEntity<CommentResponseDTO> addCommentToTask(
            @PathVariable Long projectId, @PathVariable Long taskId,@Valid @RequestBody TaskCommentRequestDTO request) {

        CommentResponseDTO response = addCommentToTaskUseCase.execute(request, taskId);

        return ResponseEntity.status(201).body(response);
    }

    /**
     * Endpoint para buscar las tareas que cumplan con un estado determinado
     */
    @GetMapping("/tasks") // responde a un get a /projects/tasks
    public ResponseEntity<List<TaskResponseDTO>> getTasksByStatus(@RequestParam("status") TaskStatus status) {
        List<TaskResponseDTO> responseDTOs = findTaskUseCase.execute(status);
        return ResponseEntity.ok(responseDTOs); //Retorna 200 Ok con la lista
    }

    @GetMapping("/projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<TaskWithCommentsResponseDTO> getTaskById(@PathVariable Long projectId,@PathVariable Long taskId, @RequestParam(value = "comments", defaultValue = "false") boolean withComments) {

        TaskWithCommentsResponseDTO response = getTaskByIdUseCase.execute(taskId, withComments);
        return ResponseEntity.ok(response);
    }

}