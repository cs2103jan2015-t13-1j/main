package organizer.logic;

import java.util.ArrayList;

import organizer.parser.CommandParser;

//@author A0113871J
public class SearchTask {
	private static final String MESSAGE_SEARCH_FOUND = "Search results found: \"%1$s\"";
	private static final String MESSAGE_NO_RESULT = "No results found!";
	ResultSet returnResult = new ResultSet();
	
	public ResultSet execute(String searchTerm, TaskListSet allLists) {
		ArrayList<Task> tempList = new ArrayList<Task>();
		allLists.setResultList(tempList);
		
		for(int i = 0; i < allLists.getTaskList().size(); i++) {
			Task task = allLists.getTaskList().get(i);
			if(task.getTaskName().toLowerCase().contains(searchTerm.trim().toLowerCase())) {
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
		returnResult.setCommandType(CommandParser.COMMAND_TYPE.SEARCH_TASK);
		return returnResult;
	}
}
