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
		ViewTask command = new ViewTask();
		returnResult = command.execute(MODE_INIT_VIEW, allLists);
		allLists.setInitList(returnResult.getReturnList());
		return allLists.getInitList();
	}

	public void writeStorage() throws IOException {
		tempStorage.writeFile(allLists.getTaskList());
	}
	
	public ResultSet addCommand(String taskInfo) {
		undoList.push(new ArrayList<Task>(allLists.getTaskList()));
		AddTask command = new AddTask();
		returnResult = command.execute(taskInfo, allLists.getTaskList());
		return returnResult;
	}
	
	public ResultSet deleteCommand(String taskInfo) {
		undoList.push(new ArrayList<Task>(allLists.getTaskList()));
		DeleteTask command = new DeleteTask();
		returnResult = command.execute(taskInfo, allLists, validOp);
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
		undoList.push(new ArrayList<Task>(allLists.getTaskList()));
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
		undoList.push(new ArrayList<Task>(allLists.getTaskList()));
		RankTask command = new RankTask();
		returnResult = command.execute(taskInfo, allLists, validOp);
		return returnResult;
	}
	
	public ResultSet completeCommand(String taskInfo) {
		undoList.push(new ArrayList<Task>(allLists.getTaskList()));
		CompleteTask command = new CompleteTask();
		returnResult = command.execute(taskInfo, allLists, validOp);
		return returnResult;	
	}
	
	public ResultSet incompleteCommand(String taskInfo) {
		undoList.push(new ArrayList<Task>(allLists.getTaskList()));
		IncompleteTask command = new IncompleteTask();
		returnResult = command.execute(taskInfo, allLists, validOp);
		return returnResult;	
	}
	
	public ResultSet editCommand(String userContent) {
		undoList.push(new ArrayList<Task>(allLists.getTaskList()));
		EditTask command = new EditTask();
		returnResult = command.execute(userContent, allLists, validOp);
		return returnResult;
	}
	
	public ResultSet undoCommand() {
		UndoCommand command = new UndoCommand();
		returnResult = command.execute(allLists, undoList);
		return returnResult;
	}
	
	
	

	
	
	
	
	
	
	
}