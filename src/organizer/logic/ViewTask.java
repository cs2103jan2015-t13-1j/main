package organizer.logic;

import java.time.LocalDate;
import java.util.ArrayList;

public class ViewTask {
	private static final String MESSAGE_EMPTY_LIST = "No task(s) found!";
	private static final String MESSAGE_SUCCESS = "View %1$s task(s) operation is successful!\n\n";
	private enum ViewType {
		TODAY,
		COMPLETE,
		INCOMPLETE,
		ALL
	}

	public ResultSet execute(String viewTypeString, TaskListSet allLists){
		LocalDate currentDate = LocalDate.now();
		ArrayList<Task> tempList = new ArrayList<Task>();
		allLists.setViewList(tempList);
		ResultSet returnResult = new ResultSet();
		
		final ViewType viewType;
		switch (viewTypeString.trim().toLowerCase()) {
		case "today":
			viewType = ViewType.TODAY;
			break;
		case "complete":
			viewType = ViewType.COMPLETE;
			break;
		case "incomplete":
			viewType = ViewType.INCOMPLETE;
			break;
		case "all":
			viewType = ViewType.ALL;
			break;
		default:
			viewType = null;
			break;
		}

		for(int i = 0; i < allLists.getTaskList().size(); i++) {
			Task task = allLists.getTaskList().get(i);
			if(task.getTaskStartDate() == null) {
				tempList.add(task);
			} else {
				if(viewType == ViewType.TODAY && task.getTaskStartDate().equals(currentDate)) {
					tempList.add(task);
				}
			}

			if(viewType == ViewType.COMPLETE && task.getTaskStatus().toLowerCase().equals("complete")) {
				tempList.add(task);
			} else if(viewType == ViewType.INCOMPLETE && task.getTaskStatus().toLowerCase().equals("incomplete")) {
				tempList.add(task);
			}else if (viewType == ViewType.ALL){
				tempList.add(task);
			}

		}

		allLists.setViewList(tempList);

		if(allLists.getViewList().isEmpty()) {
			returnResult.setOpStatus(MESSAGE_EMPTY_LIST);
		} else {
			returnResult.setOpStatus(String.format(MESSAGE_SUCCESS, viewType));
		}

		returnResult.setReturnList(allLists.getViewList());
		return returnResult;

	}

}

