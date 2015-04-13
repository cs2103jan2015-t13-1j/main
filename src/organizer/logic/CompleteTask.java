package organizer.logic;

import java.util.ArrayList;
import java.util.logging.Logger;

import organizer.parser.CommandParser;

//@author A0113871J
public class CompleteTask {
	private static final Logger LOGGER = Logger.getLogger(CompleteTask.class.getName());
	private static final String MESSAGE_INVALID_TASK = "Selected task does not exists!";
	private static final String MESSAGE_SUCCESS = "Complete task(s) operation is successful!";
	private static final String STATUS_COMPLETE = "COMPLETE";
	
	public ResultSet execute(String taskInfo, TaskListSet allLists, Validation validOp) {
		int lineNum;
		try {
			lineNum = Integer.parseInt(taskInfo.trim());
		} catch (NumberFormatException e) {
			LOGGER.throwing(getClass().getName(), "execute", e);
			LOGGER.severe("Invalid number format");
			throw e;
		}
		ResultSet returnResult = new ResultSet();
		ArrayList<Task> tempList = allLists.getTaskList();
		
		if(validOp.isValidTask(lineNum, allLists)) {
			Task tempTask = null;
			int taskID = validOp.checkForTaskID(lineNum, allLists);
			assert taskID >= 0;
			for (Task task : tempList) {
				if (taskID == task.getTaskID()) {
					tempTask = task;
					task.setTaskStatus(STATUS_COMPLETE);
					returnResult.setOpStatus(MESSAGE_SUCCESS);
					LOGGER.info(String.format("mark task %s, id %d complete", task.getTaskName(), task.getTaskID()));
					break;
				}
			}
			assert tempTask != null;
		} else {
			returnResult.setOpStatus(MESSAGE_INVALID_TASK);
		}
		
		allLists.setTaskList(tempList);
		returnResult.setReturnList(allLists.getTaskList());
		returnResult.setCommandType(CommandParser.COMMAND_TYPE.COMPLETE_TASK);
		
		return returnResult;
	}
}
