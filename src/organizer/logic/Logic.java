package organizer.logic;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import organizer.storage.Storage;

public class Logic {
	private static final String dateFieldIdentifier = "%%";
	private static final int daysPerWeek = 7;
	private static final String dayPattern = "monday|tuesday|wednesday|thursday|friday|saturday|sunday";
	private static final String datePattern = "\\d{4}-\\d{2}-\\d{2}";

	Storage tempStorage = new Storage();
	ArrayList<Task> taskList = new ArrayList<Task>(); 
	ArrayList<Task> resultList = new ArrayList<Task>(); //for search
	ArrayList<Task> viewList = new ArrayList<Task>();

	boolean isSearch = false;
	boolean isView = false;

	Task tempTask = new Task();

	enum COMMAND_TYPE {
		ADD_TASK, DELETE_TASK, VIEW_TASK, SEARCH_TASK, COMPLETE_TASK, CLEAR_TASK, INVALID, EXIT
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
		case "clear":
			return COMMAND_TYPE.CLEAR_TASK;
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
		case CLEAR_TASK:
			return clearTask();
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
		String taskName = taskInfo;
		String taskDate = null;
		LocalDate dueDate = LocalDate.now();

		if(taskInfo.contains(dateFieldIdentifier)) {
			taskDate = taskInfo.substring(taskInfo.indexOf(dateFieldIdentifier)+2);
			if(determineDate(taskDate) != null) {
				taskName = taskInfo.substring(0, taskInfo.indexOf(dateFieldIdentifier)-1);
				dueDate = determineDate(taskDate);
			} 
		}

		tempTask.setTaskName(taskName);
		tempTask.setDueDate(dueDate);
		tempTask.setTaskStatus("INCOMPLETE");
		tempTask.setTaskID(taskList.size());

		taskList.add(tempTask);
		tempTask = new Task();
		return taskList;

	}

	private LocalDate determineDate(String dateInfo) {
		dateInfo = dateInfo.trim().toLowerCase();
		LocalDate taskDate;
		if(dateInfo.equals("today")) {
			taskDate = LocalDate.now();
		} else if(dateInfo.equals("tomorrow")) {
			taskDate = LocalDate.now().plusDays(1);
		} else if(dateInfo.matches(datePattern)) {
			taskDate = LocalDate.parse(dateInfo);
		} else if(dateInfo.matches(dayPattern)) {
			taskDate = determineDay(dateInfo);
		} else {
			taskDate = null;
		}

		return taskDate;
	}

	private LocalDate determineDay(String dateInfo) {
		LocalDate taskDate = null;
		String dayOfWeek = LocalDate.now().getDayOfWeek().toString().toLowerCase();

		Map<String,Integer> dayMap=new HashMap<String,Integer>();

		dayMap.put("sunday",1);
		dayMap.put("monday",2);
		dayMap.put("tuesday",3);
		dayMap.put("wednesday",4);
		dayMap.put("thursday",5);
		dayMap.put("friday",6);
		dayMap.put("saturday",7);


		int numOfDay = dayMap.get(dayOfWeek).intValue();
		int numOfTaskDay = dayMap.get(dateInfo).intValue();

		if(numOfDay == numOfTaskDay) {
			taskDate = LocalDate.now().plusDays(daysPerWeek);
		} else {
			int taskDuration = (numOfTaskDay + daysPerWeek - numOfDay) % daysPerWeek;
			taskDate = LocalDate.now().plusDays(taskDuration);
		}

		return taskDate;
	}


	public ArrayList<Task> deleteTask(String taskInfo) {
		int lineNum = Integer.parseInt(taskInfo.trim());
		int taskID = -1;

		if(isSearch && lineNum <= resultList.size()) {
			taskID = resultList.get(lineNum-1).getTaskID();
			isSearch = false;
		} else if(isView && lineNum <= viewList.size()){
			taskID = viewList.get(lineNum-1).getTaskID();
			isView = false;

		} else{
			if(lineNum <= taskList.size()) {
				taskID = taskList.get(lineNum-1).getTaskID();
			}
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

	public ArrayList<Task> clearTask(){
		taskList.clear();
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
		viewType = viewType.toLowerCase();
		viewList.clear();
		isView = true;

		for(int i = 0; i < taskList.size(); i++) {
			Task task = taskList.get(i);
			if(viewType.trim().equals("today") && task.getDueDate().equals(currentDate)) {
				viewList.add(task);
			}else if(viewType.trim().equals("complete") && task.getTaskStatus().toLowerCase().equals("complete")){
				viewList.add(task);
			}else if (viewType.trim().equalsIgnoreCase("all")){
				viewList.add(task);
			}
		}

		return viewList;

	}
}

