package organizer.logic;


import java.io.IOException;
//import java.time.LocalDate;
import java.util.ArrayList;


import organizer.storage.Storage;

public class Logic {

	Storage tempStorage = new Storage();
	ResultSet returnResult = new ResultSet();
	TaskListSet allLists = new TaskListSet();
	Validation validOp = new Validation();
	UndoCommand undoOp = new UndoCommand();

	public ArrayList<Task> loadStorage() throws IOException {
		allLists.setTaskList(tempStorage.readFile()); 
		return allLists.getTaskList();
	}

	public void writeStorage() throws IOException {
		tempStorage.writeFile(allLists.getTaskList());
	}
	
	public ResultSet addCommand(String taskInfo) {
	//	undoOp.addToUndoList(allLists.getTaskList());
		AddTask command = new AddTask();
		returnResult = command.execute(taskInfo, allLists.getTaskList());
		return returnResult;
	}
	
	public ResultSet deleteCommand(String taskInfo) {
		undoOp.addToUndoList(allLists.getTaskList());
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
		PostponeTask command = new PostponeTask();
		returnResult = command.execute(taskInfo, allLists, validOp);
		return returnResult;
	}
	
	public ResultSet clearCommand() {
		ClearTask command = new ClearTask();
		returnResult = command.execute(allLists);
		return returnResult;
	}
	
	public ResultSet rankCommand(String taskInfo) {
		RankTask command = new RankTask();
		returnResult = command.execute(taskInfo, allLists, validOp);
		return returnResult;
	}
	
	public ResultSet completeCommand(String taskInfo) {
		CompleteTask command = new CompleteTask();
		returnResult = command.execute(taskInfo, allLists, validOp);
		return returnResult;	
	}
	
	public ResultSet incompleteCommand(String taskInfo) {
		IncompleteTask command = new IncompleteTask();
		returnResult = command.execute(taskInfo, allLists, validOp);
		return returnResult;	
	}
	
	public ResultSet editCommand(String userContent) {
		EditTask command = new EditTask();
		returnResult = command.execute(userContent, allLists, validOp);
		return returnResult;
	}
	
	public ResultSet undoCommand() {
		returnResult = undoOp.execute(allLists);
		return returnResult;
	}
	
	
	
	
	
	
	
}