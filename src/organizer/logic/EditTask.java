package organizer.logic;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class EditTask {
	private static final String MESSAGE_INVALID_TASK = "Selected task does not exists!";
	private static final String MESSAGE_INVALID_CONTENT = "Edit task operation failed for invalid content!";
	private static final String MESSAGE_SUCCESS = "Edit task(s) operation is successful!\n\n";

	private static final String dateFieldIdentifier = "%";
	private static final String timeFieldIdentifier = "@";
	private static final String timeFieldSeparator = "-";
	private static final long TASK_DURATION = 1;

	DateAndTime dateTime = new DateAndTime();

	public ResultSet execute(String userContent, TaskListSet allLists, Validation validOp) {
		int lineNum = Integer.parseInt(userContent.substring(0, userContent.indexOf(" ")));
		ArrayList<Task> tempList = new ArrayList<Task>();
		tempList = allLists.getTaskList();
		ResultSet returnResult = new ResultSet();
		String taskStartTime = null;
		String taskEndTime = null;
		DateAndTime dateTimeCheck = new DateAndTime();
		LocalTime startTime = null;
		LocalTime endTime = null;

		if(validOp.isValidTask(lineNum, allLists)) {
			int taskID = validOp.checkForTaskID(lineNum, allLists);
			String editContent = userContent.substring(userContent.indexOf(" "));
			if(editContent.indexOf(" ") >= 0) {
				if(editContent.contains(dateFieldIdentifier)) {
					LocalDate dueDate = dateTime.determineDate(editContent.substring(editContent.indexOf(dateFieldIdentifier)+1));
					tempList.get(taskID).setDueDate(dueDate);
				} else if(editContent.contains(timeFieldIdentifier)) {
					String timeInfo = editContent.substring(editContent.indexOf(timeFieldIdentifier)+1);
					if(timeInfo.contains(timeFieldSeparator)) {
						taskStartTime = timeInfo.substring(0, timeInfo.indexOf(timeFieldSeparator));
						taskEndTime = timeInfo.substring(timeInfo.indexOf(timeFieldSeparator)+1);
						startTime = dateTimeCheck.determineTime(taskStartTime);
						endTime = dateTimeCheck.determineTime(taskEndTime);
						if((startTime == null && endTime == null) || (endTime != null && tempList.get(taskID).getStartTime() == null)) {
							returnResult.setOpStatus(MESSAGE_INVALID_CONTENT);
							return returnResult;
						} else if(endTime != null && tempList.get(taskID).getStartTime() != null) {
							tempList.get(taskID).setEndTime(endTime);
						} else if(startTime != null && tempList.get(taskID).getEndTime() != null) {
							tempList.get(taskID).setStartTime(startTime);
						} else if(startTime != null && tempList.get(taskID).getEndTime() == null){
							tempList.get(taskID).setStartTime(startTime);
							tempList.get(taskID).setEndTime(startTime.plusHours(TASK_DURATION));
						} else {
							tempList.get(taskID).setStartTime(startTime);
							tempList.get(taskID).setEndTime(endTime);
						} 
					} else {
						startTime = dateTimeCheck.determineTime(timeInfo);
						if(startTime == null) {
							returnResult.setOpStatus(MESSAGE_INVALID_CONTENT);
							return returnResult;
						} else {
							tempList.get(taskID).setStartTime(startTime);
						}
					}

				}
				else {
					tempList.get(taskID).setTaskName(editContent.substring(1));
				}
				returnResult.setOpStatus(String.format(MESSAGE_SUCCESS));
			} else {
				returnResult.setOpStatus(MESSAGE_INVALID_CONTENT);
			}

		} else {
			returnResult.setOpStatus(MESSAGE_INVALID_TASK);
		}

		allLists.setTaskList(tempList);
		returnResult.setReturnList(allLists.getTaskList());

		return returnResult;
	}
}
