package application.usecase;

import application.dto.request.TaskCommentRequestDTO;
import application.dto.response.CommentResponseDTO;
import application.mapper.TaskCommentMapper;
import infrastructure.exception.ResourceNotFoundException;
import domain.model.Task;
import domain.model.TaskComment;
import domain.repository.TaskCommentRepository;
import domain.repository.TaskRepository;

import java.time.Clock;
import java.time.LocalDateTime;

public class AddCommentToTaskUseCase {

    private final TaskRepository taskRepository;
    private final TaskCommentRepository commentRepository;
    private final TaskCommentMapper commentMapper;
    private final Clock clock; // Ahora sí lo vamos a usar

    public AddCommentToTaskUseCase(TaskRepository taskRepository, TaskCommentRepository commentRepository, TaskCommentMapper commentMapper, Clock clock) {
        this.taskRepository = taskRepository;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.clock = clock;
    }

    /**
     * Ejecuta el caso de uso (Versión Mejorada).
     * @param request El DTO simplificado (solo texto y autor).
     * @param taskId El ID de la tarea (de la URL), que ahora SÍ se usa.
     * @return El comentario creado.
     * @throws ResourceNotFoundException si la tarea no existe.
     */
    public CommentResponseDTO execute(TaskCommentRequestDTO request, Long taskId) {

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        LocalDateTime creationTime = LocalDateTime.now(clock);

        TaskComment newComment = TaskComment.create(
                task,
                request.text(),
                request.author(),
                creationTime
        );

        TaskComment saved = commentRepository.save(newComment);

        return commentMapper.toResponseDTO(saved);
    }


}
