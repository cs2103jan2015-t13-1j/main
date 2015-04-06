//@author A0098824N
package organizer.logic;

import java.util.ArrayList;

public class ClearTask {
	private static final String MESSAGE_EMPTY_LIST = "No task(s) found!";
	private static final String MESSAGE_SUCCESS = "Clear task(s) operation is successful!";
	
	public ResultSet execute(ArrayList<Task> taskList){
		ResultSet returnResult = new ResultSet();
		
		if(taskList.isEmpty()) {
			returnResult.setOpStatus(MESSAGE_EMPTY_LIST);
		} else {
			taskList.clear();
			returnResult.setOpStatus(String.format(MESSAGE_SUCCESS));
		}
		
		returnResult.setReturnList(taskList);
		return returnResult;
	}
}
