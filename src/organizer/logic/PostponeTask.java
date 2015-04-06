package organizer.logic;

import java.time.LocalDate;
import java.util.ArrayList;

public class PostponeTask {
	private static final String MESSAGE_SUCCESS = "Postpone task(s) operation is successful!\n\n";
	private static final String MESSAGE_INVALID_TASK = "Selected task does not exists!";

	public ResultSet execute(String taskInfo, TaskListSet allLists, Validation validOp) {	
		int lineNum = Integer.parseInt(taskInfo.trim());
		ArrayList<Task> tempList = new ArrayList<Task>();
		ResultSet returnResult = new ResultSet();

		if(validOp.isValidTask(lineNum, allLists)) {
			tempList = allLists.getTaskList();
			LocalDate newDueDate = tempList.get(lineNum - 1).getTaskEndDate().plusDays(1);
			tempList.get(lineNum - 1).setTaskEndDate(newDueDate);
			allLists.setTaskList(tempList);
			returnResult.setOpStatus(String.format(MESSAGE_SUCCESS));
		} else {
			returnResult.setOpStatus(MESSAGE_INVALID_TASK);
		}

		returnResult.setReturnList(allLists.getTaskList());
		return returnResult;
	}
}
