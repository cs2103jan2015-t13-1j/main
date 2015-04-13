package organizer.logic;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import organizer.parser.CommandParser;

//@author A0098824N
public class DeadlineTask {
	private static final String MESSAGE_INVALID_TASK = "Selected task does not exists!";
	private static final String MESSAGE_INVALID_CONTENT = "Deadline a task operation failed for invalid content!";
	private static final String MESSAGE_TYPE_CHANGED= "Deadline a task operation is successful! Changed to DEADLINE task.";
	private static final String MESSAGE_INVALID_TYPE = "Selected task is already a deadline task!";

	private static final String TYPE_DEADLINE = "DEADLINE";
	private static final String TIME_DEFAULT = "23:59";

	private static final String PATTERN_DEADLINE_ENDDATETIME = "([0-9]+)(\\s)(\\bby\\b)(\\s)(((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]))(\\s)(([01]?[0-9]|2[0-3]):([0-5][0-9]))";
	private static final String PATTERN_DEADLINE_ENDDATE = "([0-9]+)(\\s)(\\bby\\b)(\\s)(((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]))";
	private static final String PATTERN_DEADLINE_ENDDAY = "([0-9]+)(\\s)(\\bby\\b)(\\s)(monday|tuesday|wednesday|thursday|friday|saturday|sunday|mon|tues|wed|thurs|fri|sat|sun|today|tomorrow)";
	private static final String PATTERN_DEADLINE_ENDDAYTIME = "([0-9]+)(\\s)(\\bby\\b)(\\s)(monday|tuesday|wednesday|thursday|friday|saturday|sunday|mon|tues|wed|thurs|fri|sat|sun|today|tomorrow)(\\s)(([01]?[0-9]|2[0-3]):([0-5][0-9]))";

	private Matcher DEADLINE_ENDDATETIME, DEADLINE_ENDDATE, DEADLINE_ENDDAY, DEADLINE_ENDDAYTIME;
	private DateAndTime dtCheck = new DateAndTime();

	private boolean isReadyToDeadline = false;
	private boolean isValidLineNum = false;
	private boolean isDeadlined = false;
	private boolean isValidType = false;

	private int taskID = -1;

	public ResultSet execute(String userContent, TaskListSet allLists, Validation validOp) {
		ResultSet returnResult = new ResultSet();
		loadMatchers(userContent);
		checkForMatch(userContent);
		runEditProcess(userContent, allLists, returnResult, validOp);

		returnResult.setReturnList(allLists.getTaskList());
		returnResult.setCommandType(CommandParser.COMMAND_TYPE.DEADLINE_TASK);

		return returnResult;
	}

	private void loadMatchers(String userContent) {
		DEADLINE_ENDDATETIME = Pattern.compile(PATTERN_DEADLINE_ENDDATETIME).matcher(userContent);
		DEADLINE_ENDDATE = Pattern.compile(PATTERN_DEADLINE_ENDDATE).matcher(userContent);
		DEADLINE_ENDDAY = Pattern.compile(PATTERN_DEADLINE_ENDDAY).matcher(userContent);
		DEADLINE_ENDDAYTIME = Pattern.compile(PATTERN_DEADLINE_ENDDAYTIME).matcher(userContent);
	}

	private void checkForMatch(String userContent) {
		if(DEADLINE_ENDDATETIME.matches() || DEADLINE_ENDDATE.matches()
				|| DEADLINE_ENDDAY.matches() || DEADLINE_ENDDAYTIME.matches()) {

			isReadyToDeadline = true;
		} else {
			isReadyToDeadline = false;
		}
	}

	private void runEditProcess(String userContent, TaskListSet allLists, ResultSet returnResult, Validation validOp) {
		Task tempTask = new Task();

		if(isReadyToDeadline) {
			int lineNum = Integer.parseInt(userContent.substring(0, userContent.indexOf(" ")));
			checkValidTaskID(lineNum, allLists, validOp);
		} else {
			returnResult.setOpStatus(MESSAGE_INVALID_CONTENT);
		}
		if(isReadyToDeadline && isValidLineNum) {
			tempTask = getToDeadlineTask(taskID, allLists);
		}

		if(isReadyToDeadline && isValidLineNum && isValidType) {
			matchEditProcess(userContent, tempTask);
		} else if(isReadyToDeadline && isValidLineNum && !isValidType) {
			returnResult.setOpStatus(MESSAGE_INVALID_TYPE);
		} else if(isReadyToDeadline && !isValidLineNum) {
			returnResult.setOpStatus(MESSAGE_INVALID_TASK);

		}

		if(isReadyToDeadline && isValidLineNum && isDeadlined && isValidType) {
			returnResult.setOpStatus(MESSAGE_TYPE_CHANGED);
			isReadyToDeadline = false;
			isValidLineNum = false;
			isDeadlined = false;
			isValidType = false;
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

	private Task getToDeadlineTask(int taskID, TaskListSet allLists) {
		Task tempTask = null;
		for(int index = 0; index < allLists.getTaskList().size(); index++) {
			tempTask = allLists.getTaskList().get(index);
			if(taskID == tempTask.getTaskID()) {
				break;
			} 
		}
		if(tempTask.getTaskType().equals(TYPE_DEADLINE)) {
			isValidType = false;
		} else {
			tempTask.setTaskEndDate(null);
			tempTask.setTaskEndTime(null);
			tempTask.setTaskStartTime(null);
			tempTask.setTaskStartDate(null);
			tempTask.setTaskType(TYPE_DEADLINE);
			isValidType = true;
		}

		return tempTask;
	}

	private void matchEditProcess(String editInfo, Task tempTask) {
		if(DEADLINE_ENDDATETIME.matches()) {
			setEndDateTime(editInfo, tempTask);
		} else if(DEADLINE_ENDDATE.matches()) {
			setEndDate(editInfo, tempTask);
		} else if(DEADLINE_ENDDAY.matches()) {
			setEndDay(editInfo, tempTask);
		} else if(DEADLINE_ENDDAYTIME.matches()) {
			setEndDayTime(editInfo, tempTask);
		}

		isDeadlined = true;
	}

	private void setEndDateTime(String userContent, Task tempTask) {
		tempTask.setTaskEndDate(dtCheck.toValidDate(DEADLINE_ENDDATETIME.group(5)));
		tempTask.setTaskEndTime(dtCheck.determineHour(DEADLINE_ENDDATETIME.group(11)));

	}

	private void setEndDate(String userContent, Task tempTask) {
		tempTask.setTaskEndDate(dtCheck.toValidDate(DEADLINE_ENDDATE.group(5)));
		tempTask.setTaskEndTime(LocalTime.parse(TIME_DEFAULT));
	}

	private void setEndDay(String userContent, Task tempTask) {
		tempTask.setTaskEndDate(dtCheck.determineDate(DEADLINE_ENDDAY.group(5)));
		tempTask.setTaskEndTime(LocalTime.parse(TIME_DEFAULT));
	}

	private void setEndDayTime(String userContent, Task tempTask) {
		tempTask.setTaskEndDate(dtCheck.determineDate(DEADLINE_ENDDAYTIME.group(5)));
		tempTask.setTaskEndTime(dtCheck.determineHour(DEADLINE_ENDDAYTIME.group(7)));
	}


}
