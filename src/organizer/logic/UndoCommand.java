//@author A0098824N
package organizer.logic;

import java.util.ArrayList;
import java.util.Stack;

public class UndoCommand {
	private final static String MESSAGE_UNDO_SUCCESS = "Undo successfully!";
	private final static String MESSAGE_UNDO_FAIL = "No actions to undo!";
	ResultSet returnResult = new ResultSet();
	ViewTask refreshTask = new ViewTask();
	
	public ResultSet execute(TaskListSet allLists, Stack<ArrayList<Task>> undoList) {
		if(!undoList.isEmpty()) {
			allLists.setTaskList(undoList.pop());
			returnResult.setOpStatus(MESSAGE_UNDO_SUCCESS);
			returnResult.setIsSuccessful(true);
		} else {
			returnResult.setOpStatus(MESSAGE_UNDO_FAIL);
			returnResult.setIsSuccessful(false);
		}
		returnResult.setReturnList(allLists.getTaskList());
		return returnResult;
	}
	
}
