package organizer.logic;

import java.util.ArrayList;

public class FloatTask {
	private static final String MESSAGE_SUCCESS = "Set float task(s) operation is successful!";
	private static final String MESSAGE_INVALID_TYPE = "Selected task is already a floating task!";
	private static final String MESSAGE_INVALID_TASK = "Selected task does not exists!";
	
	private static final String TYPE_DEADLINE = "DEADLINE";
	private static final String TYPE_FLOATING = "FLOATING";
	private static final String TYPE_TIMED = "TIMED";
	
	public ResultSet execute(String taskInfo, TaskListSet allLists, Validation validOp) {
		int lineNum = Integer.parseInt(taskInfo.trim());
		ResultSet returnResult = new ResultSet();
		
		if(validOp.isValidTask(lineNum, allLists)) {
			int taskID = validOp.checkForTaskID(lineNum, allLists);
			returnResult.setOpStatus(setTaskToFloat(taskID, allLists.getTaskList()));
		} else {
			returnResult.setOpStatus(MESSAGE_INVALID_TASK);
		}

		returnResult.setReturnList(allLists.getTaskList());
		return returnResult;
	}
	
	private String setTaskToFloat(int taskID, ArrayList<Task> taskList) {
		Task tempTask = new Task();
		String opStatus = "";
		
		for(int i = 0; i < taskList.size(); i++) {
			tempTask = taskList.get(i);
			if(tempTask.getTaskID() == taskID) {
				if(tempTask.getTaskType().equals(TYPE_DEADLINE) || tempTask.getTaskType().equals(TYPE_TIMED)) {
					tempTask.setTaskEndDate(null);
					tempTask.setTaskEndTime(null);
					tempTask.setTaskStartDate(null);
					tempTask.setTaskStartTime(null);
					tempTask.setTaskType(TYPE_FLOATING);
					opStatus = MESSAGE_SUCCESS;
				} else {
					opStatus = MESSAGE_INVALID_TYPE;
				}
			}
		}
		
		return opStatus;
	}
}
