package organizer.logic;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import organizer.parser.CommandParser;
import organizer.storage.Storage;

//@author A0098824N
public class LoadTask {
	private final static Logger LOGGER = Logger.getLogger(LoadTask.class.getName());
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
		try {
			recoverFile.createNewFile();
		} catch (SecurityException e) {
			LOGGER.throwing(getClass().getName(), "recoverTempList", e);
			LOGGER.severe("Security settings prevent creating a new recovery file");
			throw e;
		}
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
			File tempFile = new File(FILE_TEMP_STORAGE);
			try {
				tempFile.createNewFile();
			} catch (SecurityException e) {
				LOGGER.throwing(getClass().getName(), "execute", e);
				LOGGER.severe("Security settings prevent creating a new temporary file");
				throw e;
			}
			
			if(!tempFile.exists()) {
				PrintWriter writer = new PrintWriter(FILE_TEMP_STORAGE);
				writer.print("");
				writer.close();
			} else {
				tempStorage.writeFile(allLists.getTaskList(), FILE_TEMP_STORAGE);
			}
			
		}
		
		File userFile = new File(fileName);
		if(!fileName.equals(FILE_TEMP_STORAGE)) {
			try {
				userFile.createNewFile();
			} catch (SecurityException e) {
				LOGGER.throwing(getClass().getName(), "recoverTempList", e);
				LOGGER.severe("Security settings prevent creating a new user storage");
				throw e;
			}
			
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
		returnResult.setCommandType(CommandParser.COMMAND_TYPE.LOAD);
		return returnResult;
	}
	
	
}
