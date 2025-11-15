package application.dto.request;

import domain.model.Task;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

public record TaskCommentRequestDTO(
        @NotBlank(message = "There can't be comments without a task reference.")
        Task task,
        @NotNull(message = "There can't be empty comments")
        String text,
        @NotNull(message = "There can't be comments without an author")
        String author,
        @NotNull(message = "There can't be comments without a posting date")
        @PastOrPresent(message = "Posting date must be past or present.")
        LocalDateTime createdAt
){ }
