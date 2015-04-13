package organizer.logic;

//@author A0098824N
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import organizer.parser.CommandParser;
import organizer.storage.Storage;

public class SaveTask {
	private final static Logger LOGGER = Logger.getLogger(SaveTask.class.getName());
	private final static String MESSAGE_SAVE_SUCCESS= "Task list saved to file storage.txt successfully!";
	private final static String MESSAGE_SAVEAS_SUCCESS= "Task list saved to file %1$s successfully!";

	ResultSet returnResult = new ResultSet();
	Storage tempStorage = new Storage();
	
	public ResultSet executeSave(ArrayList<Task> taskList) throws IOException {
		try {
			tempStorage.writeFile(taskList);
		} catch (IOException e) {
			LOGGER.throwing(getClass().getName(), "executeSaveAs", e);
			LOGGER.severe("Failed to save into default file");
			throw e;
		}
		returnResult.setOpStatus(MESSAGE_SAVE_SUCCESS);
		returnResult.setReturnList(taskList);
		returnResult.setCommandType(CommandParser.COMMAND_TYPE.SAVE);
		return returnResult;
	}
	
	public ResultSet executeSaveAs(ArrayList<Task> taskList, String fileName) throws IOException {
		try {
			tempStorage.writeFile(taskList, fileName);
		} catch (IOException e) {
			LOGGER.throwing(getClass().getName(), "executeSaveAs", e);
			LOGGER.severe("Failed to save as a file");
			throw e;
		}
		returnResult.setOpStatus(String.format(MESSAGE_SAVEAS_SUCCESS, fileName));
		returnResult.setReturnList(taskList);
		returnResult.setCommandType(CommandParser.COMMAND_TYPE.SAVEAS);
		return returnResult;
	}
	
	
}
