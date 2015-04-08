package organizer.logic;

//@author A0098824N
import java.io.IOException;
import java.util.ArrayList;

import organizer.storage.Storage;

public class SaveTask {
	private final static String MESSAGE_SAVE_SUCCESS= "Task list saved to file storage.txt successfully!";
	private final static String MESSAGE_SAVEAS_SUCCESS= "Task list saved to file %1$s successfully!";

	ResultSet returnResult = new ResultSet();
	Storage tempStorage = new Storage();
	
	public ResultSet executeSave(ArrayList<Task> taskList) throws IOException {
		tempStorage.writeFile(taskList);
		returnResult.setOpStatus(MESSAGE_SAVE_SUCCESS);
		returnResult.setReturnList(taskList);
		return returnResult;
	}
	
	public ResultSet executeSaveAs(ArrayList<Task> taskList, String fileName) throws IOException {
		tempStorage.writeFile(taskList, fileName);
		returnResult.setOpStatus(String.format(MESSAGE_SAVEAS_SUCCESS, fileName));
		returnResult.setReturnList(taskList);
		return returnResult;
	}
	
	
}
