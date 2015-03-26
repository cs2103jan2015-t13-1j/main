package organizer.gui;

import java.time.LocalDate;

import javafx.beans.property.*;
import organizer.logic.Task;

public class TaskItem {
	private final Task task;
	private final StringProperty taskName;
	private final StringProperty taskStatus;
	private final StringProperty taskPriority;
	private final IntegerProperty taskIndex;
	private final ObjectProperty<LocalDate> taskDueDate;
	
	TaskItem(Task task, int index) {
		this.task = task;
		taskName = new SimpleStringProperty(task.getTaskName());
		taskStatus = new SimpleStringProperty(task.getTaskStatus());
		taskDueDate = new SimpleObjectProperty<>(task.getDueDate());
		taskIndex = new SimpleIntegerProperty(index);
		taskPriority = new SimpleStringProperty(task.getTaskPriority());
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
	
	public int getTaskIndex() {
		return taskIndex.get();
	}
	
	public IntegerProperty taskIndexProperty() {
		return taskIndex;
	}
	
	public String getTaskStatus() {
		return task.getTaskStatus();
	}
	
	public StringProperty taskStatusProperty() {
		return taskStatus;
	}
	
	public String getTaskPriority() {
		return task.getTaskPriority();
	}
	
	public StringProperty taskPriorityProperty() {
		return taskPriority;
	}
}
