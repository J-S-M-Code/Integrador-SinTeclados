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

import static org.junit.jupiter.api.Assertions.*;
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
        Project project = Project.create(
                "Proyecto Padre Real",
                LocalDate.now(),
                LocalDate.now().plusDays(20),
                ProjectStatus.PLANNED,
                Optional.of("Descripción")
        );
        Project savedProject = projectRepository.save(project); // La BD asigna un ID
        Long projectId = savedProject.getId();

        TaskRequestDTO taskRequestDTO = new TaskRequestDTO(
                null,
                "testing con H2",
                8,
                "Analista QA",
                TaskStatus.TODO,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now()
        );

        TaskResponseDTO actualResponse = createTaskUseCase.execute(projectId, taskRequestDTO);

        assertNotNull(actualResponse);
        assertNotNull(actualResponse.id(), "El ID debe haber sido generado por H2");
        assertEquals("testing con H2", actualResponse.title());
        assertEquals(projectId, actualResponse.project().getId());

        assertTrue(taskRepository.existByTitleAndProject("testing con H2", savedProject));}

    @Test
    @Order(2)
    @DisplayName("Test que lanza una exception cuando el proyecto no existe")
    void testCrearTask_DeberiaLanzarResourceNotFoundException_CuandoElProyectoNoExiste() {

        Long noExistentId = 999L; //ID que no existe

        TaskRequestDTO taskRequestDTO = new TaskRequestDTO(
                null, "Test", 8, "Test", TaskStatus.TODO,
                LocalDateTime.now().plusDays(1), LocalDateTime.now()
        );

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            createTaskUseCase.execute(noExistentId, taskRequestDTO);
        });

        assertEquals("El proyecto no fue encontrado id: " + noExistentId, exception.getMessage());
    }



    //Estos dos ultimos no dan en el error que esperas, y tampoco entiendo que preparas con mockito para tener el error
    @Test
    @Order(3)
    @DisplayName("Lanzar una Exception sin el preyoecto se encuentra en estado CERRADO (CLOSED)")
    void testCrearTask_DeberiaLanzarBusinessRuleException_CuandoElProyectoEstaCerrado() {
        Project closedProject = Project.create(
                "Proyecto Cerrado DB",
                LocalDate.now(),
                LocalDate.now().plusDays(10),
                ProjectStatus.CLOSED,
                Optional.empty()
        );
        Project savedClosedProject = projectRepository.save(closedProject);

        TaskRequestDTO taskRequestDTO = new TaskRequestDTO(
                null, "Tarea Imposible", 8, "Dev", TaskStatus.TODO,
                LocalDateTime.now().plusDays(5), LocalDateTime.now()
        );

        Exception exception = assertThrows(BusinessRuleViolationsException.class, () -> {
            createTaskUseCase.execute(savedClosedProject.getId(), taskRequestDTO);
        });

        assertEquals("No se puede agregar una tarea a un proyecto Cerrado (CLOSED).", exception.getMessage());
    }

    @Test
    @Order(4)
    @DisplayName("Test que lanza una Exception si el titulo de la tarea ya esta duplicado")
    void testCrearTask_DeberiaLanzarDuplicateResourceException_CuandoElTituloDeLaTareaEstaDuplicado() {
        Project project = Project.create(
                "Proyecto Duplicados",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                ProjectStatus.ACTIVE,
                Optional.empty()
        );
        Project savedProject = projectRepository.save(project);

        Task existingTask = Task.create(
                null,
                "Tarea Repetida", // Título reétido
                savedProject,
                5,
                "Dev 1",
                TaskStatus.TODO,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
        );
        taskRepository.save(existingTask);

        TaskRequestDTO requestDuplicado = new TaskRequestDTO(
                null,
                "Tarea Repetida",
                8, "Dev 2",
                TaskStatus.TODO,
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now()
        );

        Exception exception = assertThrows(DuplicateResourceException.class, () -> {
            createTaskUseCase.execute(savedProject.getId(), requestDuplicado);
        });

        assertEquals("Ya existe una tarea con el mismo titulo en este proyecto.", exception.getMessage());
    }


}