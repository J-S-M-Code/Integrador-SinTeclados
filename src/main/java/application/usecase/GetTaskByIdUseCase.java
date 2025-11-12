package application.usecase;

import application.mapper.TaskCommentMapper;
import infrastructure.exception.ResourceNotFoundException;
import domain.model.Task;
import domain.model.TaskComment;
import domain.repository.TaskCommentRepository;
import domain.repository.TaskRepository;

import java.util.List;

public class GetTaskByIdUseCase {

    private final TaskRepository taskRepository;
    private final TaskCommentRepository commentRepository;

    public GetTaskByIdUseCase(TaskRepository taskRepository, TaskCommentRepository commentRepository, TaskCommentMapper commentMapper) {
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

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (withComments) {
            List<TaskComment> comments = commentRepository.findAllByTaskId(taskId);
        }

        return task;
    }
}
