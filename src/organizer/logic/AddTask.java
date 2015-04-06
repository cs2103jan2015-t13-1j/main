package organizer.logic;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddTask {
	private static final String PATTERN_DEADLINE_DATEONLY = "((19|20\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]))";

	private static final String PATTERN_DEADLINE_DAYASDATE = "monday|tuesday|wednesday|thursday|friday|saturday|sunday|mon|tues|wed|thurs|fri|sat|sun|today|tomorrow";

	private static final String KEYWORD_DEADLINE = " by ";
	private static final String TYPE_DEADLINE = "DEADLINE";
	private static final String TYPE_FLOATING = "FLOATING";
	private static final String TYPE_TIMED = "TIMED";
	private static final String TIME_DEADLINE ="23:59";
	
	private static final String KEYWORD_TIMED_DATE = " on ";
//	private static final String KEYWORD_TIMED_START = " from ";
//	private static final String KEYWORD_TIMED_END = " to ";
	private static final String KEYWORD_TIMED_TODAY = " today ";
	private static final String KEYWORD_TIMED_TMRW = " tomorrow ";
	
	//add {taskname} {on} {date} {from} {time} {to} {time}
	private static final String PATTERN_TIMED_STARTEND_1DATE = "((19|20\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]))(\\s)(\\bfrom\\b)(\\s)(([01]?[0-9]|2[0-3]):([0-5][0-9]))(\\s)(\\bto\\b)(\\s)(([01]?[0-9]|2[0-3]):([0-5][0-9]))";
	//add {taskname} {on} {mon-sun} {from} {time} {to} {time}
	private static final String PATTERN_TIMED_STARTEND_1DAY = "(\\w+)(\\s)(\\bfrom\\b)(\\s)(([01]?[0-9]|2[0-3]):([0-5][0-9]))(\\s)(\\bto\\b)(\\s)(([01]?[0-9]|2[0-3]):([0-5][0-9]))";	
	//add {taskname} {today|tomorrow} {from} {time} {to} {time}
	private static final String PATTERN_TIMED_STARTEND_TODAYTMRW = "(today|tomorrow)(\\s)(\\bfrom\\b)(\\s)(([01]?[0-9]|2[0-3]):([0-5][0-9]))(\\s)(\\bto\\b)(\\s)(([01]?[0-9]|2[0-3]):([0-5][0-9]))";
	//add {taskname} {on} {date} {from} {time} {to} {date} {time}
	private static final String PATTERN_TIMED_STARTEND_2DATE = "((19|20\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]))(\\s)(\\bfrom\\b)(\\s)(([01]?[0-9]|2[0-3]):([0-5][0-9]))(\\s)(\\bto\\b)(\\s)((19|20\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]))(\\s)(([01]?[0-9]|2[0-3]):([0-5][0-9]))";
	//add {taskname} {on} {date} {time}
	private static final String PATTERN_TIMED_START_DATE = "((19|20\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]))(\\s)(([01]?[0-9]|2[0-3]):([0-5][0-9]))";
	//add {taskname} {on} {mon-sun} {time}
	private static final String PATTERN_TIMED_START_DAY = "(\\w+)(\\s)(([01]?[0-9]|2[0-3]):([0-5][0-9]))";
	//add {taskname} {today|tomorrow} {time}
	private static final String PATTERN_TIMED_START_TODAYTMRW = "(today|tomorrow)(\\s)(([01]?[0-9]|2[0-3]):([0-5][0-9]))";

	private static final String MESSAGE_SUCCESS = "Add task(s) operation is successful!\n\n";
	//private static final String MESSAGE_UNSUCCESS = "Add task(s) operation is unsuccessful!\n\n";
	private DateAndTime dtCheck = new DateAndTime();


	
	public ResultSet execute(String taskInfo, ArrayList<Task> taskList) {
		String taskName = null;
		String taskDateTime = null;
		int taskID = taskList.size();
		Task tempItem = new Task();
		ResultSet returnResult = new ResultSet();
		
		if(taskInfo.contains(KEYWORD_DEADLINE)) {
			taskName = taskInfo.substring(0, taskInfo.lastIndexOf(KEYWORD_DEADLINE)).trim();
			taskDateTime = taskInfo.substring(taskInfo.lastIndexOf(KEYWORD_DEADLINE)+KEYWORD_DEADLINE.length()).trim();
			tempItem = addDeadlineTask(taskName, taskDateTime, taskID);
		} else if(taskInfo.contains(KEYWORD_TIMED_DATE)) {
			taskName = taskInfo.substring(0, taskInfo.lastIndexOf(KEYWORD_TIMED_DATE)).trim();
			taskDateTime = taskInfo.substring(taskInfo.lastIndexOf(KEYWORD_TIMED_DATE)+KEYWORD_TIMED_DATE.length()).trim();
			tempItem = addTimedTask_DATEDAY(taskName, taskDateTime, taskID);
		} else if(taskInfo.contains(KEYWORD_TIMED_TODAY)) {
			taskName = taskInfo.substring(0, taskInfo.lastIndexOf(KEYWORD_TIMED_TODAY)).trim();
			taskDateTime = taskInfo.substring(taskInfo.lastIndexOf(KEYWORD_TIMED_TODAY)).trim();
			tempItem =  addTimedTask_TODAYTMRW(taskName, taskDateTime, taskID);
			
		} else if(taskInfo.contains(KEYWORD_TIMED_TMRW)) {
			taskName = taskInfo.substring(0, taskInfo.lastIndexOf(KEYWORD_TIMED_TMRW)).trim();
			taskDateTime = taskInfo.substring(taskInfo.lastIndexOf(KEYWORD_TIMED_TMRW)).trim();
			tempItem =  addTimedTask_TODAYTMRW(taskName, taskDateTime, taskID);
		} else {
			taskName = taskInfo;
			tempItem = addFloatingTask(taskName, taskID);
		}
		
		
		taskList.add(tempItem);
		returnResult.setOpStatus(MESSAGE_SUCCESS);
		returnResult.setReturnList(taskList);
		
		return returnResult;
	}
	
	public Task addDeadlineTask(String taskName, String taskDateTime, int taskID) {
		Pattern dateOnly;
		Matcher dateOnlyMatch;
		
		dateOnly = Pattern.compile(PATTERN_DEADLINE_DATEONLY);	
		dateOnlyMatch = dateOnly.matcher(taskDateTime);

		Task deadlineTask = new Task();
		
		if(dateOnlyMatch.matches()) {
			deadlineTask.setTaskEndDate(LocalDate.parse(taskDateTime));
		} else if(taskDateTime.matches(PATTERN_DEADLINE_DAYASDATE)) {
			deadlineTask.setTaskEndDate(dtCheck.determineDate(taskDateTime));
		} else {
			taskName = taskName.concat(" "+taskDateTime);
		}
		
		deadlineTask.setTaskName(taskName);
		deadlineTask.setTaskID(taskID);
		deadlineTask.setTaskEndTime(LocalTime.parse(TIME_DEADLINE));
		deadlineTask.setTaskType(TYPE_DEADLINE);
		
		return deadlineTask;
		
	}
	
	public Task addFloatingTask(String taskName, int taskID) {
		Task floatTask = new Task();
		floatTask.setTaskName(taskName);
		floatTask.setTaskID(taskID);
		floatTask.setTaskType(TYPE_FLOATING);
		
		return floatTask;
	}
	
	public Task addTimedTask_DATEDAY(String taskName, String taskDateTime, int taskID) {
		Task timedTask = new Task();
		Matcher TIMED_STARTEND_2DATE;
		Matcher TIMED_STARTEND_1DATE;
		Matcher TIMED_STARTEND_1DAY;
		Matcher TIMED_START_DATE;
		Matcher TIMED_START_DAY;
		
		TIMED_STARTEND_2DATE = Pattern.compile(PATTERN_TIMED_STARTEND_2DATE).matcher(taskDateTime);
		TIMED_STARTEND_1DATE = Pattern.compile(PATTERN_TIMED_STARTEND_1DATE).matcher(taskDateTime);
		TIMED_STARTEND_1DAY = Pattern.compile(PATTERN_TIMED_STARTEND_1DAY).matcher(taskDateTime);
		TIMED_START_DATE = Pattern.compile(PATTERN_TIMED_START_DATE).matcher(taskDateTime);
		TIMED_START_DAY = Pattern.compile(PATTERN_TIMED_START_DAY).matcher(taskDateTime);
		
		if(TIMED_STARTEND_1DATE.matches()) {
			timedTask.setTaskStartDate(LocalDate.parse(TIMED_STARTEND_1DATE.group(1)));
			timedTask.setTaskEndDate(timedTask.getTaskStartDate());
			timedTask.setTaskStartTime(dtCheck.determineHour(TIMED_STARTEND_1DATE.group(8)));
			timedTask.setTaskEndTime(dtCheck.determineHour(TIMED_STARTEND_1DATE.group(14)));
			
		} else if(TIMED_STARTEND_1DAY.matches()) {
			timedTask.setTaskStartDate(dtCheck.determineDate(TIMED_STARTEND_1DAY.group(1)));
			timedTask.setTaskEndDate(timedTask.getTaskStartDate());
			timedTask.setTaskStartTime(dtCheck.determineHour(TIMED_STARTEND_1DAY.group(5)));
			timedTask.setTaskEndTime(dtCheck.determineHour(TIMED_STARTEND_1DAY.group(11)));
			
		} else if(TIMED_STARTEND_2DATE.matches()) {
			timedTask.setTaskStartDate(LocalDate.parse(TIMED_STARTEND_2DATE.group(1)));
			timedTask.setTaskEndDate(LocalDate.parse(TIMED_STARTEND_2DATE.group(14)));
			timedTask.setTaskStartTime(dtCheck.determineHour(TIMED_STARTEND_2DATE.group(8)));
			timedTask.setTaskEndTime(dtCheck.determineHour(TIMED_STARTEND_2DATE.group(19)));
			
		} else if(TIMED_START_DATE.matches()) {
			timedTask.setTaskStartDate(LocalDate.parse(TIMED_START_DATE.group(1)));
			timedTask.setTaskStartTime(dtCheck.determineHour(TIMED_START_DATE.group(6)));
			
		} else if(TIMED_START_DAY.matches()) {
			timedTask.setTaskStartDate(dtCheck.determineDate(TIMED_START_DAY.group(1)));
			timedTask.setTaskStartTime(dtCheck.determineHour(TIMED_START_DAY.group(3)));
		} else {
			//need some debugging
		}

		timedTask.setTaskType(TYPE_TIMED);
		timedTask.setTaskName(taskName);
		return timedTask;
	}
	
	public Task addTimedTask_TODAYTMRW(String taskName, String taskDatetime, int taskID) {
		Task timedTask = new Task();
		Matcher TIMED_STARTEND_TODAYTMRW;
		Matcher TIMED_START_TODAYTMRW;
		
		TIMED_STARTEND_TODAYTMRW = Pattern.compile(PATTERN_TIMED_STARTEND_TODAYTMRW).matcher(taskDatetime);
		TIMED_START_TODAYTMRW = Pattern.compile(PATTERN_TIMED_START_TODAYTMRW).matcher(taskDatetime);
		
		if(TIMED_STARTEND_TODAYTMRW.matches()) {
			timedTask.setTaskStartDate(dtCheck.determineDate(TIMED_STARTEND_TODAYTMRW.group(1)));
			timedTask.setTaskEndDate(timedTask.getTaskStartDate());
			timedTask.setTaskStartTime(dtCheck.determineHour(TIMED_STARTEND_TODAYTMRW.group(5)));
			timedTask.setTaskEndTime(dtCheck.determineHour(TIMED_STARTEND_TODAYTMRW.group(11)));
			
		} else if(TIMED_START_TODAYTMRW.matches()) {
			timedTask.setTaskStartDate(dtCheck.determineDate(TIMED_START_TODAYTMRW.group(1)));
			timedTask.setTaskStartTime(dtCheck.determineHour(TIMED_START_TODAYTMRW.group(3)));
			
		} else {
			//need to do some debugging
		}
		
		timedTask.setTaskType(TYPE_TIMED);
		timedTask.setTaskName(taskName);
		return timedTask;

	}
}
