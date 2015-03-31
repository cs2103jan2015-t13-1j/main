package organizer.logic;

import java.util.ArrayList;

public class ClearTask {
	private static final String MESSAGE_EMPTY_LIST = "No task(s) found!";
	private static final String MESSAGE_SUCCESS = "%1$s task(s) operation is successful!\n\n";
	
	public ResultSet execute(TaskListSet allLists){
		ResultSet returnResult = new ResultSet();
		ArrayList<Task> tempList = new ArrayList<Task>();
		tempList = allLists.getTaskList();
		
		if(tempList.isEmpty()) {
			returnResult.setOpStatus(MESSAGE_EMPTY_LIST);
		} else {
			tempList.clear();
			returnResult.setOpStatus(String.format(MESSAGE_SUCCESS, "Clear"));
		}
		
		allLists.setTaskList(tempList);
		returnResult.setReturnList(allLists.getTaskList());
		return returnResult;
	}
}
