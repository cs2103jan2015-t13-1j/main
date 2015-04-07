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
	private final ObjectProperty<LocalDate> taskStartDate;
	private final ObjectProperty<LocalDate> taskEndDate;
	private final ObjectProperty<LocalTime> taskStartTime;
	private final ObjectProperty<LocalTime> taskEndTime;
	
	TaskItem(Task task, int index) {
		this.task = task;
		taskName = new SimpleStringProperty(task.getTaskName());
		taskStatus = new SimpleStringProperty(task.getTaskStatus());
		taskStartDate = new SimpleObjectProperty<>(task.getTaskStartDate());
		taskEndDate = new SimpleObjectProperty<>(task.getTaskEndDate());
		taskStartTime = new SimpleObjectProperty<>(task.getTaskStartTime());
		taskEndTime = new SimpleObjectProperty<>(task.getTaskEndTime());
		taskIndex = new SimpleIntegerProperty(index);
		taskPriority = new SimpleStringProperty(task.getTaskPriority());
	}
	
	public String getTaskType() {
		return task.getTaskType();
	}
	
	public String getTaskName() {
		return taskName.get();
	}
	
	public StringProperty taskNameProperty() {
		return taskName;
	}
	
	public LocalDate getTaskStartDate() {
		return taskStartDate.get();
	}
	
	public ObjectProperty<LocalDate> taskStartDateProperty() {
		return taskStartDate;
	}
	
	public LocalDate getTaskEndDate() {
		return taskEndDate.get();
	}
	
	public ObjectProperty<LocalDate> taskEndDateProperty() {
		return taskEndDate;
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
		return task.getTaskStartTime();
	}
	
	public ObjectProperty<LocalTime> taskStartTimeProperty() {
		return taskStartTime;
	}
	
	public LocalTime getTaskEndTime() {
		return task.getTaskEndTime();
	}
	
	public ObjectProperty<LocalTime> taskEndTimeProperty() {
		return taskEndTime;
	}
}
