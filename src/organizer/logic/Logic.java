
package organizer.logic;

//@author A0098824N
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Stack;

import organizer.storage.Storage;

public class Logic {
	private static final String MODE_INIT_VIEW = "all";
	private static final String opView = "View Tasks Filter: %1$s \nStatus: ";
	
	String MODE_VIEW = "all";
	String TERM_SEARCH = "";
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
		validOp.setIsSearch(false);
		validOp.setIsView(false);
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
		if(!viewType.equals(MODE_INIT_VIEW)) {
			MODE_VIEW = viewType;
		}
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
		String operationMessage = returnResult.getOpStatus();
		returnResult.setReturnList(viewDefault());
		returnResult.setOpStatus(String.format(opView, MODE_VIEW).concat(operationMessage));
	}
	
	public ResultSet loadFileCommand(String fileName) throws IOException {
		LoadTask command = new LoadTask();
		returnResult = command.execute(allLists, fileName);
		returnResult.setReturnList(viewDefault());
		validOp.setIsSearch(false);
		validOp.setIsView(false);
		isDefaultFile = false;
		userFile = fileName;
		return returnResult;
	}
	
	public ResultSet recoverFileCommand() throws IOException {
		LoadTask command = new LoadTask();
		returnResult = command.recoverTempList(allLists);
		returnResult.setReturnList(viewDefault());
		validOp.setIsSearch(false);
		validOp.setIsView(false);
		return returnResult;
	}
}