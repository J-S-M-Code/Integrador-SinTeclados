package domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    @Order(1)
    @DisplayName("Camino 1: Objeto Task Completo")

    public void OBjetoTaskCompleto() {
        //Creacion de un proyecto para la tarea
        Project project = Project.create("Proyecto Prog Avanz",
                LocalDate.of(2025, 10, 11),
                LocalDate.of(2025, 10, 30),
                ProjectStatus.PLANNED,
                Optional.empty());

        //Crear fecha de inicio
        LocalDateTime fechaInicio = LocalDateTime.of(2025,
                Month.NOVEMBER,
                10,
                19,
                0);
        //Crear fecha de Fin
        LocalDateTime fechaFin = LocalDateTime.of(2025,
                Month.NOVEMBER,
                12,
                23,
                0);
        //Crear Objeto Tarea
        Task task = Task.create(null,
                "Prog II: implement",
                project,
                15,
                "Joaquin del Canto",
                TaskStatus.TODO,
                fechaInicio,
                fechaFin);

        assertNotNull(task, "La tarea no deberia ser nula");

        assertNull(task.getId(), "El id debe ser nulo antes de guardarse");

        assertEquals("Prog II: implement", task.getTitle());
        assertEquals(project, task.getProyect(), "El proyecto asignado no es el correcto");        assertEquals(15, task.getEstimatedHours());
        assertEquals(15, task.getEstimatedHours());
        assertEquals("Joaquin del Canto", task.getAssignee());
        assertEquals(TaskStatus.TODO, task.getStatus());
        assertEquals(fechaInicio, task.getCreatedAt());
        assertEquals(fechaFin, task.getFinishedAt());




    }


}
