package organizer.logic;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import organizer.storage.Storage;

public class LoadTask {
	private final static String MESSAGE_LOAD_SUCCESS= "Loaded %1$s successfully!";
	private final static String MESSAGE_NEWFILE_SUCCESS= "File not found. %1$s has been created successfully!";
	private final static String FILE_TEMP_STORAGE = "tasklists.tmp";
	private final static String MESSAGE_TEMPFILE_ACCESS = "Unable to load file! Please check file name!";
	private final static String MESSAGE_TEMPFILE_NOTFOUND = "Nothing to recover!";
	private final static String MESSAGE_TEMPFILE_FOUND = "Tasklist recovered!";
	
	ResultSet returnResult = new ResultSet();
	Storage tempStorage = new Storage();
	
	public ResultSet recoverTempList(TaskListSet allLists) throws IOException {
		File recoverFile = new File(FILE_TEMP_STORAGE);
		recoverFile.createNewFile();
		if(recoverFile.exists()) {
			allLists.setTaskList(tempStorage.readFile(FILE_TEMP_STORAGE));
			returnResult.setOpStatus(MESSAGE_TEMPFILE_FOUND);
		} else {
			returnResult.setOpStatus(MESSAGE_TEMPFILE_NOTFOUND);
		}
		
		returnResult.setReturnList(allLists.getTaskList());
		return returnResult;
	}
	
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
		if(!fileName.equals(FILE_TEMP_STORAGE)) {
			userFile.createNewFile();
			
			if(!userFile.exists()) {
				returnResult.setOpStatus(String.format(MESSAGE_NEWFILE_SUCCESS, fileName));
				allLists.setTaskList(null);
			} else {
				returnResult.setOpStatus(String.format(MESSAGE_LOAD_SUCCESS, fileName));
				allLists.setTaskList(tempStorage.readFile(fileName));
			}
		} else {
			returnResult.setOpStatus(MESSAGE_TEMPFILE_ACCESS);
		}
		
		returnResult.setReturnList(allLists.getTaskList());
		return returnResult;
	}
	
	
}
