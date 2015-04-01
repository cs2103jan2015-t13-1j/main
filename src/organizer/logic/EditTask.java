package organizer.logic;

import java.time.LocalDate;
import java.util.ArrayList;

public class EditTask {
	private static final String MESSAGE_INVALID_TASK = "Selected task does not exists!";
	private static final String MESSAGE_INVALID_CONTENT = "Edit task operation failed for invalid content!";
	private static final String MESSAGE_SUCCESS = "Edit task(s) operation is successful!\n\n";

	private static final String dateFieldIdentifier = "%";
	
	DateAndTime dateTime = new DateAndTime();

	public ResultSet execute(String userContent, TaskListSet allLists, Validation validOp) {
		int lineNum = Integer.parseInt(userContent.substring(0, userContent.indexOf(" ")));
		ArrayList<Task> tempList = new ArrayList<Task>();
		tempList = allLists.getTaskList();
		ResultSet returnResult = new ResultSet();

		if(validOp.isValidTask(lineNum, allLists)) {
			int taskID = validOp.checkForTaskID(lineNum, allLists);
			String editContent = userContent.substring(userContent.indexOf(" "));
			if(editContent.indexOf(" ") >= 0) {
				if(editContent.contains(dateFieldIdentifier)) {
					LocalDate dueDate = dateTime.determineDate(editContent.substring(editContent.indexOf(dateFieldIdentifier)+1));
					tempList.get(taskID).setDueDate(dueDate);
				} else {
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
