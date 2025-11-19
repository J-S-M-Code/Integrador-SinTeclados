package application.usecase;

import application.dto.request.ProjectRequestDTO;
import application.dto.request.TaskRequestDTO;
import application.dto.response.TaskResponseDTO;
import application.mapper.TaskMapper;
import domain.model.Project;
import domain.model.ProjectStatus;
import domain.model.Task;
import domain.model.TaskStatus;
import domain.repository.ProjectRepository;
import domain.repository.TaskRepository;
import infrastructure.exception.BusinessRuleViolationsException;
import infrastructure.exception.DuplicateResourceException;
import infrastructure.exception.ResourceNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateTaskTest {

    @Mock // Simulamos el repo de Tareas
    private TaskRepository taskRepository;

    @Mock // Simulamos el repo de Proyectos
    private ProjectRepository projectRepository;

    @Mock // Simulamos el mapper de Tareas
    private TaskMapper taskMapper;

    @InjectMocks // Instanciamos el Caso de Uso a probar
    private CreateTaskUseCase createTaskUseCase;


    @Test
    @Order(1)
    @DisplayName("Crear un task con Datos validos")
    void testCreateTask_Debería_Ejecutarse_Con_Datos_Válidos() {
        //Preparar datos para este test
        LocalDateTime now = LocalDateTime.now();

        // 1. Crear el Proyecto padre (Mock)
        Project parentProject = mock(Project.class);
        when(parentProject.getId()).thenReturn(1L);
        when(parentProject.getName()).thenReturn("Proyecto Padre");
        when(parentProject.getStatus()).thenReturn(ProjectStatus.PLANNED);

        // 2. Crear el ProjectRequestDTO
        ProjectRequestDTO projectRequestDTO = new ProjectRequestDTO(
                parentProject.getName(),
                LocalDate.now().minusDays(10),
                LocalDate.now().plusDays(20),
                parentProject.getStatus(),
                "Descripción"
        );

        // 3. Crear el TaskRequestDTO (de entrada)
        LocalDateTime finishDate = LocalDateTime.now().plusMinutes(1);
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO(
                null, "Hacer el testing del CU", 8, "Analista QA",
                TaskStatus.TODO, finishDate, now
        );

        // 4. Crear la entidad Task (lo que devuelve el mapper)
        Task taskToSave = Task.create(
                null, "Hacer el testing del CU", parentProject, 8, "Analista QA",
                TaskStatus.TODO, now, finishDate
        );

        // 5. Crear la entidad Task guardada (Mock, lo que devuelve el repo)
        Task savedTask = mock(Task.class);
        when(savedTask.getId()).thenReturn(100L);
        when(savedTask.getTitle()).thenReturn("Hacer el testing del CU");
        when(savedTask.getProyect()).thenReturn(parentProject);
        when(savedTask.getEstimatedHours()).thenReturn(8);
        when(savedTask.getAssignee()).thenReturn("Analista QA");
        when(savedTask.getStatus()).thenReturn(TaskStatus.TODO);

        // 6. Crear el DTO de respuesta esperado
        TaskResponseDTO expectedResponse = new TaskResponseDTO(
                100L, "Hacer el testing del CU", parentProject, 8,
                "Analista QA", TaskStatus.TODO
        );

        // 7. Definir el comportamiento
        when(projectRepository.existsByName(projectRequestDTO.name())).thenReturn(true);
        // En lugar de taskRequestDTO.project(), pasamos parentProject
        when(taskRepository.existByTitleAndProject(taskRequestDTO.title(), parentProject)).thenReturn(false);
        when(taskMapper.toDomain(taskRequestDTO, parentProject)).thenReturn(taskToSave);
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);
        when(taskMapper.toResponseDTO(savedTask)).thenReturn(expectedResponse);


        TaskResponseDTO actualResponse = createTaskUseCase.execute(1L, taskRequestDTO);

        Assertions.assertNotNull(actualResponse);
        Assertions.assertEquals(expectedResponse.id(), actualResponse.id());
        Assertions.assertEquals(expectedResponse.title(), actualResponse.title());
        Assertions.assertEquals(expectedResponse.project(), actualResponse.project());

        // Verificamos que se llamó a cada método en el orden correcto
        verify(projectRepository).existsByName(projectRequestDTO.name());
        verify(taskRepository).existByTitleAndProject(taskRequestDTO.title(), parentProject);
        verify(taskMapper).toDomain(taskRequestDTO, parentProject);
        verify(taskRepository).save(taskToSave);
        verify(taskMapper).toResponseDTO(savedTask);
    }

    @Test
    @Order(2)
    @DisplayName("Test que lanza una exception cuando el proyecto no existe")
    void testCrearTask_DeberiaLanzarResourceNotFoundException_CuandoElProyectoNoExiste() {

        ProjectRequestDTO projectRequestDTO = new ProjectRequestDTO(
                "Proyecto Inexistente", null, null, ProjectStatus.PLANNED, null
        );

        TaskRequestDTO taskRequestDTO = new TaskRequestDTO(
                null, "Test", 8, "Test", TaskStatus.TODO, LocalDateTime.now().plusDays(1), LocalDateTime.now()
        );

        // comportamiento: el proyecto NO existe
        when(projectRepository.existsByName(projectRequestDTO.name())).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            createTaskUseCase.execute(1L, taskRequestDTO);
        });


        String expectedMessage = "El proyecto no fue encontrado" + projectRequestDTO.name();
        Assertions.assertEquals(expectedMessage, exception.getMessage());

        // Verificamos que solo se llamó a la primera validación
        verify(projectRepository).existsByName(projectRequestDTO.name());
        verify(taskRepository, never()).existByTitleAndProject(any(), any());
        verify(taskMapper, never()).toDomain(any(), any());
        verify(taskRepository, never()).save(any());
    }

    @Test
    @Order(3)
    @DisplayName("Lanzar una Exception sin el preyoecto se encuentra en estado CERRADO (CLOSED)")
    void testCrearTask_DeberiaLanzarBusinessRuleException_CuandoElProyectoEstaCerrado() {
        //DTO específico con estado CLOSED
        ProjectRequestDTO closedProjectDTO = new ProjectRequestDTO(
                "Proyecto Cerrado", null, null, ProjectStatus.CLOSED, null
        );
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO(
                null, "Test", 8, "Test", TaskStatus.TODO, LocalDateTime.now().plusDays(5), LocalDateTime.now()
        );

        // Definimos el comportamiento: el proyecto SÍ existe
        when(projectRepository.existsByName(closedProjectDTO.name())).thenReturn(true);

        Exception exception = assertThrows(BusinessRuleViolationsException.class, () -> {
            createTaskUseCase.execute(98L, taskRequestDTO);
        });

        String expectedMessage = "No se Puede agregar una tarea a un proyecto Cerrado (CLOSED).";
        Assertions.assertEquals(expectedMessage, exception.getMessage());


        verify(projectRepository).existsByName(closedProjectDTO.name());
        verify(taskRepository, never()).existByTitleAndProject(any(), any());
    }

    @Test
    @Order(4)
    @DisplayName("Test que lanza una Exception si el titulo de la tarea ya esta duplicado")
    void testCrearTask_DeberiaLanzarDuplicateResourceException_CuandoElTituloDeLaTareaEstaDuplicado() {
        Project parentProject = mock(Project.class);
        when(parentProject.getName()).thenReturn("Proyecto Padre");
        when(parentProject.getStatus()).thenReturn(ProjectStatus.PLANNED);

        ProjectRequestDTO projectRequestDTO = new ProjectRequestDTO(
                parentProject.getName(), null, null, parentProject.getStatus(), null
        );

        TaskRequestDTO taskRequestDTO = new TaskRequestDTO(
                null, "Tarea Duplicada", 8, "QA",
                TaskStatus.TODO, null, LocalDateTime.now()
        );


        // 1. El proyecto SÍ existe
        when(projectRepository.existsByName(projectRequestDTO.name())).thenReturn(true);
        // 2. La tarea SÍ existe (devuelve true)
        when(taskRepository.existByTitleAndProject(taskRequestDTO.title(), parentProject)).thenReturn(true);

        Exception exception = assertThrows(DuplicateResourceException.class, () -> {
            createTaskUseCase.execute(98L, taskRequestDTO);
        });

        String expectedMessage = "Ya existe una tarea con el mismo titulo.";
        Assertions.assertEquals(expectedMessage, exception.getMessage());

        verify(projectRepository).existsByName(projectRequestDTO.name());
        verify(taskRepository).existByTitleAndProject(taskRequestDTO.title(), parentProject);
        verify(taskMapper, never()).toDomain(any(), any());
        verify(taskRepository, never()).save(any());
    }


}