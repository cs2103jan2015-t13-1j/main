package organizer.logic;

import java.time.LocalDate;

public class Task {

    private String taskName = null;
    private String taskStatus = null;
    private LocalDate dueDate = null;

    /**
     * Default constructor.
     */
    public Task() {
       taskName = null;
       taskStatus = null;
       dueDate = null;
    }

    public Task(String name, String status, LocalDate date) {
    	taskName = name;
    	taskStatus = status;
    	dueDate = date;
	}

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String name) {
        taskName = name;
    }
    
    public String getTaskStatus() {
    	return taskStatus;
    }
    
    public void setTaskStatus(String status) {
    	taskStatus = status;
    }
    
    public LocalDate getDueDate() {
    	return dueDate;
    }
    
    public void setDueDate(LocalDate date) {
    	dueDate = date;
    }
    
    
}