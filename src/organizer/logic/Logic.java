//@author A0098824N
package organizer.logic;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Stack;

import organizer.storage.Storage;

public class Logic {
	private final static String MODE_INIT_VIEW = "all";
	Storage tempStorage = new Storage();
	ResultSet returnResult = new ResultSet();
	TaskListSet allLists = new TaskListSet();
	Validation validOp = new Validation();
	UndoCommand undoOp = new UndoCommand();
	boolean isDefaultFile = true;
	String userFile = "";
	Stack<ArrayList<Task>> undoList = new Stack<ArrayList<Task>>();
	
	public ArrayList<Task> loadStorage(InputStream in) throws IOException {
		allLists.setTaskList(tempStorage.readFromStream(in));
		return viewDefault();
	}

	public ArrayList<Task> loadStorage() throws IOException {
		allLists.setTaskList(tempStorage.readFile());
		validOp.setIsView(false);
		validOp.setIsSearch(false);
		return viewDefault();
	}

	public void writeStorage() throws IOException {
		tempStorage.writeFile(allLists.getTaskList());
	}
	
	public ArrayList<Task> viewDefault() {
		ViewTask command = new ViewTask();
		if(validOp.getIsView()) {
			allLists.setInitList(allLists.getViewList());
		} else if(validOp.getIsSearch()) {
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
		return returnResult;
	}
	
	public ResultSet deleteCommand(String taskInfo) {
		addToUndoList(allLists.getTaskList());
		DeleteTask command = new DeleteTask();
		returnResult = command.execute(taskInfo, allLists, validOp);
		setViewMode();
		return returnResult;
	}
	
	public ResultSet searchCommand(String searchTerm) {
		validOp.setIsSearch(true);
		SearchTask command = new SearchTask();
		returnResult = command.execute(searchTerm, allLists);
		return returnResult;
	}
	
	public ResultSet viewCommand(String viewType) {
		validOp.setIsView(true);
		ViewTask command = new ViewTask();
		returnResult = command.execute(viewType, allLists);
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
		returnResult = command.execute(allLists, undoList);
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
		if(isDefaultFile) {
			returnResult = command.executeSave(allLists.getTaskList());
		} else {
			returnResult = command.executeSaveAs(allLists.getTaskList(), userFile);
		}
		setViewMode();
		return returnResult;
	}
	
	public ResultSet saveAsCommand(String fileName) throws IOException {
		SaveTask command = new SaveTask();
		returnResult = command.executeSaveAs(allLists.getTaskList(), fileName);
		setViewMode();
		isDefaultFile = false;
		userFile = fileName;
		return returnResult;
	}

	private void setViewMode() {
		returnResult.setReturnList(viewDefault());
		validOp.setIsView(false);
		validOp.setIsSearch(false);
	}
	
	public ResultSet loadFileCommand(String fileName) throws IOException {
		LoadTask command = new LoadTask();
		returnResult = command.execute(allLists, fileName);
		returnResult.setReturnList(viewDefault());
		isDefaultFile = false;
		userFile = fileName;
		return returnResult;
	}
	
	public ResultSet recoverFileCommand() throws IOException {
		LoadTask command = new LoadTask();
		returnResult = command.recoverTempList(allLists);
		returnResult.setReturnList(viewDefault());
		return returnResult;
	}
}