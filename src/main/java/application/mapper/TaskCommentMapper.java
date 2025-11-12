package application.mapper;

import application.dto.request.TaskCommentRequestDTO;
import application.dto.response.CommentResponseDTO;
import domain.model.TaskComment;
import org.springframework.stereotype.Component;

@Component
public class TaskCommentMapper {

    public TaskComment toDomain(TaskCommentRequestDTO dto){
        if (dto == null){
            return null;
        }
        return TaskComment.create(dto.task(),
                dto.text(),
                dto.author(),
                dto.createdAt());
    }

    public CommentResponseDTO toResponseDTO(TaskComment taskComment){
        if (taskComment == null) {
            return null;
        }
        return new CommentResponseDTO(taskComment.getId(),
                taskComment.getTask(),
                taskComment.getText(),
                taskComment.getAuthor(),
                taskComment.getCreatedAt());
    }
}
