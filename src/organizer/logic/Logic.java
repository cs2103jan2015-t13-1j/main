package organizer.logic;


import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import organizer.storage.Storage;

public class Logic {


	private static final String MESSAGE_INVALID_TASK = "Selected task does not exists!";
	private static final String MESSAGE_EMPTY_LIST = "No task(s) found!";
	private static final String MESSAGE_NO_RESULT = "No results found!";
	private static final String MESSAGE_INVALID_CONTENT = "Edit task operation failed for invalid content!";
	private static final String MESSAGE_SEARCH_FOUND = "Search results found: \"%1$s\"";
	private static final String MESSAGE_SUCCESS = "%1$s task(s) operation is successful!\n\n";

	private static final String dateFieldIdentifier = "%";
	private static final String priorityFieldIdentifier = "^";
	private static final int daysPerWeek = 7;
	private static final String dayPattern = "monday|tuesday|wednesday|thursday|friday|saturday|sunday";
	private static final String datePattern = "\\d{4}-\\d{2}-\\d{2}";

	Storage tempStorage = new Storage();
	ResultSet returnResult = new ResultSet();

	ArrayList<Task> taskList = new ArrayList<Task>(); 
	ArrayList<Task> resultList = new ArrayList<Task>(); //for search
	ArrayList<Task> viewList = new ArrayList<Task>();

	boolean isSearch = false;
	boolean isView = false;
	boolean isSuccessful = false;

	Task tempTask = new Task();

	public ArrayList<Task> loadStorage() throws IOException {
		taskList = tempStorage.readFile();
		return taskList;
	}

	public void writeStorage() throws IOException {
		tempStorage.writeFile(taskList);
	}


	public ResultSet editTask(String userContent) {
		int lineNum = Integer.parseInt(userContent.substring(0, userContent.indexOf(" ")));
		if(checkValidTask(lineNum)) {
			int taskID = checkForTaskID(lineNum);
			String editContent = userContent.substring(userContent.indexOf(" "));
			if(editContent.indexOf(" ") >= 0) {
				if(editContent.contains(dateFieldIdentifier)) {
					LocalDate dueDate = determineDate(editContent.substring(editContent.indexOf(dateFieldIdentifier)+1));
					taskList.get(taskID).setDueDate(dueDate);
				} else {
					taskList.get(taskID).setTaskName(editContent.substring(1));
				}
				returnResult.setOpStatus(String.format(MESSAGE_SUCCESS, "Edit"));
			} else {
				returnResult.setOpStatus(MESSAGE_INVALID_CONTENT);
			}

		} else {
			returnResult.setOpStatus(MESSAGE_INVALID_TASK);
		}

		returnResult.setReturnList(taskList);

		return returnResult;
	}

