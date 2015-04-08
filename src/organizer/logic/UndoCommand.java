//@author A0098824N
package organizer.logic;

import java.util.ArrayList;
import java.util.Stack;

public class UndoCommand {
	private final static String MESSAGE_UNDO_SUCCESS = "Undo successfully!";
	private final static String MESSAGE_UNDO_FAIL = "No actions to undo!";
	ResultSet returnResult = new ResultSet();
	ViewTask refreshTask = new ViewTask();
	
	public ResultSet execute(TaskListSet allLists, Stack<ArrayList<Task>> undoList, boolean isView, boolean isSearch) {
		if(!undoList.isEmpty()) {
			allLists.setTaskList(undoList.pop());
			returnResult.setOpStatus(MESSAGE_UNDO_SUCCESS);
		} else {
			returnResult.setOpStatus(MESSAGE_UNDO_FAIL);
		}
		
		if(isView) {
			returnResult.setReturnList(allLists.getViewList());
		} else if(isSearch) {
			returnResult.setReturnList(allLists.getResultList());
		} else {
			returnResult.setReturnList(allLists.getTaskList());
		}
		return returnResult;
	}
	
}
