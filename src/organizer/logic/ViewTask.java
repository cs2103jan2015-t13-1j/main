package organizer.logic;

import java.time.LocalDate;
import java.util.ArrayList;

public class ViewTask {
	private static final String MESSAGE_EMPTY_LIST = "No task(s) found!";
	private static final String MESSAGE_SUCCESS = "%1$s task(s) operation is successful!\n\n";
	
	public ResultSet execute(String viewType, TaskListSet allLists){
		LocalDate currentDate = LocalDate.now();
		viewType = viewType.toLowerCase();
		ArrayList<Task> tempList = new ArrayList<Task>();
		allLists.setViewList(tempList);
		ResultSet returnResult = new ResultSet();

		for(int i = 0; i < allLists.getTaskList().size(); i++) {
			Task task = allLists.getTaskList().get(i);
			if(task.getDueDate() == null) {
				//do nothing
			} else {
				if(viewType.trim().equals("today") && task.getDueDate().equals(currentDate)) {
					tempList.add(task);
				} else if(viewType.trim().equals("complete") && task.getTaskStatus().toLowerCase().equals("complete")) {
					tempList.add(task);
				} else if(viewType.trim().equals("incomplete") && task.getTaskStatus().toLowerCase().equals("incomplete")) {
					tempList.add(task);
				}else if (viewType.trim().equalsIgnoreCase("all")){
					tempList.add(task);
				}
			}
		}
		
		allLists.setViewList(tempList);

		if(allLists.getViewList().isEmpty()) {
			returnResult.setOpStatus(MESSAGE_EMPTY_LIST);
		} else {
			returnResult.setOpStatus(String.format(MESSAGE_SUCCESS, "View "+viewType));
		}

		returnResult.setReturnList(allLists.getViewList());
		return returnResult;

	}
}
