package organizer.logic;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import organizer.parser.CommandParser;

//@author A0098824N
public class EditTask {
	private static final String MESSAGE_INVALID_TASK = "Selected task does not exists!";
	private static final String MESSAGE_INVALID_CONTENT = "Edit task operation failed for invalid content!";
	private static final String MESSAGE_SUCCESS = "Edit task operation is successful!";
	private static final String MESSAGE_UNSUCCESS = "Edit task operation failed for end date/time error!";
	private static final String MESSAGE_TYPE_CHANGED= "Edit task operation is successful! Changed to %s task.";


	private static final String PATTERN_EDIT_STARTENDDATETIME = "(\\d)(\\s)(\\bfrom\\b)(\\s)(((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]))(\\s)(([01]?[0-9]|2[0-3]):([0-5][0-9]))"
			+ "(\\s)(\\bto\\b)(\\s)(((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]))(\\s)(([01]?[0-9]|2[0-3]):([0-5][0-9]))";
	private static final String PATTERN_EDIT_STARTDATETIME = "(\\d)(\\s)(\\bfrom\\b)(\\s)(((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]))(\\s)(([01]?[0-9]|2[0-3]):([0-5][0-9]))";
	private static final String PATTERN_EDIT_STARTDATE = "(\\d)(\\s)(\\bfrom\\b)(\\s)(((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]))";
	private static final String PATTERN_EDIT_STARTTIME = "(\\d)(\\s)(\\bfrom\\b)(\\s)(([01]?[0-9]|2[0-3]):([0-5][0-9]))";
	private static final String PATTERN_EDIT_ENDTIME = "(\\d)(\\s)(\\bto\\b)(\\s)(([01]?[0-9]|2[0-3]):([0-5][0-9]))";
	private static final String PATTERN_EDIT_ENDDATETIME = "(\\d)(\\s)(\\bto\\b)(\\s)(((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]))(\\s)(([01]?[0-9]|2[0-3]):([0-5][0-9]))";
	private static final String PATTERN_EDIT_ENDDATE = "(\\d)(\\s)(\\bto\\b)(\\s)(((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]))";
	private static final String PATTERN_EDIT_TITLE = "(\\d)(\\s)(.*)";

	private static final String TYPE_DEADLINE = "DEADLINE";
	private static final String TYPE_FLOATING = "FLOATING";
	private static final String TYPE_TIMED = "TIMED";

	private Matcher EDIT_STARTENDDATETIME, EDIT_STARTDATETIME, EDIT_STARTDATE, EDIT_STARTTIME,
	EDIT_ENDDATETIME, EDIT_ENDDATE, EDIT_ENDTIME, EDIT_TITLE;

	private boolean isValidLineNum = false;
	private boolean isEdited= false;
	private boolean isReadyToEdit = false;
	private boolean isTaskTypeChanged = false;
	private String editedTaskType = "";
	private DateAndTime dtCheck = new DateAndTime();
	private int taskID = -1;

	public ResultSet execute(String userContent, TaskListSet allLists, Validation validOp) {
		ResultSet returnResult = new ResultSet();
		loadMatchers(userContent);
		checkForMatch(userContent);

		runEditProcess(userContent, allLists, returnResult, validOp);

		returnResult.setReturnList(allLists.getTaskList());
		returnResult.setCommandType(CommandParser.COMMAND_TYPE.EDIT_TASK);
		return returnResult;
	}

