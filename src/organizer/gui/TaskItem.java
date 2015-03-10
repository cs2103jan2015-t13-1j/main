package organizer.gui;

import java.time.LocalDate;

import javafx.beans.property.*;
import organizer.logic.Task;

public class TaskItem {
	private final Task task;
	private final StringProperty taskName;
	private final StringProperty taskStatus;
	private final IntegerProperty taskId;
	private final ObjectProperty<LocalDate> taskDueDate;
	
	TaskItem(Task task) {
		this.task = task;
		taskName = new SimpleStringProperty(task.getTaskName());
		taskStatus = new SimpleStringProperty(task.getTaskStatus());
		taskDueDate = new SimpleObjectProperty<>(task.getDueDate());
		taskId = new SimpleIntegerProperty(task.getTaskID());
	}
	
	public String getTaskName() {
		return taskName.get();
	}
	
	public StringProperty taskNameProperty() {
		return taskName;
	}
	
	public LocalDate getTaskDueDate() {
		return taskDueDate.get();
	}
	
	public ObjectProperty<LocalDate> taskDueDateProperty() {
		return taskDueDate;
	}
	
	public int getTaskId() {
		return taskId.get();
	}
	
	public IntegerProperty taskIdProperty() {
		return taskId;
	}
	
	public String getTaskStatus() {
		return task.getTaskStatus();
	}
	
	public StringProperty taskStatusProperty() {
		return taskStatus;
	}
}
