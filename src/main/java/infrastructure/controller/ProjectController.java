package infrastructure.controller;

import application.dto.request.ProjectRequestDTO;
import application.dto.response.ProjectResponseDTO;
import application.usecase.CreateProjectUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

//A implementar

@RestController
@RequestMapping("/projects")
public class ProjectController {

    // Casos de uso que se van a utilizar para responder a los post y get
    private final CreateProjectUseCase createProjectUseCase;;

    // Inyectores de caso de uso por medio del constructor
    public ProjectController(CreateProjectUseCase createProjectUseCase) {
        this.createProjectUseCase = createProjectUseCase;
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



}
