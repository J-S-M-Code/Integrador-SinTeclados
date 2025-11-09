package domain.model;

import infrastructure.exception.BusinessRuleViolationsException;

import java.time.LocalDateTime;

public class Task {
    private Long id;
    private String title;//ready
    private Proyect proyect;
    private Integer estimatedHours;//ready
    private String assignee;
    private TaskStatus status;
    private LocalDateTime finishedAt;
    private LocalDateTime createdAt;

    private Task(){

    }
    public static Task create(Long id, String title, Proyect proyect, Integer estimatedHours,
                              String assignee, TaskStatus status, LocalDateTime finishedAt,
                              LocalDateTime createdAt){

        if(title == null || title.isBlank()){
            throw new BusinessRuleViolationsException("Task title should not be empty.");
        }

        if(estimatedHours == null || estimatedHours < 0 || estimatedHours >= Integer.MAX_VALUE){
            throw new BusinessRuleViolationsException("Estimated hours should be above 0");
        }

        if(assignee == null || assignee.isBlank()){
            throw new BusinessRuleViolationsException();
        }



        



        return new Taks();


    }


}
