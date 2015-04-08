package organizer.logic;

import java.util.ArrayList;

//@author A0098824N
public class SearchTask {
	private static final String MESSAGE_SEARCH_FOUND = "Search results found: \"%1$s\"";
	private static final String MESSAGE_NO_RESULT = "No results found!";
	ResultSet returnResult = new ResultSet();
	
	public ResultSet execute(String searchTerm, TaskListSet allLists) {
		ArrayList<Task> tempList = new ArrayList<Task>();
		allLists.setResultList(tempList);
		
		for(int i = 0; i < allLists.getTaskList().size(); i++) {
			Task task = allLists.getTaskList().get(i);
			if(task.getTaskName().contains(searchTerm.trim())) {
				tempList.add(task);
			}
		}
		
		allLists.setResultList(tempList);
		
		if(allLists.getResultList().isEmpty()) {
			returnResult.setOpStatus(MESSAGE_NO_RESULT);
		} else {
			returnResult.setOpStatus(String.format(MESSAGE_SEARCH_FOUND,searchTerm));
		}

		returnResult.setReturnList(allLists.getResultList());
		return returnResult;
	}
}
