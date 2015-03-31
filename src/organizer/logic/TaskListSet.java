package organizer.logic;

import java.util.ArrayList;

public class TaskListSet {
	ArrayList<Task> taskList = new ArrayList<Task>(); 
	ArrayList<Task> resultList = new ArrayList<Task>();
	ArrayList<Task> viewList = new ArrayList<Task>();
	
	public TaskListSet() {
		taskList = null;
		resultList = null;
		viewList = null;
	}
	
	public TaskListSet(ArrayList<Task> taskList, ArrayList<Task> resultList, ArrayList<Task> viewList) {
		this.taskList = taskList;
		this.resultList = resultList;
		this.viewList = viewList;
	}
	
	public ArrayList<Task> getTaskList() {
		return taskList;
	}
	
	public void setTaskList(ArrayList<Task> taskList) {
		this.taskList = taskList;
	}
	
	public ArrayList<Task> getResultList() {
		return resultList;
	}
	
	public void setResultList(ArrayList<Task> resultList) {
		this.resultList = resultList;
	}
	
	public ArrayList<Task> getViewList() {
		return viewList;
	}
	
	public void setViewList(ArrayList<Task> viewList) {
		this.viewList = viewList;
	}
	
	
}
