package organizer.logic;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class ViewTask {
	private static final String MESSAGE_EMPTY_LIST = "No task(s) found!";
	private static final String MESSAGE_SUCCESS = "View %1$s task(s) operation is successful!";
	private static final String MESSAGE_UNSUCCESS = "View %1$s task(s) operation is unsuccessful!";
	private static final String STATUS_INCOMPLETE = "INCOMPLETE";
	private enum ViewType {
		TODAY,
		DEADLINE,
		FLOATING,
		TIMED,
		COMPLETE,
		INCOMPLETE,
		OVERDUE,
		ALL;

		public String toString() {
			switch (this) {
			case TODAY: return "today";
			case COMPLETE: return "COMPLETE";
			case INCOMPLETE: return "INCOMPLETE";
			case DEADLINE: return "DEADLINE";
			case FLOATING: return "FLOATING";
			case TIMED: return "TIMED";
			case OVERDUE: return "OVERDUE";
			case ALL: return "all";
			default: return "any";
			}
		}
	}

	public ResultSet execute(String viewTypeString, TaskListSet allLists){
		ArrayList<Task> tempList = new ArrayList<Task>();
		allLists.setViewList(tempList);
		ResultSet returnResult = new ResultSet();
		
		switch (viewTypeString.trim().toLowerCase()) {
		case "today":
			allLists.setViewList(viewToday(allLists.getTaskList()));
			break;
		case "complete":
			allLists.setViewList(viewComplete(allLists.getTaskList()));
			break;
		case "incomplete":
			allLists.setViewList(viewIncomplete(allLists.getTaskList()));
			break;
		case "all":
			allLists.setViewList(allLists.getTaskList());
			break;
		case "floating":
			allLists.setViewList(viewFloating(allLists.getTaskList()));
			break;
		case "timed":
			allLists.setViewList(viewTimed(allLists.getTaskList()));
			break;
		case "deadline":
			allLists.setViewList(viewDeadline(allLists.getTaskList()));
		case "overdue":
			allLists.setViewList(viewOverDue(allLists.getTaskList()));
			break;
		default:
			returnResult.setReturnList(allLists.getTaskList());
			returnResult.setOpStatus(String.format(MESSAGE_UNSUCCESS, viewTypeString));
			return returnResult;
		}
		
		returnResult.setReturnList(allLists.getViewList());
		returnResult.setOpStatus(isEmptyView(allLists.getViewList(), viewTypeString));
		
		return returnResult;

	}
	
	private String isEmptyView(ArrayList<Task> viewList, String viewTypeString) {
		if(viewList.isEmpty()) {
			return MESSAGE_EMPTY_LIST;
		} else {
			return String.format(MESSAGE_SUCCESS, viewTypeString);
		}
	}
	
	private ArrayList<Task> viewToday(ArrayList<Task> taskList) {
		ArrayList<Task> tempList = new ArrayList<Task>();
		
		for(int index = 0; index < taskList.size(); index++) {
			if(taskList.get(index).getTaskEndDate() != null && taskList.get(index).getTaskEndDate().equals(LocalDate.now()) ) {
				tempList.add(taskList.get(index));
			}
		}
		
		return tempList;
	}
	
	private ArrayList<Task> viewComplete(ArrayList<Task> taskList) {
		ArrayList<Task> tempList = new ArrayList<Task>();
		
		for(int index = 0; index < taskList.size(); index++) {
			if(taskList.get(index).getTaskStatus().equals(ViewType.COMPLETE.toString())) {
				tempList.add(taskList.get(index));
			}
		}
		
		return tempList;
	}
	
	private ArrayList<Task> viewIncomplete(ArrayList<Task> taskList) {
		ArrayList<Task> tempList = new ArrayList<Task>();
		
		for(int index = 0; index < taskList.size(); index++) {
			if(taskList.get(index).getTaskStatus().equals(ViewType.INCOMPLETE.toString())) {
				tempList.add(taskList.get(index));
			}
		}
		
		return tempList;
	}
	
	private ArrayList<Task> viewFloating(ArrayList<Task> taskList) {
		ArrayList<Task> tempList = new ArrayList<Task>();
		
		for(int index = 0; index < taskList.size(); index++) {
			if(taskList.get(index).getTaskType().equals(ViewType.FLOATING.toString())) {
				tempList.add(taskList.get(index));
			}
		}
		
		return tempList;
	}
	
	private ArrayList<Task> viewTimed(ArrayList<Task> taskList) {
		ArrayList<Task> tempList = new ArrayList<Task>();
		
		for(int index = 0; index < taskList.size(); index++) {
			if(taskList.get(index).getTaskType().equals(ViewType.TIMED.toString())) {
				tempList.add(taskList.get(index));
			}
		}
		
		return tempList;
	}
	
	private ArrayList<Task> viewDeadline(ArrayList<Task> taskList) {
		ArrayList<Task> tempList = new ArrayList<Task>();
		
		for(int index = 0; index < taskList.size(); index++) {
			if(taskList.get(index).getTaskType().equals(ViewType.DEADLINE.toString())) {
				tempList.add(taskList.get(index));
			}
		}
		
		return tempList;
	}
	
	private ArrayList<Task> viewOverDue(ArrayList<Task> taskList) {
		ArrayList<Task> tempList = new ArrayList<Task>();
		Task tempTask = new Task();
		LocalDate currentDate = LocalDate.now();
		LocalTime currentTime = LocalTime.now();
		
		for(int index = 0; index < taskList.size(); index++) {
			tempTask = taskList.get(index);
			
			if(tempTask.getTaskEndDate() != null && tempTask.getTaskStatus().equals(STATUS_INCOMPLETE)) {
				//date already overdue
				if(tempTask.getTaskEndDate().compareTo(currentDate) < 0) {
					tempList.add(tempTask);
					//date is the same as current date so proceed to check time
				} else if((tempTask.getTaskEndDate().compareTo(currentDate) == 0) && tempTask.getTaskEndTime() != null) {
					if(tempTask.getTaskEndTime().compareTo(currentTime) < 0) {
						tempList.add(tempTask);
					}
				}
			
			}
		}
		
		return tempList;
	}
	
	
	
}

