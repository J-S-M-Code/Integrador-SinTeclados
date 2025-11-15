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
    private final Clock clock;

    public AddCommentToTaskUseCase(TaskRepository taskRepository, TaskCommentRepository commentRepository, TaskCommentMapper commentMapper, Clock clock) {
        this.taskRepository = taskRepository;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.clock = clock;
    }

    /**
     * Ejecuta el caso de uso.
     * @param request El DTO a mapear
     * @param taskId El ID de la tarea a la que se añade el comentario.
     * @return El comentario creado.
     * @throws ResourceNotFoundException si la tarea no existe.
     */
    public CommentResponseDTO execute(TaskCommentRequestDTO request, Long taskId) {

        Task task = taskRepository.findById(request.task().getId()).orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        /*
        Long id = commentRepository.getLatestId() + 1L;

        TaskComment comment = TaskComment.create(
                id,
                task,
                command.text(),
                command.author(),
                LocalDateTime.now(clock)
        );
        */
        TaskComment comment = commentMapper.toDomain(request);
        
        TaskComment saved = commentRepository.save(comment);
        
        return commentMapper.toResponseDTO(saved);
    }

}
