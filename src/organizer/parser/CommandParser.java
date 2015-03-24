package organizer.parser;


import java.io.IOException;
import java.util.ArrayList;
//import java.util.List;

import organizer.logic.*;
//import organizer.storage.Storage;

public class CommandParser {

//	private static final String MESSAGE_INVALID_TASK = "Selected task does not exists!";
	private static final String MESSAGE_INVALID_COMMAND = "Unregconized command!";
//	private static final String MESSAGE_UNSUCCESS = "Operation is unsuccessful.\n\n";
//	private static final String MESSAGE_SUCCESS = "Operation is successful.\n\n";
	
	
	ArrayList<Task> taskList = new ArrayList<Task>(); 
	ArrayList<Task> resultList = new ArrayList<Task>(); //for search
	ArrayList<Task> viewList = new ArrayList<Task>();
	
	Logic logic = new Logic();
	
	public ArrayList<Task> loadStorage() throws IOException {
		taskList = logic.loadStorage();
		return taskList;
	}

	public void writeStorage() throws IOException {
		logic.writeStorage();
	}
	//to return multiple values



	public static class ReturnResult {
			String opStatus;
			ArrayList<Task> returnList;
			
			public ReturnResult(String opStatus, ArrayList<Task> returnList) {
				this.opStatus = opStatus;
				this.returnList = returnList;
			}
			
			public String getOpStatus(){
				return opStatus;
			}
			
			public ArrayList<Task> getReturnList(){
				return returnList;
			}
		}
	

	enum COMMAND_TYPE {
		ADD_TASK, DELETE_TASK, VIEW_TASK, SEARCH_TASK, COMPLETE_TASK, CLEAR_TASK, EDIT_TASK, INVALID, EXIT
	};

	private static COMMAND_TYPE determineCommandType(String commandTypeString) throws SecurityException, IOException {	
		if(commandTypeString.equals(null)) {
			throw new Error(MESSAGE_INVALID_COMMAND);
		} else {
			commandTypeString = commandTypeString.toLowerCase();
		}

		switch(commandTypeString) {
		case "add":
			return COMMAND_TYPE.ADD_TASK;
		case "delete":
			return COMMAND_TYPE.DELETE_TASK;
		case "view":
			return COMMAND_TYPE.VIEW_TASK;
		case "search":
			return COMMAND_TYPE.SEARCH_TASK;
		case "clear":
			return COMMAND_TYPE.CLEAR_TASK;
		case "complete":
			return COMMAND_TYPE.COMPLETE_TASK;
		case "edit":
			return COMMAND_TYPE.EDIT_TASK;
		case "exit":
			return COMMAND_TYPE.EXIT;
		default:
			return COMMAND_TYPE.INVALID;
		}

	}
	public ReturnResult executeCommand(String userCommand) throws IOException {
		//split the userCommand into operation and task info
		String userOperation;
		String userContent;

		if(userCommand.indexOf(' ') >= 0) {
			userOperation = userCommand.substring(0, userCommand.indexOf(' '));
			userContent = userCommand.substring(userCommand.indexOf(' ')+1);

		} else {
			userOperation = userCommand;
			userContent = "";
		}


		COMMAND_TYPE commandType = determineCommandType(userOperation);

		switch (commandType) {
		case ADD_TASK:	
			return new ReturnResult("Addition successful", logic.addTask(userContent));
		case DELETE_TASK:
			return new ReturnResult("Delete successful", logic.deleteTask(userContent));
		case SEARCH_TASK:
			return new ReturnResult("Search successful", logic.searchTask(userContent));
		case VIEW_TASK:
			return new ReturnResult("View successful", logic.viewList(userContent));
		case CLEAR_TASK:
			return new ReturnResult("Clear successful", logic.clearTask());
		case COMPLETE_TASK:
			return new ReturnResult("Task Completed", logic.completeTask(userContent));
		case EDIT_TASK:
			return new ReturnResult("Edition successful", logic.editTask(userContent));
		case EXIT:
			logic.writeStorage();
			System.exit(0);	
		default:
			//throw an error if the command is not recognized
			throw new Error(MESSAGE_INVALID_COMMAND);
		}

	}
}
