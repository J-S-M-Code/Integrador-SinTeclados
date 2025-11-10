package model.integradorsinteclados;

import domain.model.Proyect;
import domain.model.ProyectStatus;
import domain.model.Task;
import domain.model.TaskStatus;
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
        Proyect proyect = Proyect.create(1234L,
                "Proyecto Prog Avanz",
                LocalDate.of(2025, 10, 11),
                LocalDate.of(2025, 10, 30),
                ProyectStatus.PLANNED,
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
                proyect,
                15,
                "Joaquin del Canto",
                TaskStatus.TODO,
                fechaInicio,
                fechaFin);

        assertNotNull(task, "La tarea no deberia ser nula");

        assertNull(task.getId(), "El id debe ser nulo antes de guardarse");

        assertEquals("Prog II: implement", task.getTitle());
        assertEquals(proyect, task.getProyect(), "El proyecto asignado no es el correcto");        assertEquals(15, task.getEstimatedHours());
        assertEquals(15, task.getEstimatedHours());
        assertEquals("Joaquin del Canto", task.getAssignee());
        assertEquals(TaskStatus.TODO, task.getStatus());
        assertEquals(fechaInicio, task.getCreatedAt());
        assertEquals(fechaFin, task.getFinishedAt());




    }


}
