package application.usecase;

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
import org.mockito.Mockito; // <-- Nuevo
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CreateTaskTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private CreateTaskUseCase createTaskUseCase;

    private final Long projectId = 1L;
    private Project mockActiveProject;

    @BeforeEach
    void setupMocks() {
        // Objeto Project Mockeado, Activo y con ID
        mockActiveProject = mock(Project.class);

        Mockito.lenient().when(mockActiveProject.getId()).thenReturn(projectId);
        Mockito.lenient().when(mockActiveProject.getStatus()).thenReturn(ProjectStatus.ACTIVE);
    }

    @Test
    @Order(1)
    @DisplayName("Crear un task con Datos validos")
    void testCreateTask_Debería_Ejecutarse_Con_Datos_Válidos() {
        LocalDateTime safeStart = LocalDateTime.now().plusSeconds(5);
        LocalDateTime safeFinish = safeStart.plusDays(1);

        TaskRequestDTO taskRequestDTO = new TaskRequestDTO(
                null, "Hacer el testing del CU", 8, "Analista QA",
                TaskStatus.TODO, safeFinish, safeStart
        );

        Task taskToSave = mock(Task.class);
        Task savedTask = mock(Task.class);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(mockActiveProject));

        when(taskRepository.existByTitleAndProject(taskRequestDTO.title(), mockActiveProject)).thenReturn(false);

        when(taskMapper.toDomain(taskRequestDTO, mockActiveProject)).thenReturn(taskToSave);
        when(taskRepository.save(taskToSave)).thenReturn(savedTask);

        // TaskResponseDTO necesita todos los campos, incluyendo las fechas
        TaskResponseDTO expectedResponse = new TaskResponseDTO(
                100L, "Hacer el testing del CU", mockActiveProject, 8,
                "Analista QA", TaskStatus.TODO, safeStart, safeFinish
        );
        when(taskMapper.toResponseDTO(savedTask)).thenReturn(expectedResponse);


        TaskResponseDTO actualResponse = createTaskUseCase.execute(projectId, taskRequestDTO);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());

        verify(projectRepository).findById(projectId);
        verify(taskRepository).existByTitleAndProject(taskRequestDTO.title(), mockActiveProject);
        verify(taskMapper).toDomain(taskRequestDTO, mockActiveProject);
        verify(taskRepository).save(taskToSave);
        verify(taskMapper).toResponseDTO(savedTask);
    }

    @Test
    @Order(2)
    @DisplayName("Test que lanza una exception cuando el proyecto no existe")
    void testCrearTask_DeberiaLanzarResourceNotFoundException_CuandoElProyectoNoExiste() {
        Long noExistentId = 999L;

        TaskRequestDTO taskRequestDTO = new TaskRequestDTO(
                null, "Test", 8, "Test", TaskStatus.TODO,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusSeconds(1) // Fecha dummy
        );

        when(projectRepository.findById(noExistentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            createTaskUseCase.execute(noExistentId, taskRequestDTO);
        });

        assertEquals("El proyecto no fue encontrado con id: " + noExistentId, exception.getMessage());

        verify(projectRepository).findById(noExistentId);
        verify(taskRepository, never()).existByTitleAndProject(any(), any());
    }

    @Test
    @Order(3)
    @DisplayName("Lanzar una Exception si el proyecto se encuentra en estado CERRADO (CLOSED)")
    void testCrearTask_DeberiaLanzarBusinessRuleException_CuandoElProyectoEstaCerrado() {
        Long closedProjectId = 98L;
        Project mockClosedProject = mock(Project.class);
        when(mockClosedProject.getStatus()).thenReturn(ProjectStatus.CLOSED);

        TaskRequestDTO taskRequestDTO = new TaskRequestDTO(
                null, "Tarea Imposible", 8, "Dev", TaskStatus.TODO,
                LocalDateTime.now().plusDays(5), LocalDateTime.now().plusSeconds(1)
        );

        when(projectRepository.findById(closedProjectId)).thenReturn(Optional.of(mockClosedProject));

        Exception exception = assertThrows(BusinessRuleViolationsException.class, () -> {
            createTaskUseCase.execute(closedProjectId, taskRequestDTO);
        });

        assertEquals("No se puede agregar una tarea a un proyecto Cerrado (CLOSED).", exception.getMessage());

        verify(projectRepository).findById(closedProjectId);
        verify(taskRepository, never()).existByTitleAndProject(any(), any());
    }

    @Test
    @Order(4)
    @DisplayName("Test que lanza una Exception si el titulo de la tarea ya esta duplicado")
    void testCrearTask_DeberiaLanzarDuplicateResourceException_CuandoElTituloDeLaTareaEstaDuplicado() {

        LocalDateTime safeStart = LocalDateTime.now().plusSeconds(5);

        TaskRequestDTO taskRequestDTO = new TaskRequestDTO(
                null, "Tarea Duplicada", 8, "QA",
                TaskStatus.TODO, safeStart.plusDays(1), safeStart
        );

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(mockActiveProject));
        when(taskRepository.existByTitleAndProject(taskRequestDTO.title(), mockActiveProject)).thenReturn(true);

        Exception exception = assertThrows(DuplicateResourceException.class, () -> {
            createTaskUseCase.execute(projectId, taskRequestDTO);
        });

        assertEquals("Ya existe una tarea con el mismo titulo en este proyecto.", exception.getMessage());


        verify(projectRepository).findById(projectId);
        verify(taskRepository).existByTitleAndProject(taskRequestDTO.title(), mockActiveProject);
        verify(taskMapper, never()).toDomain(any(), any());
        verify(taskRepository, never()).save(any());
    }
}