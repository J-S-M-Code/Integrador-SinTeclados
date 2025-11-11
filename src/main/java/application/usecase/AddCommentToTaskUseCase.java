package application.usecase;

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
    private final Clock clock;

    public AddCommentToTaskUseCase(TaskRepository taskRepository, TaskCommentRepository commentRepository, Clock clock) {
        this.taskRepository = taskRepository;
        this.commentRepository = commentRepository;
        this.clock = clock;
    }

    /**
     * Ejecuta el caso de uso.
     * @param command El comando con los datos del comentario.
     * @param taskId El ID de la tarea a la que se añade el comentario.
     * @return El comentario creado.
     * @throws ResourceNotFoundException si la tarea no existe.
     */
    public TaskComment execute(AddCommentCommand command, Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        Long id = commentRepository.getLatestId() + 1L;

        TaskComment comment = TaskComment.create(
                id,
                task,
                command.text(),
                command.author(),
                LocalDateTime.now(clock)
        );

        // 3. Persistir el comentario
        return commentRepository.save(comment);
    }

    public record AddCommentCommand(
            String text,
            String author
    ) {}
}
