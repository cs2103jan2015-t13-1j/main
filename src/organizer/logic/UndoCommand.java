package organizer.logic;

import java.util.ArrayList;
import java.util.Stack;

public class UndoCommand {
	
	Stack<ArrayList<Task>> undoList = new Stack<ArrayList<Task>>();
	ArrayList<Task> tempList = new ArrayList<Task>();
	ResultSet returnResult = new ResultSet();
	
	public ResultSet execute(TaskListSet allLists) {
		if(!undoList.isEmpty()) {
			allLists.setTaskList(null);
			tempList = undoList.pop();
			allLists.setTaskList(tempList);
			returnResult.setOpStatus("Undo successfully!");
		} else {
			returnResult.setOpStatus("No actions to undo!");
		}
		
		returnResult.setReturnList(allLists.getTaskList());
		return returnResult;
	}
	
	public void addToUndoList(ArrayList<Task> taskList){		
		undoList.push(taskList);
	}
}
