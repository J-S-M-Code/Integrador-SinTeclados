package application.usecase;

import infrastructure.exception.ResourceNotFoundException;
import domain.model.Task;
import domain.model.TaskComment;
import domain.repository.TaskCommentRepository;
import domain.repository.TaskRepository;

import java.util.List;

public class GestTaskByIdUseCase {

    private final TaskRepository taskRepository;
    private final TaskCommentRepository commentRepository;

    public GestTaskByIdUseCase(TaskRepository taskRepository, TaskCommentRepository commentRepository) {
        this.taskRepository = taskRepository;
        this.commentRepository = commentRepository;
    }

    /**
     * Ejecuta la búsqueda.
     * @param taskId El ID de la tarea.
     * @param withComments Flag para incluir o no los comentarios.
     * @return La tarea encontrada.
     * @throws ResourceNotFoundException si la tarea no existe.
     */
    public Task execute(Long taskId, boolean withComments) {
        // 1. Buscar la tarea
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        // 2. Si se solicitan, cargar los comentarios
        if (withComments) {
            List<TaskComment> comments = commentRepository.findAllByTaskId(taskId);
        }

        return task;
    }
}
