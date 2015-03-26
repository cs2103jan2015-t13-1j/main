package organizer.logic;


import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;


import organizer.storage.Storage;

public class Logic {


	private static final String MESSAGE_INVALID_TASK = "Selected task does not exists!";
	private static final String MESSAGE_INVALID_RANK = "Invalid priority rank!";
	private static final String MESSAGE_EMPTY_LIST = "No task(s) found!";
	private static final String MESSAGE_NO_RESULT = "No results found!";
	private static final String MESSAGE_INVALID_CONTENT = "Edit task operation failed for invalid content!";
	private static final String MESSAGE_SEARCH_FOUND = "Search results found: \"%1$s\"";
	private static final String MESSAGE_SUCCESS = "%1$s task(s) operation is successful!\n\n";

	private static final String dateFieldIdentifier = "%";
//	private static final String timeFieldIdentifier = "@";

	private static final String rankPattern = "high|medium|low";


	Storage tempStorage = new Storage();
	ResultSet returnResult = new ResultSet();
	DateAndTime dateTime = new DateAndTime();
	
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
		if(isValidTask(lineNum)) {
			int taskID = checkForTaskID(lineNum);
			String editContent = userContent.substring(userContent.indexOf(" "));
			if(editContent.indexOf(" ") >= 0) {
				if(editContent.contains(dateFieldIdentifier)) {
					LocalDate dueDate = dateTime.determineDate(editContent.substring(editContent.indexOf(dateFieldIdentifier)+1));
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
		LocalDate dueDate = LocalDate.now();
		
		if(taskInfo.contains(dateFieldIdentifier)) {
			taskName = taskInfo.substring(0,taskInfo.indexOf(dateFieldIdentifier));
			taskDate = taskInfo.substring(taskInfo.indexOf(dateFieldIdentifier)+1);
			dueDate = dateTime.determineDate(taskDate);
		} else {
			taskName = taskInfo;
			dueDate = null;
		}
		
		tempTask.setTaskName(taskName);
		tempTask.setDueDate(dueDate);
		tempTask.setTaskStatus("INCOMPLETE");
		tempTask.setTaskPriority(null);
		tempTask.setTaskID(taskList.size());

		taskList.add(tempTask);
		tempTask = new Task();

		returnResult.setOpStatus(String.format(MESSAGE_SUCCESS,"Add"));
		returnResult.setReturnList(taskList);

		return returnResult;
	}

	public ResultSet deleteTask(String taskInfo) {
		int lineNum = Integer.parseInt(taskInfo.trim());
		if(isValidTask(lineNum)) {
			int taskID = checkForTaskID(lineNum);
			removeFromTaskList(taskID);
			returnResult.setOpStatus(String.format(MESSAGE_SUCCESS,"Delete"));
		} else {
			returnResult.setOpStatus(MESSAGE_INVALID_TASK);
		}

		returnResult.setReturnList(taskList);

		return returnResult;
	}

	public boolean isValidTask(int lineNum) {
		if(isSearch && lineNum > resultList.size()) {
			return false;
		} else if(isView && lineNum > viewList.size()) {
			return false;
		} else if(lineNum > taskList.size()){
			return false;
		} else if(lineNum <= 0) {
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
		if(isValidTask(lineNum)) {
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
		if(isValidTask(lineNum)) {
			LocalDate newDueDate = taskList.get(lineNum - 1).getDueDate().plusDays(1);
			taskList.get(lineNum - 1).setDueDate(newDueDate);
			returnResult.setOpStatus(String.format(MESSAGE_SUCCESS,"Postpone"));
		} else {
			returnResult.setOpStatus(MESSAGE_INVALID_TASK);
		}
		returnResult.setReturnList(taskList);
		return returnResult;
	}
	
	public ResultSet rankTask(String taskInfo) {
		int lineNum = Integer.parseInt(taskInfo.substring(0, taskInfo.indexOf(" ")));
		String taskRank = taskInfo.substring(taskInfo.indexOf(" ")+1);
		
		if(isValidTask(lineNum) && isValidRank(taskRank)) {
			int taskID = checkForTaskID(lineNum);
			for(int i = 0; i < taskList.size(); i++) {
				if(taskList.get(i).getTaskID() == taskID) {
					taskList.get(i).setTaskPriority(taskRank.toUpperCase());
				}
			}
			returnResult.setOpStatus(String.format(MESSAGE_SUCCESS,"Rank"));
			
		} else if(!isValidRank(taskRank)){
			returnResult.setOpStatus(MESSAGE_INVALID_RANK);
		} else {
			returnResult.setOpStatus(MESSAGE_INVALID_TASK);
		}
		
		returnResult.setReturnList(taskList);
		
		return returnResult;
	}
	
	private boolean isValidRank(String taskRank) {
		if(taskRank.matches(rankPattern)) {
			return true;
		} else {
			return false;
		}
	}
}


