package organizer.logic;

import java.util.ArrayList;

public class DeleteTask {
	private static final String MESSAGE_SUCCESS = "Delete task(s) operation is successful!\n\n";
	private static final String MESSAGE_INVALID_TASK = "Selected task does not exists!";
	
	public ResultSet execute(String taskInfo, TaskListSet allList, Validation validOp) {
		int lineNum = Integer.parseInt(taskInfo.trim());
		ResultSet returnResult = new ResultSet();
		
		if(validOp.isValidTask(lineNum, allList)) {
			int taskID = validOp.checkForTaskID(lineNum, allList);
			removeFromTaskList(taskID, allList.getTaskList());
			returnResult.setOpStatus(String.format(MESSAGE_SUCCESS));
		} else {
			returnResult.setOpStatus(MESSAGE_INVALID_TASK);
		}

		returnResult.setReturnList(allList.getTaskList());

		return returnResult;
	}
	
	private void removeFromTaskList(int taskID, ArrayList<Task> taskList) {
		for(int i = 0; i < taskList.size(); i++) {
			if(taskList.get(i).getTaskID() == taskID) {
				taskList.remove(i);
			}
		}
	}

}