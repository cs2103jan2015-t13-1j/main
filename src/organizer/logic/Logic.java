package organizer.logic;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import organizer.storage.Storage;

public class Logic {
	Storage tempStorage = new Storage();
	ArrayList<Task> taskList = new ArrayList<Task>(); 
	ArrayList<Task> resultList = new ArrayList<Task>(); //for search
	ArrayList<Task> viewList = new ArrayList<Task>();
	
	boolean isSearch = false;
	boolean isView = false;

	Task tempTask = new Task();

	enum COMMAND_TYPE {
		ADD_TASK, DELETE_TASK, VIEW_TASK, SEARCH_TASK, COMPLETE_TASK, INVALID, EXIT
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
		case "complete":
			return COMMAND_TYPE.COMPLETE_TASK;
		case "exit":
			return COMMAND_TYPE.EXIT;
		default:
			return COMMAND_TYPE.INVALID;
		}

	}

	public ArrayList<Task> loadStorage() throws IOException {
		taskList = tempStorage.readFile();
		return taskList;
	}

	public void writeStorage() throws IOException {
		tempStorage.writeFile(taskList);
	}


	public ArrayList<Task> executeCommand(String userCommand) throws IOException {
		//split the userCommand into operation and task info
		String userOperation;
		String userContent;
		
		if(userCommand.indexOf(' ') >= 0) {
			userOperation = userCommand.substring(0, userCommand.indexOf(' '));
			userContent = userCommand.substring(userCommand.indexOf(' ')+1);

		}
		else {
			userOperation = userCommand;
			userContent = null;
		}
		

		COMMAND_TYPE commandType = determineCommandType(userOperation);

		switch (commandType) {
		case ADD_TASK:	
			return addTask(userContent);	
		case DELETE_TASK:
			return deleteTask(userContent);
		case SEARCH_TASK:
			return searchTask(userContent);
		case VIEW_TASK:
			return viewList(userContent);
		case COMPLETE_TASK:
			return completeTask(userContent);
		case EXIT:
			tempStorage.writeFile(taskList);
			System.exit(0);	
		default:
			//throw an error if the command is not recognized
			throw new Error("Unrecognized command type");
		}

	}

	public ArrayList<Task> addTask(String taskInfo) {
		tempTask.setTaskName(taskInfo);
		tempTask.setDueDate(LocalDate.now());
		tempTask.setTaskStatus("INCOMPLETE");
		tempTask.setTaskID(taskList.size());
		taskList.add(tempTask);
		tempTask = new Task();
		return taskList;

	}
	
	public ArrayList<Task> deleteTask(String taskInfo) {
		int lineNum = Integer.parseInt(taskInfo.trim()) - 1;
		int taskID;
		
		if(isSearch) {
			taskID = resultList.get(lineNum).getTaskID();
			isSearch = false;
		} else if(isView){
			taskID = viewList.get(lineNum).getTaskID();
			isView = false;
			
		} else {
			taskID = taskList.get(lineNum-1).getTaskID();
		}
		
		removeFromTaskList(taskID);
		return taskList;
		
	}
	

	private void removeFromTaskList(int taskID) {
		for(int i = 0; i < taskList.size(); i++) {
			if(taskList.get(i).getTaskID() == taskID) {
				taskList.remove(i);
			}
		}
	}
	
	public ArrayList<Task> completeTask(String taskInfo) {
		int num = Integer.parseInt(taskInfo.trim());
		taskList.get(num-1).setTaskStatus("COMPLETE");
		return taskList;
	}



	public ArrayList<Task> searchTask(String searchTerm) {
		resultList.clear();
		isSearch = true;
		
		for(int i = 0; i < taskList.size(); i++) {
			Task task = taskList.get(i);
			if(task.getTaskName().contains(searchTerm.trim())) {
				resultList.add(task);
			}
		}

		return resultList;
	}

	public ArrayList<Task> viewList(String viewType){
		LocalDate currentDate = LocalDate.now();
		viewList.clear();
		
		for(int i = 0; i < taskList.size(); i++) {
			Task task = taskList.get(i);
			if(viewType.trim().equalsIgnoreCase("today")&&task.getDueDate() == currentDate) {
				viewList.add(task);
			}else if(viewType.trim().equalsIgnoreCase("complete")&&task.getTaskStatus().equalsIgnoreCase("complete")){
				viewList.add(task);
			}else if (viewType.trim().equalsIgnoreCase("all")){
				viewList.add(task);
			}
		}
		System.out.println(currentDate);
		return viewList;
	}
}
