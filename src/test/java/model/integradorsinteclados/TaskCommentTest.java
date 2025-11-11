package model.integradorsinteclados;

import domain.model.Proyect;
import static domain.model.ProyectStatus.*;
import domain.model.Task;
import static domain.model.TaskStatus.*;
import domain.model.TaskComment;
import infrastructure.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la entidad TaskComment, enfocados en el método factory 'create'.
 */
@ExtendWith(MockitoExtension.class)
public class TaskCommentTest {

    private final Long testId = 1L;

    private final LocalDateTime fixedTime1 = LocalDateTime.of(2025, 11, 11, 12, 0, 0);
    private final LocalDateTime fixedTime2 = LocalDateTime.of(2025, 12, 11, 12, 0, 0);
    private final LocalDate fixedTime3 = LocalDate.of(2025, 11, 12);
    private final LocalDate fixedTime4 = LocalDate.of(2025, 12, 12);

    private final Proyect testProject = Proyect.create(10L, "testName", fixedTime3, fixedTime4, ACTIVE, "".describeConstable());

    private final Task testTask = Task.create(11L, "testTask", testProject, 10, "Pepe Botellas", TODO, fixedTime1, fixedTime2);

    private final String validText = "Este es un comentario válido.";
    private final String validAuthor = "Autor Válido";
    // Usamos una fecha y hora fijas para los tests



    @Test
    @DisplayName("Debería crear un TaskComment exitosamente con datos válidos")
    void createTaskComment_WithValidData_ShouldSucceed() {
        // Act
        TaskComment comment = TaskComment.create(testId, testTask, validText, validAuthor, fixedTime1);

        // Assert
        assertNotNull(comment);
        assertEquals(testTask, comment.getTask());
        assertEquals(validText, comment.getText());
        assertEquals(validAuthor, comment.getAuthor());
        assertEquals(fixedTime1, comment.getCreatedAt());
    }

    @Test
    @DisplayName("Debería lanzar ValidationException si la Tarea (task) es nula")
    void createTaskComment_WithNullTask_ShouldThrowValidationException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            TaskComment.create(testId,null, validText, validAuthor, fixedTime1);
        });

        assertEquals("Comment should be associated to a Task.", exception.getMessage());
    }

    @Test
    @DisplayName("Debería lanzar ValidationException si el Texto (text) es nulo")
    void createTaskComment_WithNullText_ShouldThrowValidationException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            TaskComment.create(testId, testTask, null, validAuthor, fixedTime1);
        });

        assertEquals("Comment should have text.", exception.getMessage());
    }

    @Test
    @DisplayName("Debería lanzar ValidationException si el Texto (text) está vacío")
    void createTaskComment_WithEmptyText_ShouldThrowValidationException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            TaskComment.create(testId, testTask, "", validAuthor, fixedTime1);
        });

        assertEquals("Comment should have text.", exception.getMessage());
    }

    @Test
    @DisplayName("Debería lanzar ValidationException si el Texto (text) solo contiene espacios")
    void createTaskComment_WithBlankText_ShouldThrowValidationException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            TaskComment.create(testId, testTask, "   ", validAuthor, fixedTime1);
        });

        assertEquals("Comment should have text.", exception.getMessage());
    }

    @Test
    @DisplayName("Debería lanzar ValidationException si el Autor (author) es nulo")
    void createTaskComment_WithNullAuthor_ShouldThrowValidationException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            TaskComment.create(testId, testTask, validText, null, fixedTime1);
        });

        assertEquals("Comment should have an author.", exception.getMessage());
    }

    @Test
    @DisplayName("Debería lanzar ValidationException si el Autor (author) está vacío")
    void createTaskComment_WithEmptyAuthor_ShouldThrowValidationException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            TaskComment.create(testId, testTask, validText, "", fixedTime1);
        });

        assertEquals("Comment should have an author.", exception.getMessage());
    }

    @Test
    @DisplayName("Debería lanzar ValidationException si el Autor (author) solo contiene espacios")
    void createTaskComment_WithBlankAuthor_ShouldThrowValidationException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            TaskComment.create(testId, testTask, validText, "   ", fixedTime1);
        });

        assertEquals("Comment should have an author.", exception.getMessage());
    }
}
