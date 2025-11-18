package persistence;

import application.usecase.AddCommentToTaskUseCase;
import application.dto.request.TaskCommentRequestDTO;
import application.dto.response.CommentResponseDTO;
import application.mapper.TaskCommentMapper;
import domain.model.Task;
import domain.model.TaskComment;
import domain.repository.TaskCommentRepository;
import domain.repository.TaskRepository;
import infrastructure.exception.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddCommentToTaskPersistenceIntegrationTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskCommentRepository commentRepository;

    @Mock
    private TaskCommentMapper commentMapper;

    @Mock
    private Clock clock;

    @InjectMocks
    private AddCommentToTaskUseCase addCommentToTaskUseCase;

    @Captor
    private ArgumentCaptor<TaskComment> commentCaptor;

    private static final LocalDateTime FIXED_NOW = LocalDateTime.of(2025, 11, 15, 21, 30, 0);
    private static final Clock FIXED_CLOCK = Clock.fixed(FIXED_NOW.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    @BeforeEach
    void setUp() {
        lenient().when(clock.instant()).thenReturn(FIXED_CLOCK.instant());
        lenient().when(clock.getZone()).thenReturn(FIXED_CLOCK.getZone());
    }

    @Test
    @DisplayName("Shall save a comment if a given task exists")
    void testExecute_ShouldSaveComment_WhenTaskExists_ImprovedLogic() {
        // 1. Arrange

        Long taskIdFromUrl = 1L;

        TaskCommentRequestDTO requestDTO = new TaskCommentRequestDTO(
                "Test Comment, for a, supposedly, existing Task",
                "yetanotherauthor"
        );

        Task mockTaskFound = mock(Task.class);
        //when(mockTaskFound.getId()).thenReturn(taskIdFromUrl);

        TaskComment savedComment = mock(TaskComment.class);
        //when(savedComment.getId()).thenReturn(100L);

        CommentResponseDTO expectedResponse = new CommentResponseDTO(
                100L,
                mockTaskFound,
                "Test Comment, for a, supposedly, existing Task",
                "yetanotherauthor",
                FIXED_NOW
        );

        when(taskRepository.findById(taskIdFromUrl)).thenReturn(Optional.of(mockTaskFound));

        when(commentRepository.save(any(TaskComment.class))).thenReturn(savedComment);

        when(commentMapper.toResponseDTO(savedComment)).thenReturn(expectedResponse);

        // 2. Act
        CommentResponseDTO actualResponse = addCommentToTaskUseCase.execute(requestDTO, taskIdFromUrl);

        // 3. Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());
        assertEquals(expectedResponse.text(), actualResponse.text());
        assertEquals(expectedResponse.createdAt(), actualResponse.createdAt());

        verify(taskRepository).findById(taskIdFromUrl);

        verify(commentRepository).save(commentCaptor.capture());
        TaskComment commentToSave = commentCaptor.getValue();

        assertNull(commentToSave.getId(), "ID expected to be null before inyecting");
        assertEquals(mockTaskFound, commentToSave.getTask());
        assertEquals("Test Comment, for a, supposedly, existing Task", commentToSave.getText()); // El texto correcto
        assertEquals("yetanotherauthor", commentToSave.getAuthor());
        assertEquals(FIXED_NOW, commentToSave.getCreatedAt());

        verify(commentMapper).toResponseDTO(savedComment);
    }

    @Test
    @DisplayName("Shall throw ResourceNotFoundException if there is no task associated to the given ID")
    void testExecute_ShouldThrowException_WhenTaskNotFound_ImprovedLogic() {
        // 1. Arrange
        Long invalidTaskId = 99L;

        // El DTO (simplificado)
        TaskCommentRequestDTO requestDTO = new TaskCommentRequestDTO(
                "Untestable Comment, for an untestable Task",
                "Autor"
        );

        when(taskRepository.findById(invalidTaskId)).thenReturn(Optional.empty());

        // 2. Act & 3. Assert
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            addCommentToTaskUseCase.execute(requestDTO, invalidTaskId);
        });

        assertEquals("Task not found with id: " + invalidTaskId, exception.getMessage());

        verify(taskRepository).findById(invalidTaskId);
        verify(commentRepository, never()).save(any());
        verify(commentMapper, never()).toResponseDTO(any());
    }
}
