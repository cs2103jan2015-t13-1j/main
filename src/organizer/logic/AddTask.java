package organizer.logic;

import java.time.LocalDate;
import java.util.ArrayList;

public class AddTask {
	private static final String dateFieldIdentifier = "%";
	private static final String MESSAGE_SUCCESS = "%1$s task(s) operation is successful!\n\n";
	private DateAndTime dateTime = new DateAndTime();
	
	public ResultSet execute(String taskInfo, TaskListSet allLists) {
		String taskName = null;
		String taskDate = null;
		LocalDate dueDate = LocalDate.now();
		Task tempTask = new Task();
		ResultSet returnResult = new ResultSet();
		ArrayList<Task> tempList = new ArrayList<Task>();
		tempList = allLists.getTaskList();
		
		if(taskInfo.contains(dateFieldIdentifier)) {
			taskName = taskInfo.substring(0,taskInfo.indexOf(dateFieldIdentifier));
			taskDate = taskInfo.substring(taskInfo.indexOf(dateFieldIdentifier)+1);
			dueDate = dateTime.determineDate(taskDate);
		} else {
			taskName = taskInfo;
			dueDate = null;
		}
		
		tempTask.setTaskName(taskName);
		tempTask.setDueDate(dueDate);
		tempTask.setTaskStatus("INCOMPLETE");
		tempTask.setTaskPriority(null);
		tempTask.setTaskID(tempList.size());

		tempList.add(tempTask);
		tempTask = new Task();
		allLists.setTaskList(tempList);

		returnResult.setOpStatus(String.format(MESSAGE_SUCCESS,"Add"));
		returnResult.setReturnList(allLists.getTaskList());

		return returnResult;
	}
}