	public ResultSet addTask(String taskInfo) {
		String taskName = null;
		String taskDate = null;
		int taskPrior = 0;
		LocalDate dueDate = LocalDate.now();
		
		if(taskInfo.contains(dateFieldIdentifier) && taskInfo.contains(priorityFieldIdentifier)) {
			taskDate = taskInfo.substring(taskInfo.indexOf(dateFieldIdentifier)+1, taskInfo.indexOf(priorityFieldIdentifier));
			taskPrior = Integer.parseInt(taskInfo.substring(taskInfo.indexOf(priorityFieldIdentifier)+1));
			taskName = taskInfo.substring(0, taskInfo.indexOf(dateFieldIdentifier));
			dueDate = determineDate(taskDate);
			
		} else if(taskInfo.contains(priorityFieldIdentifier) && !taskInfo.contains(dateFieldIdentifier)) {
			taskPrior = Integer.parseInt(taskInfo.substring(taskInfo.indexOf(priorityFieldIdentifier)+1));
			taskName = taskInfo.substring(0, taskInfo.indexOf(priorityFieldIdentifier));
			
		} else if(!taskInfo.contains(priorityFieldIdentifier) && taskInfo.contains(dateFieldIdentifier)) {
			taskName = taskInfo.substring(0, taskInfo.indexOf(dateFieldIdentifier));
			taskDate = taskInfo.substring(taskInfo.indexOf(dateFieldIdentifier)+1);
			dueDate = determineDate(taskDate);
			
		} else {
			dueDate = null;
			taskName = taskInfo;
		}

		switch(taskPrior) {
		case 0: tempTask.setTaskPriority("");
		break;
		case 1: tempTask.setTaskPriority("LOW");
		break;
		case 2: tempTask.setTaskPriority("MED");
		break;
		case 3: tempTask.setTaskPriority("HIGH");
		break;
		}
		
		tempTask.setTaskName(taskName);
		tempTask.setDueDate(dueDate);
		tempTask.setTaskStatus("INCOMPLETE");
		tempTask.setTaskID(taskList.size());

		taskList.add(tempTask);
		tempTask = new Task();

		returnResult.setOpStatus(String.format(MESSAGE_SUCCESS,"Add"));
		returnResult.setReturnList(taskList);

		return returnResult;
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


	public ResultSet deleteTask(String taskInfo) {
		int lineNum = Integer.parseInt(taskInfo.trim());
		if(checkValidTask(lineNum)) {
			int taskID = checkForTaskID(lineNum);
			removeFromTaskList(taskID);
			returnResult.setOpStatus(String.format(MESSAGE_SUCCESS,"Delete"));
		} else {
			returnResult.setOpStatus(MESSAGE_INVALID_TASK);
		}

		returnResult.setReturnList(taskList);

		return returnResult;
	}

	private boolean checkValidTask(int lineNum) {
		if(isSearch && lineNum > resultList.size()) {
			return false;
		} else if(isView && lineNum > viewList.size()) {
			return false;
		} else if(lineNum > taskList.size()){
			return false;
		} else {
			return true;
		}
	}

	private int checkForTaskID(int lineNum) {
		int taskID = -1;

		if(isSearch) {
			taskID = resultList.get(lineNum-1).getTaskID();
			isSearch = false;
		} else if(isView) {
			taskID = viewList.get(lineNum-1).getTaskID();
			isView = false;			
		} else {
			taskID = taskList.get(lineNum-1).getTaskID();
		}
		return taskID;
	}


	private void removeFromTaskList(int taskID) {
		for(int i = 0; i < taskList.size(); i++) {
			if(taskList.get(i).getTaskID() == taskID) {
				taskList.remove(i);
			}
		}
	}

	public ResultSet completeTask(String taskInfo) {
		int lineNum = Integer.parseInt(taskInfo.trim());
		if(checkValidTask(lineNum)) {
			int taskID = checkForTaskID(lineNum);
			taskList.get(taskID).setTaskStatus("COMPLETE");
			returnResult.setOpStatus(String.format(MESSAGE_SUCCESS, "Complete"));
		} else {
			returnResult.setOpStatus(MESSAGE_INVALID_TASK);
		}

		returnResult.setReturnList(taskList);
		return returnResult;
	}

	public ResultSet clearTask(){
		if(taskList.isEmpty()) {
			returnResult.setOpStatus(MESSAGE_EMPTY_LIST);
		} else {
			taskList.clear();
			returnResult.setOpStatus(String.format(MESSAGE_SUCCESS, "Clear"));
		}

		returnResult.setReturnList(taskList);
		return returnResult;
	}

	public ResultSet searchTask(String searchTerm) {
		resultList.clear();
		isSearch = true;

		for(int i = 0; i < taskList.size(); i++) {
			Task task = taskList.get(i);
			if(task.getTaskName().contains(searchTerm.trim())) {
				resultList.add(task);
			}
		}

		if(resultList.isEmpty()) {
			returnResult.setOpStatus(MESSAGE_NO_RESULT);
		} else {
			returnResult.setOpStatus(String.format(MESSAGE_SEARCH_FOUND,searchTerm));
		}

		returnResult.setReturnList(resultList);
		return returnResult;
	}

	public ResultSet viewList(String viewType){
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

		if(viewList.isEmpty()) {
			returnResult.setOpStatus(MESSAGE_EMPTY_LIST);
		} else {
			returnResult.setOpStatus(String.format(MESSAGE_SUCCESS, "View "+viewType));
		}

		returnResult.setReturnList(viewList);
		return returnResult;

	}

	public ResultSet postponeTask(String taskInfo) {	
		int lineNum = Integer.parseInt(taskInfo.trim());		
		LocalDate newDueDate = taskList.get(lineNum - 1).getDueDate().plusDays(1);
		taskList.get(lineNum - 1).setDueDate(newDueDate);
		returnResult.setOpStatus(String.format(MESSAGE_SUCCESS,"Postpone"));
		returnResult.setReturnList(taskList);
		return returnResult;
	}
}

