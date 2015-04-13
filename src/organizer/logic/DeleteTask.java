package organizer.logic;

import java.util.ArrayList;
import java.util.logging.Logger;

import organizer.parser.CommandParser;

//@author A0098824N
public class DeleteTask {
	private static final Logger LOGGER = Logger.getLogger(DeleteTask.class.getName());
	private static final String MESSAGE_SUCCESS = "Delete task operation is successful!";
	private static final String MESSAGE_INVALID_TASK = "Selected task does not exists!";
	
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
		
		if(validOp.isValidTask(lineNum, allLists)) {
			int taskID = validOp.checkForTaskID(lineNum, allLists);
			assert taskID >= 0;
			removeFromTaskList(taskID, allLists.getTaskList());
			returnResult.setOpStatus(String.format(MESSAGE_SUCCESS));
			LOGGER.info(String.format("delete task with ID %d", taskID));
		} else {
			returnResult.setOpStatus(MESSAGE_INVALID_TASK);
		}
		
		returnResult.setReturnList(allLists.getTaskList());
		returnResult.setCommandType(CommandParser.COMMAND_TYPE.DELETE_TASK);
		return returnResult;
	}
	
	private void removeFromTaskList(int taskID, ArrayList<Task> taskList) {
		for(int i = 0; i < taskList.size(); i++) {
			if(taskList.get(i).getTaskID() == taskID) {
				taskList.remove(i);
				break;
			}
		}
	}

}
