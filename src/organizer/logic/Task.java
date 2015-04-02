package organizer.logic;

import java.time.LocalDate;
import java.time.LocalTime;

public class Task {

	private int taskID = 0;
	private String taskName = null;
	private String taskStatus = null;
	private LocalDate dueDate = null;
	private LocalTime startTime = null;
	private LocalTime endTime = null;
	private String taskPriority = null;
	private LocalDate startDate = null;


	/**
	 * Default constructor.
	 */
	public Task() {
		taskID = 0;
		taskName = null;
		taskStatus = null;
		dueDate = null;
		startTime = null;
		endTime = null;
		taskPriority = null;
	}


	public Task(int taskID, String taskName, String taskStatus, LocalDate dueDate) {
		this.taskID = taskID;
		this.taskName = taskName;
		this.taskStatus = taskStatus;
		this.dueDate = dueDate;
	}

	/**
	 * Get/Set methods for task attributes.
	 */
	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	
	public LocalTime getStartTime() {
		return startTime;
	}
	
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	
	public LocalTime getEndTime() {
		return endTime;
	}
	
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
	

	public String getTaskPriority() {
		return taskPriority;
	}

	public void setTaskPriority(String taskPriority) {
		this.taskPriority = taskPriority;
	}


	public void setStartDate(LocalDate startDate) {
		this.startDate  = startDate;
	}


	public LocalDate getStartDate() {
		return startDate;
	}

}