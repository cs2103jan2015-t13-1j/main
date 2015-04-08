package organizer.logic;

import java.util.ArrayList;

//@author A0098824N
public class IncompleteTask {
    private static final String MESSAGE_INVALID_TASK = "Selected task does not exists!";
    private static final String MESSAGE_SUCCESS = "Revert completed task operation is successful!";
    private static final String STATUS_TASK = "INCOMPLETE";
    
    public ResultSet execute(String taskInfo, TaskListSet allLists, Validation validOp) {
        int lineNum = Integer.parseInt(taskInfo.trim());
        ResultSet returnResult = new ResultSet();
        ArrayList<Task> tempList = allLists.getTaskList();
        
        if(validOp.isValidTask(lineNum, allLists)) {
			Task tempTask = new Task();
			int taskID = validOp.checkForTaskID(lineNum, allLists);
			for(int i = 0; i < tempList.size(); i++) {
				tempTask = tempList.get(i);
				if(taskID == tempTask.getTaskID()) {
					tempTask.setTaskStatus(STATUS_TASK);
					returnResult.setOpStatus(String.format(MESSAGE_SUCCESS));
				}
			}	
		} else {
			returnResult.setOpStatus(MESSAGE_INVALID_TASK);
		}
        
        allLists.setTaskList(tempList);
        returnResult.setReturnList(allLists.getTaskList());
        return returnResult;
    }
}
