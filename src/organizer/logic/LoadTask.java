package organizer.logic;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import organizer.storage.Storage;

public class LoadTask {
	private final static String MESSAGE_LOAD_SUCCESS= "Loaded %1$s successfully!";
	private final static String MESSAGE_NEWFILE_SUCCESS= "File not found. %1$s has been created successfully!";
	private final static String FILE_TEMP_STORAGE = "tasklists.tmp";
	ResultSet returnResult = new ResultSet();
	Storage tempStorage = new Storage();
	
	public ResultSet execute(TaskListSet allLists, String fileName) throws IOException {
		if(!allLists.getTaskList().isEmpty()) {
			File tempFile = new File(fileName);
			tempFile.createNewFile();
			
			if(!tempFile.exists()) {
				PrintWriter writer = new PrintWriter(tempFile);
				writer.print("");
				writer.close();
			} else {
				tempStorage.writeFile(allLists.getTaskList(), FILE_TEMP_STORAGE);
			}
			
		}
		
		File userFile = new File(fileName);
		userFile.createNewFile();
		
		if(!userFile.exists()) {
			returnResult.setOpStatus(String.format(MESSAGE_NEWFILE_SUCCESS, fileName));
			allLists.setTaskList(null);
		} else {
			returnResult.setOpStatus(String.format(MESSAGE_LOAD_SUCCESS, fileName));
			allLists.setTaskList(tempStorage.readFile(fileName));
		}
		
		returnResult.setReturnList(allLists.getTaskList());
		return returnResult;
	}
	
	
}
