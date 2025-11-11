package model.integradorsinteclados;

import domain.model.Task;
import domain.model.TaskComment;
import infrastructure.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la entidad TaskComment, enfocados en el método factory 'create'.
 */
@ExtendWith(MockitoExtension.class)
public class TaskCommentTest {

    @Mock
    private Long mockId;

    @Mock
    private Task mockTask;

    private final String validText = "Este es un comentario válido.";
    private final String validAuthor = "Autor Válido";
    // Usamos una fecha y hora fijas para los tests
    private final LocalDateTime fixedTime = LocalDateTime.of(2025, 11, 11, 12, 0, 0);


    @Test
    @DisplayName("Debería crear un TaskComment exitosamente con datos válidos")
    void createTaskComment_WithValidData_ShouldSucceed() {
        // Act
        TaskComment comment = TaskComment.create(mockId, mockTask, validText, validAuthor, fixedTime);

        // Assert
        assertNotNull(comment);
        assertEquals(mockTask, comment.getTask());
        assertEquals(validText, comment.getText());
        assertEquals(validAuthor, comment.getAuthor());
        assertEquals(fixedTime, comment.getCreatedAt());
    }

    @Test
    @DisplayName("Debería lanzar ValidationException si la Tarea (task) es nula")
    void createTaskComment_WithNullTask_ShouldThrowValidationException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            TaskComment.create(mockId,null, validText, validAuthor, fixedTime);
        });

        assertEquals("Comment should be associated to a Task.", exception.getMessage());
    }

    @Test
    @DisplayName("Debería lanzar ValidationException si el Texto (text) es nulo")
    void createTaskComment_WithNullText_ShouldThrowValidationException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            TaskComment.create(mockId, mockTask, null, validAuthor, fixedTime);
        });

        assertEquals("Comment should have text.", exception.getMessage());
    }

    @Test
    @DisplayName("Debería lanzar ValidationException si el Texto (text) está vacío")
    void createTaskComment_WithEmptyText_ShouldThrowValidationException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            TaskComment.create(mockId, mockTask, "", validAuthor, fixedTime);
        });

        assertEquals("Comment should have text.", exception.getMessage());
    }

    @Test
    @DisplayName("Debería lanzar ValidationException si el Texto (text) solo contiene espacios")
    void createTaskComment_WithBlankText_ShouldThrowValidationException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            TaskComment.create(mockId, mockTask, "   ", validAuthor, fixedTime);
        });

        assertEquals("Comment should have text.", exception.getMessage());
    }

    @Test
    @DisplayName("Debería lanzar ValidationException si el Autor (author) es nulo")
    void createTaskComment_WithNullAuthor_ShouldThrowValidationException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            TaskComment.create(mockId, mockTask, validText, null, fixedTime);
        });

        assertEquals("Comment should have an author.", exception.getMessage());
    }

    @Test
    @DisplayName("Debería lanzar ValidationException si el Autor (author) está vacío")
    void createTaskComment_WithEmptyAuthor_ShouldThrowValidationException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            TaskComment.create(mockId, mockTask, validText, "", fixedTime);
        });

        assertEquals("Comment should have an author.", exception.getMessage());
    }

    @Test
    @DisplayName("Debería lanzar ValidationException si el Autor (author) solo contiene espacios")
    void createTaskComment_WithBlankAuthor_ShouldThrowValidationException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            TaskComment.create(mockId, mockTask, validText, "   ", fixedTime);
        });

        assertEquals("Comment should have an author.", exception.getMessage());
    }
}
