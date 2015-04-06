package organizer.logic;

import java.io.IOException;

import organizer.storage.Storage;

public class LoadTask {
	private final static String MESSAGE_LOAD_SUCCESS= "Loaded %1$s successfully!";
	private final static String MESSAGE_NEWFILE_SUCCESS= "File not found. %1$s has been created successfully!";
	ResultSet returnResult = new ResultSet();
	Storage tempStorage = new Storage();
	
	public ResultSet execute(TaskListSet allLists, String fileName) throws IOException {
		allLists.setTaskList(tempStorage.readFile(fileName));
		if(allLists.getTaskList().isEmpty()) {
			returnResult.setOpStatus(String.format(MESSAGE_NEWFILE_SUCCESS, fileName));
		} else {
			returnResult.setOpStatus(String.format(MESSAGE_LOAD_SUCCESS, fileName));
		}
		returnResult.setReturnList(allLists.getTaskList());
		return returnResult;
	}
	
	
}
