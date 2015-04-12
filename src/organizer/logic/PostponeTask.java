package organizer.logic;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import organizer.parser.CommandParser;

//@author A0113871J
public class PostponeTask {
	private static final String MESSAGE_SUCCESS = "Postpone task operation is successful!";
	private static final String MESSAGE_UNSUCCESS = "Postpone task operation failed for invalid content!";
	private static final String MESSAGE_NODEADLINE = "No deadline found!";
	private static final String MESSAGE_INVALID_TASK = "Selected task does not exists!";
	private final static String PATTERN_POSTPONE = "(\\d)(\\s)(\\bby\\b)(\\s)(\\d)(\\s)(hours|hour|days|day|hr|hrs)";
	private final static String PATTERN_HOUR = "hour|hours|hr|hrs";
	private final static String PATTERN_DAY = "day|days";

	public ResultSet execute(String taskInfo, TaskListSet allLists, Validation validOp) {
		Matcher POSTPONE = Pattern.compile(PATTERN_POSTPONE).matcher(taskInfo);
		ResultSet returnResult = new ResultSet();
		DateAndTime dtCheck = new DateAndTime();
		
		if(POSTPONE.matches()) {
			int lineNum = Integer.parseInt(POSTPONE.group(1));
			int NumofDaysOrhours = Integer.parseInt(POSTPONE.group(5));
			String timeIdentifier = POSTPONE.group(7);
					
			Task tempTask = new Task();
			
			if(validOp.isValidTask(lineNum, allLists)) {
				int taskID = validOp.checkForTaskID(lineNum, allLists);
				
				for(int index = 0; index < allLists.getTaskList().size(); index++) {
					if(taskID == index) {
						tempTask = allLists.getTaskList().get(index);
					}
				}
				
				LocalDate startDate = tempTask.getTaskStartDate();
				LocalDate endDate = tempTask.getTaskEndDate();
				LocalTime startTime = tempTask.getTaskStartTime();
				LocalTime endTime = tempTask.getTaskEndTime();
				
				if(endDate != null || endTime != null) {
					if(endTime != null && timeIdentifier.matches(PATTERN_HOUR)) {
						endTime = endTime.plusHours(NumofDaysOrhours);
					} else if(endDate != null && timeIdentifier.matches(PATTERN_DAY)) {
						endDate = endDate.plusDays(NumofDaysOrhours);
					}
					
					if(dtCheck.isValidDueDT(startDate, endDate, startTime, endTime)) {
						tempTask.setTaskEndDate(endDate);
						tempTask.setTaskEndTime(endTime);
						returnResult.setOpStatus(String.format(MESSAGE_SUCCESS));
					}
				} else {
					returnResult.setOpStatus(MESSAGE_NODEADLINE);
				}
					
			} else {
				returnResult.setOpStatus(MESSAGE_INVALID_TASK);
			}
		} else {
			returnResult.setOpStatus(MESSAGE_UNSUCCESS);
		}

		returnResult.setReturnList(allLists.getTaskList());
		returnResult.setCommandType(CommandParser.COMMAND_TYPE.POSTPONE_TASK);
		return returnResult;
	}
}
