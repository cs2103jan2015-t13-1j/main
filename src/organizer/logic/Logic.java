
package organizer.logic;

//@author A0098824N
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import organizer.storage.Storage;

public class Logic {
	private final static String MODE_INIT_VIEW = "all";
	private final static String OP_VIEW = "View Tasks Filter: %1$s \nStatus: ";
	private final static String LAST_SAVED_FILENAME = "lastSaved.tmp";
	private final static String LOGGER_FileStatus = "Currently working on %1$s";
	private final static Logger LOGGER_Logic= Logger.getLogger(Logic.class.getName()); 
	public static String MODE_VIEW = "all";

	String TERM_SEARCH = "";
	Storage tempStorage = new Storage();
	ResultSet returnResult = new ResultSet();
	TaskListSet allLists = new TaskListSet();
	Validation validOp = new Validation();
	UndoCommand undoOp = new UndoCommand();
	boolean isDefaultFile = true;
	String userFile = "";
	String defaultFileName = "storage.txt";

	Stack<ArrayList<Task>> undoList = new Stack<ArrayList<Task>>();

	public ArrayList<Task> loadStorage(InputStream in) throws IOException {
		allLists.setTaskList(tempStorage.readFromStream(in));
		validOp.setIsSearch(false);
		validOp.setIsView(false);
		return viewDefault();
	}

	public ArrayList<Task> loadStorage() throws IOException {
		File tempFile = new File(LAST_SAVED_FILENAME);

		try {
			Scanner sc = new Scanner(tempFile);
			while(sc.hasNextLine()) {
				String lastSavedName = sc.nextLine();
				allLists.setTaskList(tempStorage.readFile(lastSavedName));
				LOGGER_Logic.log(Level.INFO, String.format(LOGGER_FileStatus, lastSavedName));
			}
			
			sc.close();
			tempFile.delete();

		} catch (FileNotFoundException e) {
			allLists.setTaskList(tempStorage.readFile());
			LOGGER_Logic.log(Level.INFO, String.format(LOGGER_FileStatus, defaultFileName));
		}

		validOp.setIsView(false);
		validOp.setIsSearch(false);
		return viewDefault();
	}

	public void writeStorage() throws IOException {
		tempStorage.writeFile(allLists.getTaskList());
	}

	public void writeStorage(OutputStream out) throws IOException {
		tempStorage.writeFileToStream(allLists.getTaskList(), out);
	}

	public ArrayList<Task> viewDefault() {
		ViewTask command = new ViewTask();
		if(validOp.getIsView()) {
			allLists.setViewList(viewCommand(MODE_VIEW).getReturnList());
			allLists.setInitList(allLists.getViewList());
		} else if(validOp.getIsSearch()) {
			allLists.setResultList(searchCommand(TERM_SEARCH).getReturnList());
			allLists.setInitList(allLists.getResultList());
		} else {
			ResultSet returnResult = command.execute(MODE_INIT_VIEW, allLists);
			allLists.setInitList(returnResult.getReturnList());
		}

		return allLists.getInitList();
	}

	public ResultSet addCommand(String taskInfo) {
		addToUndoList(allLists.getTaskList());
		AddTask command = new AddTask();
		returnResult = command.execute(taskInfo, allLists);
		validOp.setIsSearch(false);
		validOp.setIsView(false);
		MODE_VIEW = "all";
		setViewMode();
		return returnResult;
	}

	public ResultSet deleteCommand(String taskInfo) {
		addToUndoList(allLists.getTaskList());
		DeleteTask command = new DeleteTask();
		returnResult = command.execute(taskInfo, allLists, validOp);
		System.out.println(MODE_VIEW);
		setViewMode();
		return returnResult;
	}

	public ResultSet searchCommand(String searchTerm) {
		SearchTask command = new SearchTask();
		returnResult = command.execute(searchTerm, allLists);
		validOp.setIsSearch(true);
		TERM_SEARCH = searchTerm;
		return returnResult;
	}

	public ResultSet viewCommand(String viewType) {
		ViewTask command = new ViewTask();
		returnResult = command.execute(viewType, allLists);
		validOp.setIsView(true);
		MODE_VIEW = viewType;

		return returnResult;
	}

	public ResultSet postponeCommand(String taskInfo) {
		addToUndoList(allLists.getTaskList());
		PostponeTask command = new PostponeTask();
		returnResult = command.execute(taskInfo, allLists, validOp);
		setViewMode();
		return returnResult;
	}

	public ResultSet clearCommand() {
		addToUndoList(allLists.getTaskList());
		ClearTask command = new ClearTask();
		returnResult = command.execute(allLists.getTaskList());
		return returnResult;
	}

