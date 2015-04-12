
package organizer.logic;

import java.time.LocalDate;
import java.time.LocalTime;

//@author A0113871J
public class Task {

	private final static String STATUS_DEFAULT = "INCOMPLETE";
	
	private int taskID = 0;
	private String taskName = null;
	private String taskStatus = null;
	private LocalDate taskStartDate = null;
	private LocalTime taskStartTime = null;
	private LocalDate taskEndDate = null;
	private LocalTime taskEndTime = null;
	private String taskPriority = null;
	private String taskType = null;


	/**
	 * Default constructor.
	 */
	public Task() {
		taskID = 0;
		taskName = null;
		taskStatus = STATUS_DEFAULT;
		taskStartDate = null;
		taskStartTime = null;
		taskEndDate = null;
		taskEndTime = null;
		taskPriority = null;
		taskType = null;
	}


	public Task(int taskID, String taskName, LocalDate taskStartDate, LocalTime taskStartTime, LocalDate taskEndDate, LocalTime taskEndTime, String taskType) {
		this.taskID = taskID;
		this.taskName = taskName;
		this.taskStatus = STATUS_DEFAULT;
		this.taskStartDate = taskStartDate;
		this.taskStartTime = taskStartTime;
		this.taskEndDate = taskEndDate;
		this.taskEndTime = taskEndTime;
		this.taskType = taskType;
		
	}
	
	public Task(int taskID, String taskName, LocalDate taskStartDate, LocalTime taskStartTime, LocalDate taskEndDate, LocalTime taskEndTime, String taskType, String taskPriority) {
		this(taskID, taskName, taskStartDate, taskStartTime, taskEndDate, taskEndTime, taskType);
		this.taskPriority = taskPriority;
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

	public LocalDate getTaskStartDate() {
		return taskStartDate;
	}
	
	public void setTaskStartDate(LocalDate taskStartDate) {
		this.taskStartDate = taskStartDate;
	}
	
	public LocalTime getTaskStartTime() {
		return taskStartTime;
	}
	
	public void setTaskStartTime(LocalTime taskStartTime) {
		this.taskStartTime = taskStartTime;
	}
	
	public LocalDate getTaskEndDate() {
		return taskEndDate;
	}
	
	public void setTaskEndDate(LocalDate taskEndDate) {
		this.taskEndDate = taskEndDate;
	}
	
	public LocalTime getTaskEndTime() {
		return taskEndTime;
	}
	
	public void setTaskEndTime(LocalTime taskEndTime) {
		this.taskEndTime = taskEndTime;
	}
	
	public String getTaskPriority() {
		return taskPriority;
	}

	public void setTaskPriority(String taskPriority) {
		this.taskPriority = taskPriority;
	}
	
	public String getTaskType() {
		return taskType;
	}
	
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	
	

}