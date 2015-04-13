package organizer.logic;

import java.util.ArrayList;
import java.util.logging.Logger;

import organizer.parser.CommandParser;

//@author A0113871J
public class IncompleteTask {
	private static final Logger LOGGER = Logger.getLogger(IncompleteTask.class.getName());
    private static final String MESSAGE_INVALID_TASK = "Selected task does not exists!";
    private static final String MESSAGE_SUCCESS = "Revert completed task operation is successful!";
    private static final String STATUS_TASK = "INCOMPLETE";
    
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
			int taskID = validOp.checkForTaskID(lineNum, allLists);
			assert taskID >= 0;
			for(Task task : tempList) {
				if(taskID == task.getTaskID()) {
					task.setTaskStatus(STATUS_TASK);
					returnResult.setOpStatus(String.format(MESSAGE_SUCCESS));
					break;
				}
			}
		} else {
			returnResult.setOpStatus(MESSAGE_INVALID_TASK);
		}
        
        allLists.setTaskList(tempList);
        returnResult.setReturnList(allLists.getTaskList());
        returnResult.setCommandType(CommandParser.COMMAND_TYPE.INCOMPLETE_TASK);
        return returnResult;
    }
}
