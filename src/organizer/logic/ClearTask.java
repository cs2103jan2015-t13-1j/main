package organizer.logic;

import java.util.ArrayList;
import java.util.logging.Logger;

import organizer.parser.CommandParser;

//@author A0113871J
public class ClearTask {
	private static final Logger LOGGER = Logger.getLogger(ClearTask.class.getName());
	private static final String MESSAGE_EMPTY_LIST = "No task(s) found!";
	private static final String MESSAGE_SUCCESS = "Clear task(s) operation is successful!";
	
	public ResultSet execute(ArrayList<Task> taskList){
		ResultSet returnResult = new ResultSet();
		
		if(taskList.isEmpty()) {
			returnResult.setOpStatus(MESSAGE_EMPTY_LIST);
		} else {
			taskList.clear();
			returnResult.setOpStatus(String.format(MESSAGE_SUCCESS));
		}
		
		returnResult.setReturnList(taskList);
		returnResult.setCommandType(CommandParser.COMMAND_TYPE.CLEAR_TASK);
		
		LOGGER.info("cleared");
		return returnResult;
	}
}
