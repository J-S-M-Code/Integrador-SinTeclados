package domain.model;

public class TaskComment {
    private String content;
    private Task task;

    TaskComment(String content, Task task){
        this.content = content;
        this.task = task;
    }
}