	public ResultSet rankCommand(String taskInfo) {
		addToUndoList(allLists.getTaskList());
		RankTask command = new RankTask();
		returnResult = command.execute(taskInfo, allLists, validOp);
		setViewMode();
		return returnResult;
	}

	public ResultSet completeCommand(String taskInfo) {
		addToUndoList(allLists.getTaskList());
		CompleteTask command = new CompleteTask();
		returnResult = command.execute(taskInfo, allLists, validOp);
		setViewMode();
		return returnResult;	
	}

	public ResultSet incompleteCommand(String taskInfo) {
		addToUndoList(allLists.getTaskList());
		IncompleteTask command = new IncompleteTask();
		returnResult = command.execute(taskInfo, allLists, validOp);
		setViewMode();
		return returnResult;	
	}

	public ResultSet editCommand(String userContent) {
		addToUndoList(allLists.getTaskList());
		EditTask command = new EditTask();
		returnResult = command.execute(userContent, allLists, validOp);
		setViewMode();
		return returnResult;
	}

	public ResultSet undoCommand() {
		UndoCommand command = new UndoCommand();
		returnResult = command.execute(allLists, undoList, validOp.getIsView(), validOp.getIsSearch());
		setViewMode();
		return returnResult;
	}

	public ResultSet floatCommand(String userContent) {
		addToUndoList(allLists.getTaskList());
		FloatTask command = new FloatTask();
		returnResult = command.execute(userContent, allLists, validOp);
		setViewMode();
		return returnResult;
	}

	public void addToUndoList(ArrayList<Task> taskList) {
		ArrayList<Task> tempTaskList = new ArrayList<Task>();
		for(int index = 0; index < taskList.size(); index++) {
			Task tempTask = new Task();
			tempTask.setTaskStartDate(taskList.get(index).getTaskStartDate());
			tempTask.setTaskType(taskList.get(index).getTaskType());
			tempTask.setTaskEndDate(taskList.get(index).getTaskEndDate());
			tempTask.setTaskEndTime(taskList.get(index).getTaskEndTime());
			tempTask.setTaskStartTime(taskList.get(index).getTaskStartTime());
			tempTask.setTaskID(taskList.get(index).getTaskID());
			tempTask.setTaskName(taskList.get(index).getTaskName());
			tempTask.setTaskPriority(taskList.get(index).getTaskPriority());
			tempTask.setTaskStatus(taskList.get(index).getTaskStatus());
			tempTaskList.add(tempTask);
		}
		undoList.push(new ArrayList<Task>(tempTaskList));
	}

	public ResultSet saveCommand() throws IOException {
		SaveTask command = new SaveTask();
		String lastSaveFileName = "";

		if(isDefaultFile) {
			returnResult = command.executeSave(allLists.getTaskList());
			lastSaveFileName = defaultFileName;
		} else {
			returnResult = command.executeSaveAs(allLists.getTaskList(), userFile);
			lastSaveFileName = userFile;
		}

		createlastSaveTempFile(lastSaveFileName);

		setViewMode();
		return returnResult;
	}

	public ResultSet saveAsCommand(String fileName) throws IOException {
		SaveTask command = new SaveTask();
		returnResult = command.executeSaveAs(allLists.getTaskList(), fileName);
		setViewMode();
		isDefaultFile = false;
		userFile = fileName;
		createlastSaveTempFile(userFile);
		return returnResult;
	}

	private void setViewMode() {
		String operationMessage = returnResult.getOpStatus();
		returnResult.setReturnList(viewDefault());
		returnResult.setOpStatus(String.format(OP_VIEW, MODE_VIEW).concat(operationMessage));
	}

	public ResultSet setViewMode(ResultSet returnResult) {
		String operationMessage = returnResult.getOpStatus();
		returnResult.setReturnList(viewDefault());
		returnResult.setOpStatus(String.format(OP_VIEW, MODE_VIEW).concat(operationMessage));
		return returnResult;
	}

	public ResultSet loadFileCommand(String fileName) throws IOException {
		LoadTask command = new LoadTask();
		returnResult = command.execute(allLists, fileName);
		validOp.setIsSearch(false);
		validOp.setIsView(false);
		returnResult.setReturnList(viewDefault());
		MODE_VIEW = "all";
		isDefaultFile = false;
		userFile = fileName;
		setViewMode();
		return returnResult;
	}

	public void createlastSaveTempFile(String lastSaveFileName) throws IOException {
		PrintWriter writer = new PrintWriter(LAST_SAVED_FILENAME);
		writer.print(lastSaveFileName);
		writer.close();
	}

}