package domain.model;

import infrastructure.exception.BusinessRuleViolationsException;
import java.util.concurrent.ThreadLocalRandom;

import java.time.LocalDateTime;

public class Task {
    private Long id;
    private String title;//ready
    private Proyect proyect;
    private Integer estimatedHours;//ready
    private String assignee;//ready
    private TaskStatus status; //ready
    private LocalDateTime finishedAt;//ready
    private LocalDateTime createdAt;//ready

    public Task(Long id,
                String title,
                Proyect proyect,
                Integer estimatedHours,
                String assignee,
                TaskStatus status,
                LocalDateTime finishedAt,
                LocalDateTime createdAt) {

        this.id = ThreadLocalRandom.current().nextLong();
        this.title = title;
        this.proyect = proyect;
        this.estimatedHours = estimatedHours;
        this.assignee = assignee;
        this.status = status;
        this.finishedAt = finishedAt;
        this.createdAt = createdAt;
    }

    public static Task create(Long id,
                              String title,
                              Proyect proyect,
                              Integer estimatedHours,
                              String assignee,
                              TaskStatus status,
                              LocalDateTime finishedAt,
                              LocalDateTime createdAt){

        if(title == null || title.isBlank()){
            throw new BusinessRuleViolationsException("El titulo de la tarea no puede estar vacio");
        }

        if(estimatedHours == null || estimatedHours < 0 || estimatedHours >= Integer.MAX_VALUE){
            throw new BusinessRuleViolationsException("Estimated hours should be above 0");
        }

        if(assignee == null || assignee.isBlank()){
            throw new BusinessRuleViolationsException("La secionaria es inavlida");
        }

        if(status == null){
            throw  new  BusinessRuleViolationsException("EL estado de la tarea no puede ser null");
        }

        if(status != TaskStatus.IN_PROGRESS && status != TaskStatus.DONE && status != TaskStatus.TODO){
            throw new BusinessRuleViolationsException("El Estado solo puede ser: IN_PROGESS, DONE, TODO");
        }

        if(finishedAt == null ||  finishedAt.isBefore(LocalDateTime.now())) {
            throw  new  BusinessRuleViolationsException("La fecha de finalizacion es invalida");
        }

        if(createdAt == null || createdAt.isBefore(LocalDateTime.now())) {
            throw new BusinessRuleViolationsException("La fecha de inicio es invalida");
        }

        if(createdAt.isAfter(finishedAt)){
            throw new BusinessRuleViolationsException("La fecha de inicio no puede ser posterio a fecha de fin");
        }

        if(status == TaskStatus.DONE){
            finishedAt = LocalDateTime.now();
        }

        return new Task(id, title, proyect, estimatedHours, assignee, status, createdAt, finishedAt);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Proyect getProyect() {
        return proyect;
    }

    public void setProyect(Proyect proyect) {
        this.proyect = proyect;
    }

    public Integer getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(Integer estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
