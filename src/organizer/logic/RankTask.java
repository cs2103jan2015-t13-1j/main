package organizer.logic;

import java.util.ArrayList;

//@author A0098824N
public class RankTask {
	private static final String MESSAGE_INVALID_TASK = "Selected task does not exists!";
	private static final String MESSAGE_INVALID_RANK = "Invalid priority rank!";
	private static final String MESSAGE_SUCCESS = "Rank task operation is successful!";
	private static final String TYPE_REMOVE = "remove";
	
	public ResultSet execute(String taskInfo, TaskListSet allLists, Validation validOp) {
		int lineNum = Integer.parseInt(taskInfo.substring(0, taskInfo.indexOf(" ")));
		String taskRank = taskInfo.substring(taskInfo.indexOf(" ")+1);
		ArrayList<Task> tempList = allLists.getTaskList();
		ResultSet returnResult = new ResultSet();
		
		if(validOp.isValidTask(lineNum, allLists) && validOp.isValidRank(taskRank)) {
			int taskID = validOp.checkForTaskID(lineNum, allLists);
			for(int i = 0; i < tempList.size(); i++) {
				if(tempList.get(i).getTaskID() == taskID) {
					if(taskRank.equals(TYPE_REMOVE)) {
						tempList.get(i).setTaskPriority(null);
					} else {
						tempList.get(i).setTaskPriority(taskRank.toUpperCase());
					}
				}
			}
			
			allLists.setTaskList(tempList);
			returnResult.setOpStatus(String.format(MESSAGE_SUCCESS));
			
		} else if(!validOp.isValidRank(taskRank)){
			returnResult.setOpStatus(MESSAGE_INVALID_RANK);
		} else {
			returnResult.setOpStatus(MESSAGE_INVALID_TASK);
		}
		
		returnResult.setReturnList(allLists.getTaskList());
		
		return returnResult;
	}
}
