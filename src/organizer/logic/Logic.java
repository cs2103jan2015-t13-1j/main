package organizer.logic;


import java.io.IOException;
import java.util.ArrayList;


import java.util.Stack;

import organizer.storage.Storage;

public class Logic {
	private final static String MODE_INIT_VIEW = "incomplete";
	
	Storage tempStorage = new Storage();
	ResultSet returnResult = new ResultSet();
	TaskListSet allLists = new TaskListSet();
	Validation validOp = new Validation();
	UndoCommand undoOp = new UndoCommand();
	Stack<ArrayList<Task>> undoList = new Stack<ArrayList<Task>>();

	public ArrayList<Task> loadStorage() throws IOException {
		allLists.setTaskList(tempStorage.readFile());
		return viewDefault();
	}

	public void writeStorage() throws IOException {
		tempStorage.writeFile(allLists.getTaskList());
	}
	
	//to allow program to display "incomplete" tasklist even after adding/deleting tasks
	public ArrayList<Task> viewDefault() {
		validOp.setIsView(true);
		ViewTask command = new ViewTask();
		ResultSet returnResult = command.execute(MODE_INIT_VIEW, allLists);
		allLists.setInitList(returnResult.getReturnList());
		return allLists.getInitList();
	}
	
	public ResultSet addCommand(String taskInfo) {
		addToUndoList(allLists.getTaskList());
		AddTask command = new AddTask();
		returnResult = command.execute(taskInfo, allLists.getTaskList());
		returnResult.setReturnList(viewDefault());
		return returnResult;
	}
	
	public ResultSet deleteCommand(String taskInfo) {
		addToUndoList(allLists.getTaskList());
		DeleteTask command = new DeleteTask();
		returnResult = command.execute(taskInfo, allLists, validOp);
		returnResult.setReturnList(viewDefault());
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
		return returnResult;
	}
	
	public ResultSet clearCommand() {
		ClearTask command = new ClearTask();
		returnResult = command.execute(allLists.getTaskList());
		return returnResult;
	}
	
	public ResultSet rankCommand(String taskInfo) {
		addToUndoList(allLists.getTaskList());
		RankTask command = new RankTask();
		returnResult = command.execute(taskInfo, allLists, validOp);
		return returnResult;
	}
	
	public ResultSet completeCommand(String taskInfo) {
		addToUndoList(allLists.getTaskList());
		CompleteTask command = new CompleteTask();
		returnResult = command.execute(taskInfo, allLists, validOp);
		return returnResult;	
	}
	
	public ResultSet incompleteCommand(String taskInfo) {
		addToUndoList(allLists.getTaskList());
		IncompleteTask command = new IncompleteTask();
		returnResult = command.execute(taskInfo, allLists, validOp);
		return returnResult;	
	}
	
	public ResultSet editCommand(String userContent) {
		addToUndoList(allLists.getTaskList());
		EditTask command = new EditTask();
		returnResult = command.execute(userContent, allLists, validOp);
		returnResult.setReturnList(viewDefault());
		return returnResult;
	}
	
	public ResultSet undoCommand() {
		UndoCommand command = new UndoCommand();
		returnResult = command.execute(allLists, undoList);
		return returnResult;
	}
	
	public void addToUndoList(ArrayList<Task> taskList) {
		ArrayList<Task> tempTaskList = new ArrayList<Task>();
		for(int index = 0; index < taskList.size(); index++) {
			Task tempTask = new Task();
			tempTask.setDueDate(taskList.get(index).getDueDate());
			tempTask.setEndTime(taskList.get(index).getEndTime());
			tempTask.setStartTime(taskList.get(index).getStartTime());
			tempTask.setTaskID(taskList.get(index).getTaskID());
			tempTask.setTaskName(taskList.get(index).getTaskName());
			tempTask.setTaskPriority(taskList.get(index).getTaskPriority());
			tempTask.setTaskStatus(taskList.get(index).getTaskStatus());
			tempTaskList.add(tempTask);
		}
		undoList.push(new ArrayList<Task>(tempTaskList));
	}
	
	
	

	
	
	
	
	
	
	
}