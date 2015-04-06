package organizer.logic;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class PostponeTask {
	private static final String MESSAGE_SUCCESS = "Postpone task(s) operation is successful!\n\n";
	private static final String MESSAGE_INVALID_TASK = "Selected task does not exists!";

	public ResultSet execute(String taskInfo, TaskListSet allLists, Validation validOp) {	
		int lineNum = Integer.parseInt(taskInfo.substring(0, taskInfo.indexOf(" ")));
		int NumofDaysOrhours = Integer.parseInt(taskInfo.substring(taskInfo.indexOf(" ") + 1, taskInfo.indexOf(" ", taskInfo.indexOf(" ") + 1)));
		String timeIdentifier = taskInfo.substring(taskInfo.lastIndexOf(" ")).trim();
				
		ArrayList<Task> tempList = new ArrayList<Task>();
		ResultSet returnResult = new ResultSet();

		if(validOp.isValidTask(lineNum, allLists)) {
			tempList = allLists.getTaskList();
			timeIdentifier = timeIdentifier.toLowerCase();
			System.out.println(timeIdentifier);
			if (timeIdentifier.equals("hour")|| timeIdentifier.equals("hours")){
				LocalTime newDueTime = tempList.get(lineNum - 1).getTaskEndTime().plusHours(NumofDaysOrhours);	
				System.out.println(timeIdentifier);
				tempList.get(lineNum - 1).setTaskEndTime(newDueTime);
			} else if (timeIdentifier.equals( "day") || timeIdentifier.equals("days")){
				LocalDate newDueDate = tempList.get(lineNum - 1).getTaskEndDate().plusDays(NumofDaysOrhours);
				tempList.get(lineNum - 1).setTaskEndDate(newDueDate);
			} else {
				LocalDate newDueDate = tempList.get(lineNum - 1).getTaskEndDate().plusDays(NumofDaysOrhours);
				tempList.get(lineNum - 1).setTaskEndDate(newDueDate);
			}
			allLists.setTaskList(tempList);
			returnResult.setOpStatus(String.format(MESSAGE_SUCCESS));
		} else {
			returnResult.setOpStatus(MESSAGE_INVALID_TASK);
		}

		returnResult.setReturnList(allLists.getTaskList());
		return returnResult;
	}
}
