package organizer.logic;

import java.util.ArrayList;

import organizer.storage.Storage;

public class Logic {
	Storage tempStorage = new Storage();
	ArrayList<Task> tempList = new ArrayList<Task>();
	ArrayList<Task> resultList = new ArrayList<Task>();
	Task tempTask = new Task();
	
	enum COMMAND_TYPE {
		ADD_TASK, DELETE_TASK, VIEW_TASK, SEARCH_TASK, INVALID, EXIT
	};
	
	private static COMMAND_TYPE determineCommandType(String commandTypeString) {
		if(commandTypeString.equals(null))
			throw new Error("Unregonized command type");
		else
			commandTypeString = commandTypeString.toLowerCase();

		switch(commandTypeString) {
		case "add":
			return COMMAND_TYPE.ADD_TASK;
		case "delete":
			return COMMAND_TYPE.DELETE_TASK;
		case "view":
			return COMMAND_TYPE.VIEW_TASK;
		case "search":
			return COMMAND_TYPE.SEARCH_TASK;
		case "exit":
			return COMMAND_TYPE.EXIT;
		default:
			return COMMAND_TYPE.INVALID;
		}

	}
	
	public ArrayList<Task> loadStorage() {
		tempList = tempStorage.readFile();
		return tempList;
	}
	
	public void writeStorage() {
		tempStorage.writeFile(tempList);
	}
	

	public void executeCommand(String userCommand) {
		//split the userCommand into operation and task info
		String userOperation = userCommand.substring(0, userCommand.indexOf(' '));
		String userContent = userCommand.substring(userCommand.indexOf(' '));
		
		COMMAND_TYPE commandType = determineCommandType(userCommand);

		switch (commandType) {
		case ADD_TASK:	
			addTask(userContent);	
		case DELETE_TASK:
			deleteTask(userContent);
		case SEARCH_TASK:
			searchTask(userContent);
		case VIEW_TASK:
			viewList(userContent);
		case EXIT:
			System.exit(0);	
		default:
			//throw an error if the command is not recognized
			throw new Error("Unrecognized command type");
		}
		
	}
	
	public void addTask(String taskInfo) {
		
	}
	
	public void deleteTask() {
		
	}
	
	public void searchTask() {
		
	}
	
	public void viewList(){
		
	}
}
