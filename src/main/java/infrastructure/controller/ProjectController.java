package infrastructure.controller;

import application.dto.request.ProjectRequestDTO;
import application.dto.request.TaskCommentRequestDTO;
import application.dto.response.CommentResponseDTO;
import application.dto.response.ProjectResponseDTO;
import application.usecase.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

// A implementar

@RestController
public class ProjectController {

    private final CreateProjectUseCase createProjectUseCase;
    private final AddCommentToTaskUseCase addCommentToTaskUseCase;

    public ProjectController(CreateProjectUseCase createProjectUseCase,
                                    CreateTaskUseCase createTaskUseCase,
                                    AddCommentToTaskUseCase addCommentToTaskUseCase,
                                    GetTaskByIdUseCase getTaskByIdUseCase,
                                    FindTaskUseCase findTaskUseCase) {
        this.createProjectUseCase = createProjectUseCase;
        this.addCommentToTaskUseCase = addCommentToTaskUseCase;
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

    @PostMapping("/projects/{projectId}/tasks/{taskId}/comments")
    public ResponseEntity<CommentResponseDTO> addCommentToTask(
            @PathVariable Long projectId, // No se usa, pero es parte de la URL
            @PathVariable Long taskId,
            @Valid @RequestBody TaskCommentRequestDTO request) {

        CommentResponseDTO response = addCommentToTaskUseCase.execute(request, taskId);

        return ResponseEntity.status(201).body(response);
    }
}