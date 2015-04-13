package organizer.logic;

import java.util.ArrayList;
import java.util.logging.Logger;

import organizer.parser.CommandParser;

//@author A0098824N
public class FloatTask {
	private static final Logger LOGGER = Logger.getLogger(FloatTask.class.getName());
	private static final String MESSAGE_SUCCESS = "Set float task operation is successful!";
	private static final String MESSAGE_INVALID_TYPE = "Selected task is already a floating task!";
	private static final String MESSAGE_INVALID_TASK = "Selected task does not exists!";

	private static final String TYPE_DEADLINE = "DEADLINE";
	private static final String TYPE_FLOATING = "FLOATING";
	private static final String TYPE_TIMED = "TIMED";

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
			returnResult.setOpStatus(setTaskToFloat(taskID, allLists.getTaskList()));
		} else {
			returnResult.setOpStatus(MESSAGE_INVALID_TASK);
		}

		returnResult.setReturnList(allLists.getTaskList());
		returnResult.setCommandType(CommandParser.COMMAND_TYPE.FLOAT_TASK);
		return returnResult;
	}

	private String setTaskToFloat(int taskID, ArrayList<Task> taskList) {
		Task tempTask = new Task();
		String opStatus = "";

		for(int i = 0; i < taskList.size(); i++) {
			tempTask = taskList.get(i);
			opStatus = setDateTimeInfo(taskID, tempTask, opStatus);
		}

		return opStatus;
	}

	private String setDateTimeInfo(int taskID, Task tempTask, String opStatus) {
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
		return opStatus;
	}
}
