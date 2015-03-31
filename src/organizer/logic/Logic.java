package organizer.logic;


import java.io.IOException;
//import java.time.LocalDate;
import java.util.ArrayList;


import organizer.storage.Storage;

public class Logic {

//
//	private static final String MESSAGE_INVALID_TASK = "Selected task does not exists!";
//	private static final String MESSAGE_INVALID_RANK = "Invalid priority rank!";
//	private static final String MESSAGE_EMPTY_LIST = "No task(s) found!";
//	private static final String MESSAGE_NO_RESULT = "No results found!";
//	private static final String MESSAGE_INVALID_CONTENT = "Edit task operation failed for invalid content!";
//	private static final String MESSAGE_SEARCH_FOUND = "Search results found: \"%1$s\"";
//	private static final String MESSAGE_SUCCESS = "%1$s task(s) operation is successful!\n\n";
//
//	private static final String dateFieldIdentifier = "%";
////	private static final String timeFieldIdentifier = "@";
//
//	private static final String rankPattern = "high|medium|low";


	Storage tempStorage = new Storage();
	ResultSet returnResult = new ResultSet();
	TaskListSet allLists = new TaskListSet();
	Validation validOp = new Validation();
	
	boolean isSearch = false;
	boolean isView = false;
	boolean isSuccessful = false;


	public ArrayList<Task> loadStorage() throws IOException {
		allLists.setTaskList(tempStorage.readFile()); 
		return allLists.getTaskList();
	}

	public void writeStorage() throws IOException {
		tempStorage.writeFile(allLists.getTaskList());
	}
	
	public ResultSet addCommand(String taskInfo) {
		AddTask command = new AddTask();
		returnResult = command.execute(taskInfo, allLists);
		return returnResult;
	}
	
	public ResultSet deleteCommand(String taskInfo) {
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
	
	public ResultSet editCommand(String userContent) {
		EditTask command = new EditTask();
		returnResult = command.execute(userContent, allLists, validOp);
		return returnResult;
	}
	
	
	
	
	
	
	
}