package organizer.gui;

import java.time.LocalDate;
import java.time.LocalTime;

import javafx.beans.property.*;
import organizer.logic.Task;

public class TaskItem {
	private final Task task;
	private final StringProperty taskName;
	private final StringProperty taskStatus;
	private final StringProperty taskPriority;
	private final IntegerProperty taskIndex;
	private final ObjectProperty<LocalDate> taskDueDate;
	private final ObjectProperty<LocalTime> taskStartTime;
	private final ObjectProperty<LocalTime> taskEndTime;
	
	TaskItem(Task task, int index) {
		this.task = task;
		taskName = new SimpleStringProperty(task.getTaskName());
		taskStatus = new SimpleStringProperty(task.getTaskStatus());
		taskDueDate = new SimpleObjectProperty<>(task.getDueDate());
		taskStartTime = new SimpleObjectProperty<>(task.getStartTime());
		taskEndTime = new SimpleObjectProperty<>(task.getEndTime());
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
	
	public LocalTime getTaskStartTime() {
		return task.getStartTime();
	}
	
	public ObjectProperty<LocalTime> taskStartTimeProperty() {
		return taskStartTime;
	}
	
	public LocalTime getTaskEndTime() {
		return task.getEndTime();
	}
	
	public ObjectProperty<LocalTime> taskEndTimeProperty() {
		return taskEndTime;
	}
}
