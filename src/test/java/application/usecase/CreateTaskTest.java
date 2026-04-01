package application.usecase;

import application.dto.request.ProjectRequestDTO;
import application.dto.request.TaskRequestDTO;
import application.dto.response.TaskResponseDTO;
import application.mapper.TaskMapper;
import domain.model.Project;
import domain.model.ProjectStatus;
import domain.model.Task;
import domain.model.TaskStatus;
import domain.repository.ProjectRepository;
import domain.repository.TaskRepository;
import infrastructure.exception.BusinessRuleViolationsException;
import infrastructure.exception.DuplicateResourceException;
import infrastructure.exception.ResourceNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateTaskTest {
}