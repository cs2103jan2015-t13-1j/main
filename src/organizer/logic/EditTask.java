//@author A0098824N
package organizer.logic;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditTask {
	private static final String MESSAGE_INVALID_TASK = "Selected task does not exists!";
	private static final String MESSAGE_INVALID_CONTENT = "Edit task operation failed for invalid content!";
	private static final String MESSAGE_SUCCESS = "Edit task(s) operation is successful!";


	private static final String PATTERN_EDIT_STARTENDDATETIME = "(\\d)(\\s)(\\bfrom\\b)(\\s)((19|20\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]))(\\s)(([01]?[0-9]|2[0-3]):([0-5][0-9]))"
			+ "(\\s)(\\bto\\b)(\\s)((19|20\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]))(\\s)(([01]?[0-9]|2[0-3]):([0-5][0-9]))";
	private static final String PATTERN_EDIT_STARTDATETIME = "(\\d)(\\s)(\\bfrom\\b)(\\s)((19|20\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]))(\\s)(([01]?[0-9]|2[0-3]):([0-5][0-9]))";
	private static final String PATTERN_EDIT_STARTDATE = "(\\d)(\\s)(\\bfrom\\b)(\\s)((19|20\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]))";
	private static final String PATTERN_EDIT_STARTTIME = "(\\d)(\\s)(\\bfrom\\b)(\\s)(([01]?[0-9]|2[0-3]):([0-5][0-9]))";
	private static final String PATTERN_EDIT_ENDTIME = "(\\d)(\\s)(\\bto\\b)(\\s)(([01]?[0-9]|2[0-3]):([0-5][0-9]))";
	private static final String PATTERN_EDIT_ENDDATETIME = "(\\d)(\\s)(\\bto\\b)(\\s)((19|20\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]))(\\s)(([01]?[0-9]|2[0-3]):([0-5][0-9]))";
	private static final String PATTERN_EDIT_ENDDATE = "(\\d)(\\s)(\\bto\\b)(\\s)((19|20\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]))";
	private static final String PATTERN_EDIT_TITLE = "(\\d)(\\s)(.*)";
	
	private static final String TYPE_DEADLINE = "DEADLINE";
	private static final String TYPE_FLOATING = "FLOATING";
	private static final String TYPE_TIMED = "TIMED";
	private static final String DEADLINE_TIME = "23:59";

	private Matcher EDIT_STARTENDDATETIME, EDIT_STARTDATETIME, EDIT_STARTDATE, EDIT_STARTTIME,
	EDIT_ENDDATETIME, EDIT_ENDDATE, EDIT_ENDTIME, EDIT_TITLE;
	
	private boolean isValidLineNum = false;

	public ResultSet execute(String userContent, TaskListSet allLists, Validation validOp) {
		ResultSet returnResult = new ResultSet();
		loadMatchers(userContent);
		if((editStartEndDateTime(userContent, validOp, allLists)) || (editStartDateTime(userContent, validOp, allLists))
				|| (editStartDate(userContent, validOp, allLists)) || (editStartTime(userContent, validOp, allLists))
				|| (editEndDate(userContent, validOp, allLists)) || (editEndTime(userContent, validOp, allLists))
				||(editEndDateTime(userContent, validOp, allLists)) || (editTitle(userContent, validOp, allLists))) {
			returnResult.setOpStatus(MESSAGE_SUCCESS);
		} else {
			returnResult.setOpStatus(MESSAGE_INVALID_CONTENT);
		}
		
		if(!isValidLineNum) {
			returnResult.setOpStatus(MESSAGE_INVALID_TASK);
		} else {
			isValidLineNum = false;
		}

		returnResult.setReturnList(allLists.getTaskList());
		return returnResult;
	}

	private void loadMatchers(String userContent) {
		EDIT_STARTENDDATETIME = Pattern.compile(PATTERN_EDIT_STARTENDDATETIME).matcher(userContent);
		EDIT_STARTDATETIME =  Pattern.compile(PATTERN_EDIT_STARTDATETIME).matcher(userContent);
		EDIT_STARTDATE = Pattern.compile(PATTERN_EDIT_STARTDATE).matcher(userContent);
		EDIT_STARTTIME = Pattern.compile(PATTERN_EDIT_STARTTIME).matcher(userContent);
		EDIT_ENDDATETIME = Pattern.compile(PATTERN_EDIT_ENDDATETIME).matcher(userContent);
		EDIT_ENDDATE = Pattern.compile(PATTERN_EDIT_ENDDATE).matcher(userContent);
		EDIT_ENDTIME = Pattern.compile(PATTERN_EDIT_ENDTIME).matcher(userContent);
		EDIT_TITLE = Pattern.compile(PATTERN_EDIT_TITLE).matcher(userContent);
	}

	private boolean editTitle(String userContent, Validation validOp, TaskListSet allLists) {
		Boolean isEdit = false;
		if(EDIT_TITLE.matches()) {
			int lineNum = Integer.parseInt(EDIT_TITLE.group(1));
			String taskTitle = EDIT_TITLE.group(3);
			
			if(validOp.isValidTask(lineNum, allLists)) {
				isValidLineNum = true;
				int taskID = validOp.checkForTaskID(lineNum, allLists);
				ArrayList<Task> taskList = allLists.getTaskList();
				
				for(int id = 0; id < taskList.size(); id++) {
					Task tempTask = new Task();
					tempTask = taskList.get(id);

					if((taskID == tempTask.getTaskID())) {
						tempTask.setTaskName(taskTitle);
						isEdit = true;
					}
				}
				
			}
		}
		
		return isEdit;
	}

	private boolean editStartEndDateTime(String userContent, Validation validOp, TaskListSet allLists) {
		Boolean isEdit = false;
		if(EDIT_STARTENDDATETIME.matches()) {
			int lineNum = Integer.parseInt(EDIT_STARTENDDATETIME.group(1));
			LocalDate startDate = LocalDate.parse(EDIT_STARTENDDATETIME.group(5));
			LocalTime startTime = LocalTime.parse(EDIT_STARTENDDATETIME.group(10));
			LocalDate endDate = LocalDate.parse(EDIT_STARTENDDATETIME.group(16));
			LocalTime endTime = LocalTime.parse(EDIT_STARTENDDATETIME.group(21));
			
			
			if(validOp.isValidTask(lineNum, allLists)) {
				isValidLineNum = true;
				int taskID = validOp.checkForTaskID(lineNum, allLists);
				ArrayList<Task> taskList = allLists.getTaskList();

				for(int id = 0; id < taskList.size(); id++) {
					Task tempTask = new Task();
					tempTask = taskList.get(id);

					if((taskID == tempTask.getTaskID())) {
						if((tempTask.getTaskType().equals(TYPE_FLOATING)) || (tempTask.getTaskType().equals(TYPE_DEADLINE))) {
							tempTask.setTaskStartDate(startDate);
							tempTask.setTaskStartTime(startTime);
							tempTask.setTaskEndDate(endDate);
							tempTask.setTaskEndTime(endTime);
							tempTask.setTaskType(TYPE_TIMED);
							isEdit = true;
						} else if(tempTask.getTaskType().equals(TYPE_TIMED)) {
							tempTask.setTaskStartDate(startDate);
							tempTask.setTaskStartTime(startTime);
							tempTask.setTaskEndDate(endDate);
							tempTask.setTaskEndTime(endTime);
							isEdit = true;
						} else {
							isEdit = false;
						}
					}
				}
			}
			
		} 
		return isEdit;
	}
	
	private boolean editStartDateTime(String userContent, Validation validOp, TaskListSet allLists) {
		Boolean isEdit = false;
		if(EDIT_STARTDATETIME.matches()) {
			int lineNum = Integer.parseInt(EDIT_STARTDATETIME.group(1));
			LocalDate startDate = LocalDate.parse(EDIT_STARTDATETIME.group(5));
			LocalTime startTime = LocalTime.parse(EDIT_STARTDATETIME.group(10));
			
			
			if(validOp.isValidTask(lineNum, allLists)) {
				isValidLineNum = true;
				int taskID = validOp.checkForTaskID(lineNum, allLists);
				ArrayList<Task> taskList = allLists.getTaskList();

				for(int id = 0; id < taskList.size(); id++) {
					Task tempTask = new Task();
					tempTask = taskList.get(id);

					if((taskID == tempTask.getTaskID())) {
						if((tempTask.getTaskType().equals(TYPE_FLOATING)) || (tempTask.getTaskType().equals(TYPE_DEADLINE))) {
							tempTask.setTaskStartDate(startDate);
							tempTask.setTaskStartTime(startTime);
							tempTask.setTaskType(TYPE_TIMED);
							isEdit = true;
						} else if(tempTask.getTaskType().equals(TYPE_TIMED)) {
							tempTask.setTaskStartDate(startDate);
							tempTask.setTaskStartTime(startTime);
							isEdit = true;
						} else {
							isEdit = false;
						}
					}
				}
			}
			
		} 
		return isEdit;
	}
	
	private boolean editStartDate(String userContent, Validation validOp, TaskListSet allLists) {
		Boolean isEdit = false;
		if(EDIT_STARTDATE.matches()) {
			int lineNum = Integer.parseInt(EDIT_STARTDATE.group(1));
			LocalDate startDate = LocalDate.parse(EDIT_STARTDATE.group(5));
	
			if(validOp.isValidTask(lineNum, allLists)) {
				isValidLineNum = true;
				int taskID = validOp.checkForTaskID(lineNum, allLists);
				ArrayList<Task> taskList = allLists.getTaskList();

				for(int id = 0; id < taskList.size(); id++) {
					Task tempTask = new Task();
					tempTask = taskList.get(id);

					if((taskID == tempTask.getTaskID())) {
						if((tempTask.getTaskType().equals(TYPE_FLOATING)) || (tempTask.getTaskType().equals(TYPE_DEADLINE))) {
							tempTask.setTaskStartDate(startDate);
							tempTask.setTaskType(TYPE_TIMED);
							isEdit = true;
						} else if(tempTask.getTaskType().equals(TYPE_FLOATING)) {
							tempTask.setTaskStartDate(startDate);
							isEdit = true;
						} else {
							isEdit = false;
						}
					}
				}
			}
			
		} 
		return isEdit;
	}

	private boolean editStartTime(String userContent, Validation validOp, TaskListSet allLists) {
		Boolean isEdit = false;
		if(EDIT_STARTTIME.matches()) {
			int lineNum = Integer.parseInt(EDIT_STARTTIME.group(1));
			LocalTime startTime = LocalTime.parse(EDIT_STARTTIME.group(5));
	
			if(validOp.isValidTask(lineNum, allLists)) {
				isValidLineNum = true;
				int taskID = validOp.checkForTaskID(lineNum, allLists);
				ArrayList<Task> taskList = allLists.getTaskList();

				for(int id = 0; id < taskList.size(); id++) {
					Task tempTask = new Task();
					tempTask = taskList.get(id);

					if((taskID == tempTask.getTaskID())) {
						if(tempTask.getTaskType().equals(TYPE_TIMED)) {
							tempTask.setTaskStartTime(startTime);
							isEdit = true;
						} else {
							isEdit = false;
						}
					}
				}
			}
			
		} 
		return isEdit;
	}

	private boolean editEndDate(String userContent, Validation validOp, TaskListSet allLists) {
		Boolean isEdit = false;
		if(EDIT_ENDDATE.matches()) {
			int lineNum = Integer.parseInt(EDIT_ENDDATE.group(1));
			LocalDate endDate = LocalDate.parse(EDIT_ENDDATE.group(5));
	
			if(validOp.isValidTask(lineNum, allLists)) {
				isValidLineNum = true;
				int taskID = validOp.checkForTaskID(lineNum, allLists);
				ArrayList<Task> taskList = allLists.getTaskList();

				for(int id = 0; id < taskList.size(); id++) {
					Task tempTask = new Task();
					tempTask = taskList.get(id);

					if((taskID == tempTask.getTaskID())) {
						if(tempTask.getTaskType().equals(TYPE_FLOATING)) {
							tempTask.setTaskEndDate(endDate);
							tempTask.setTaskEndTime(LocalTime.parse(DEADLINE_TIME));
							tempTask.setTaskType(TYPE_DEADLINE);
							isEdit = true;
						} else if((tempTask.getTaskType().equals(TYPE_DEADLINE)) || (tempTask.getTaskType().equals(TYPE_TIMED))) {
							tempTask.setTaskEndDate(endDate);
							isEdit = true;
						} else {
							isEdit = false;
						}
					}
				}
			}
			
		} 
		return isEdit;
	}
	
	private boolean editEndTime(String userContent, Validation validOp, TaskListSet allLists) {
		Boolean isEdit = false;
		if(EDIT_ENDTIME.matches()) {
			int lineNum = Integer.parseInt(EDIT_ENDTIME.group(1));
			LocalTime endTime = LocalTime.parse(EDIT_ENDTIME.group(5));
	
			if(validOp.isValidTask(lineNum, allLists)) {
				isValidLineNum = true;
				int taskID = validOp.checkForTaskID(lineNum, allLists);
				ArrayList<Task> taskList = allLists.getTaskList();

				for(int id = 0; id < taskList.size(); id++) {
					Task tempTask = new Task();
					tempTask = taskList.get(id);

					if((taskID == tempTask.getTaskID())) {
						if((tempTask.getTaskType().equals(TYPE_TIMED)) || (tempTask.getTaskType().equals(TYPE_DEADLINE)))  {
							tempTask.setTaskEndTime(endTime);
							isEdit = true;
						} else {
							isEdit = false;
						}
					}
				}
			}
			
		} 
		return isEdit;
	}
	
	private boolean editEndDateTime(String userContent, Validation validOp, TaskListSet allLists) {
		Boolean isEdit = false;
		if(EDIT_ENDDATETIME.matches()) {
			int lineNum = Integer.parseInt(EDIT_ENDDATETIME.group(1));
			LocalDate endDate = LocalDate.parse(EDIT_ENDDATETIME.group(5));
			LocalTime endTime = LocalTime.parse(EDIT_ENDDATETIME.group(10));
	
			if(validOp.isValidTask(lineNum, allLists)) {
				isValidLineNum = true;
				int taskID = validOp.checkForTaskID(lineNum, allLists);
				ArrayList<Task> taskList = allLists.getTaskList();

				for(int id = 0; id < taskList.size(); id++) {
					Task tempTask = new Task();
					tempTask = taskList.get(id);

					if((taskID == tempTask.getTaskID())) {
						if(tempTask.getTaskType().equals(TYPE_FLOATING)) {
							tempTask.setTaskEndDate(endDate);
							tempTask.setTaskEndTime(endTime);
							tempTask.setTaskType(TYPE_DEADLINE);
							isEdit = true;
						} else if((tempTask.getTaskType().equals(TYPE_DEADLINE)) || (tempTask.getTaskType().equals(TYPE_TIMED))) {
							tempTask.setTaskEndDate(endDate);
							tempTask.setTaskEndTime(endTime);
							isEdit = true;
						} else {
							isEdit = false;
						}
					}
				}
			}
			
		} 
		return isEdit;
	}
}