	private void runEditProcess(String userContent, TaskListSet allLists, ResultSet returnResult, Validation validOp) {
		if(isReadyToEdit) {
			int lineNum = Integer.parseInt(userContent.substring(0, userContent.indexOf(" ")));
			checkValidTaskID(lineNum, allLists, validOp);
		} else {
			returnResult.setOpStatus(MESSAGE_INVALID_CONTENT);
		}

		if(isReadyToEdit && isValidLineNum) {
			matchEditProcess(userContent, getToEditTask(taskID, allLists));
		}

		if(isReadyToEdit && isValidLineNum && isEdited) {
			returnResult.setOpStatus(MESSAGE_SUCCESS);
			isReadyToEdit = false;
			isValidLineNum = false;
			isEdited = false;

		} else if(isReadyToEdit && !isValidLineNum) {
			returnResult.setOpStatus(MESSAGE_INVALID_TASK);

		} else if(isReadyToEdit && isValidLineNum && !isEdited) {
			returnResult.setOpStatus(MESSAGE_UNSUCCESS);

		}

		if(isTaskTypeChanged) {
			returnResult.setOpStatus(String.format(MESSAGE_TYPE_CHANGED, editedTaskType));
			isTaskTypeChanged = false;
		}
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

	private void checkForMatch(String userContent) {
		if(EDIT_STARTENDDATETIME.matches() || EDIT_STARTDATETIME.matches() || EDIT_STARTDATE.matches()
				|| EDIT_STARTTIME.matches() || EDIT_ENDDATETIME.matches() || EDIT_ENDDATE.matches()
				|| EDIT_ENDTIME.matches() || EDIT_TITLE.matches()) {
			isReadyToEdit = true;
		} else {
			isReadyToEdit = false;
		}
	}

	private void checkValidTaskID(int lineNum, TaskListSet allLists, Validation validOp) {
		if(validOp.isValidTask(lineNum, allLists)) {
			isValidLineNum =  true;
			taskID = validOp.checkForTaskID(lineNum, allLists);
		} else {
			isValidLineNum = false;
		}

	}

	private Task getToEditTask(int taskID, TaskListSet allLists) {
		Task tempTask = null;
		for(int index = 0; index < allLists.getTaskList().size(); index++) {
			tempTask = allLists.getTaskList().get(index);
			if(taskID == tempTask.getTaskID()) {
				break;
			} 
		}

		return tempTask;
	}

	private void matchEditProcess(String editInfo, Task tempTask) {
		System.out.println(editInfo);
		if(EDIT_STARTENDDATETIME.matches()) {
			editStartEndDateTime(editInfo, tempTask);
		} else if(EDIT_STARTDATETIME.matches()) {
			editStartDateTime(editInfo, tempTask);
		} else if(EDIT_STARTDATE.matches()) {
			editStartDate(editInfo, tempTask);
		} else if(EDIT_STARTTIME.matches()) {
			editStartTime(editInfo, tempTask);
		} else if(EDIT_ENDDATE.matches()) {
			editEndDate(editInfo, tempTask);
		} else if(EDIT_ENDTIME.matches()) {
			editEndTime(editInfo, tempTask);
		} else if(EDIT_ENDDATETIME.matches()) {
			editEndDateTime(editInfo, tempTask);
		} else if(EDIT_TITLE.matches()) {
			editTitle(editInfo, tempTask);
		}
	}

	private void editTitle(String userContent, Task tempTask) {
		String taskTitle = EDIT_TITLE.group(3);
		tempTask.setTaskName(taskTitle);
		isEdited = true;
	}

	private void editStartEndDateTime(String userContent, Task tempTask) {
		LocalDate startDate = dtCheck.toValidDate(EDIT_STARTENDDATETIME.group(5));
		LocalTime startTime = dtCheck.determineHour(EDIT_STARTENDDATETIME.group(11));
		LocalDate endDate = dtCheck.toValidDate(EDIT_STARTENDDATETIME.group(17));
		LocalTime endTime = dtCheck.determineHour(EDIT_STARTENDDATETIME.group(23));
		System.out.println(endTime);
		if(dtCheck.isValidDueDT(startDate, endDate, startTime, endTime)) {
			if((tempTask.getTaskType().equals(TYPE_FLOATING)) || (tempTask.getTaskType().equals(TYPE_DEADLINE))) {
				tempTask.setTaskType(TYPE_TIMED);
				isTaskTypeChanged = true;
				editedTaskType = TYPE_TIMED;
			}

			isEdited = true;
			tempTask.setTaskStartDate(startDate);
			tempTask.setTaskStartTime(startTime);
			tempTask.setTaskEndDate(endDate);
			tempTask.setTaskEndTime(endTime);

		}
	}

	private void editStartDateTime(String userContent, Task tempTask) {
		LocalDate startDate = dtCheck.toValidDate(EDIT_STARTDATETIME.group(5));
		LocalTime startTime = dtCheck.determineHour(EDIT_STARTDATETIME.group(11));
		LocalDate endDate = tempTask.getTaskEndDate();
		LocalTime endTime = tempTask.getTaskEndTime();

		if(dtCheck.isValidDueDT(startDate, endDate, startTime, endTime)) {
			if((tempTask.getTaskType().equals(TYPE_FLOATING)) || (tempTask.getTaskType().equals(TYPE_DEADLINE))) {
				tempTask.setTaskType(TYPE_TIMED);
				isTaskTypeChanged = true;
				editedTaskType = TYPE_TIMED;
			}
			isEdited = true;
			tempTask.setTaskStartDate(startDate);
			tempTask.setTaskStartTime(startTime);
		}
	}

	private void editStartDate(String userContent, Task tempTask) {
		LocalDate startDate = dtCheck.toValidDate(EDIT_STARTDATE.group(5));
		LocalDate endDate = tempTask.getTaskEndDate();
		LocalTime endTime = tempTask.getTaskEndTime();
		LocalTime startTime = tempTask.getTaskStartTime();

		if(dtCheck.isValidDueDT(startDate, endDate, startTime, endTime)) {
			if((tempTask.getTaskType().equals(TYPE_FLOATING)) || (tempTask.getTaskType().equals(TYPE_DEADLINE))) {
				tempTask.setTaskType(TYPE_TIMED);
				isTaskTypeChanged = true;
				editedTaskType = TYPE_TIMED;
			} 
			isEdited = true;
			tempTask.setTaskStartDate(startDate);

		}
	}

	private void editStartTime(String userContent, Task tempTask) {
		LocalTime startTime = dtCheck.determineHour(EDIT_STARTTIME.group(5));
		LocalTime endTime = tempTask.getTaskEndTime();
		LocalDate endDate = tempTask.getTaskEndDate();
		LocalDate startDate = tempTask.getTaskStartDate();
		if(tempTask.getTaskType().equals(TYPE_TIMED)) {
			if(dtCheck.isValidDueDT(startDate, endDate, startTime, endTime)) {
				isEdited = true;
				tempTask.setTaskStartTime(startTime);
			}
		} 
	}

	private void editEndDate(String userContent, Task tempTask) {
		LocalDate endDate = dtCheck.toValidDate(EDIT_ENDDATE.group(5));
		LocalTime endTime = tempTask.getTaskEndTime();
		LocalTime startTime = tempTask.getTaskStartTime();
		LocalDate startDate = tempTask.getTaskStartDate();

		if(dtCheck.isValidDueDT(startDate, endDate, startTime, endTime)) {
			if(!tempTask.getTaskType().equals(TYPE_FLOATING)) {
				isEdited = true;
				tempTask.setTaskEndDate(endDate);
			}
		}
	}

	private void editEndTime(String userContent, Task tempTask) {
		LocalTime endTime = dtCheck.determineHour(EDIT_ENDTIME.group(5));
		LocalDate endDate = tempTask.getTaskStartDate();
		LocalDate startDate = tempTask.getTaskStartDate();
		LocalTime startTime = tempTask.getTaskStartTime();

		if(dtCheck.isValidDueDT(startDate, endDate, startTime, endTime)) {
			if(tempTask.getTaskEndDate() == null) {
				tempTask.setTaskEndDate(tempTask.getTaskStartDate());
			}

			isEdited = true;
			tempTask.setTaskEndTime(endTime);

		} else {
			isEdited = false;
		}
	}

	//only for timed tasks, else use deadline function
	private void editEndDateTime(String userContent, Task tempTask) {
		LocalDate endDate = dtCheck.toValidDate(EDIT_ENDDATETIME.group(5));
		LocalTime endTime = dtCheck.determineHour(EDIT_ENDDATETIME.group(11));
		LocalDate startDate = tempTask.getTaskStartDate();
		LocalTime startTime = tempTask.getTaskStartTime();

		if(dtCheck.isValidDueDT(startDate, endDate, startTime, endTime)) {
			if(tempTask.getTaskType().equals(TYPE_TIMED)) {
				tempTask.setTaskEndDate(endDate);
				tempTask.setTaskEndTime(endTime);
				isEdited = true;
			}

		}
	}


}


