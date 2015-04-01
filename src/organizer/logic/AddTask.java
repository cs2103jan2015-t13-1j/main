package organizer.logic;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class AddTask {
	private static final String dateFieldIdentifier = "%";
	private static final String timeFieldIdentifier = "@";
	private static final String timeFieldSeparator = "-";
	
	private static final String MESSAGE_SUCCESS = "Add task(s) operation is successful!\n\n";
	private static final String MESSAGE_UNSUCCESS = "Add task(s) operation is unsuccessful!\n\n";
	private static final long TASK_DURATION = 1;
	private DateAndTime dateTimeCheck = new DateAndTime();
	
	public ResultSet execute(String taskInfo, ArrayList<Task> taskList) {
		String taskName = null;
		String taskDate = null;
		String taskStartTime = null;
		String taskEndTime = null;
		String taskTime = null;
		LocalDate dueDate = LocalDate.now();
		
		LocalTime startTime = null;
		LocalTime endTime = null;
		Task tempTask = new Task();
		ResultSet returnResult = new ResultSet();
		String dateTime = null;
		
		if(taskInfo.contains(dateFieldIdentifier)) {
			taskName = taskInfo.substring(0,taskInfo.indexOf(dateFieldIdentifier));
			dateTime = taskInfo.substring(taskInfo.indexOf(dateFieldIdentifier)+1);
			
			//only with date then we have time
			if(dateTime.contains(timeFieldIdentifier)) {
				taskDate = dateTime.substring(0, dateTime.indexOf(timeFieldIdentifier));
				taskTime = dateTime.substring(dateTime.indexOf(timeFieldIdentifier)+1);
				
				//by default, no end time means is 1 hour later
				if(taskTime.contains(timeFieldSeparator)) {
					taskStartTime = taskTime.substring(0, taskTime.indexOf(timeFieldSeparator));
					taskEndTime = taskTime.substring(taskTime.indexOf(timeFieldSeparator)+1);
					startTime = dateTimeCheck.determineTime(taskStartTime);
					endTime = dateTimeCheck.determineTime(taskEndTime);
					if(startTime == null || endTime == null) {
						returnResult.setOpStatus(MESSAGE_UNSUCCESS);
						return returnResult;
					}
				} else {
					startTime = dateTimeCheck.determineTime(taskTime);
					if(startTime == null) {
						returnResult.setOpStatus(MESSAGE_UNSUCCESS);
						return returnResult;
					}
					endTime = startTime.plusHours(TASK_DURATION);
				}
				
				dueDate = dateTimeCheck.determineDate(taskDate);
				if(dueDate == null) {
					returnResult.setOpStatus(MESSAGE_UNSUCCESS);
					return returnResult;
				}
				
			} else {
				taskDate = dateTime;
				dueDate = dateTimeCheck.determineDate(taskDate);
				if(dueDate == null) {
					returnResult.setOpStatus(MESSAGE_UNSUCCESS);
					return returnResult;
				}
			}
		} else {
			taskName = taskInfo;
			dueDate = null;
			startTime = null;
			endTime = null;
		}
	

		
		tempTask.setTaskName(taskName);
		tempTask.setDueDate(dueDate);
		tempTask.setStartTime(startTime);
		tempTask.setEndTime(endTime);
		tempTask.setTaskStatus("INCOMPLETE");
		tempTask.setTaskPriority(null);
		tempTask.setTaskID(taskList.size());

		taskList.add(tempTask);
		tempTask = new Task();

		returnResult.setOpStatus(String.format(MESSAGE_SUCCESS));
		returnResult.setReturnList(taskList);

		return returnResult;
	}
}
