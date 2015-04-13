package organizer.logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import organizer.parser.CommandParser;

//@author A0113871J
public class PostponeTask {
	private static final Logger LOGGER = Logger.getLogger(PostponeTask.class.getName());
	private static final String MESSAGE_SUCCESS = "Postpone task operation is successful!";
	private static final String MESSAGE_UNSUCCESS = "Postpone task operation failed for invalid content!";
	private static final String MESSAGE_NODEADLINE = "No deadline found!";
	private static final String MESSAGE_INVALID_TASK = "Selected task does not exists!";
	private final static String PATTERN_POSTPONE = "([0-9]+)(\\s)(\\bby\\b)(\\s)(\\d)(\\s)(hours|hour|days|day|hr|hrs)";
	private final static String PATTERN_HOUR = "hour|hours|hr|hrs";
	private final static String PATTERN_DAY = "day|days";

	public ResultSet execute(String taskInfo, TaskListSet allLists, Validation validOp) {
		Matcher POSTPONE = Pattern.compile(PATTERN_POSTPONE).matcher(taskInfo);
		ResultSet returnResult = new ResultSet();
		DateAndTime dtCheck = new DateAndTime();
		
		if(POSTPONE.matches()) {
			int lineNum, NumofDaysOrhours;
			try {
				lineNum = Integer.parseInt(POSTPONE.group(1));
				NumofDaysOrhours = Integer.parseInt(POSTPONE.group(5));
			} catch (NumberFormatException e) {
				LOGGER.throwing(getClass().getName(), "execute", e);
				LOGGER.severe("Invalid format of postpone command, but regex check did not detect it");
				throw e;
			}
			String timeIdentifier = POSTPONE.group(7);
			assert timeIdentifier.length() > 0;
					
			Task tempTask = null;
			
			if(validOp.isValidTask(lineNum, allLists)) {
				int taskID = validOp.checkForTaskID(lineNum, allLists);
				assert taskID >= 0;
				
				for(Task task : allLists.getTaskList()) {
					if(taskID == task.getTaskID()) {
						tempTask = task;
						break;
					}
				}
				assert tempTask != null;
				
				LocalDate startDate = tempTask.getTaskStartDate();
				LocalDate endDate = tempTask.getTaskEndDate();
				LocalTime startTime = tempTask.getTaskStartTime();
				LocalTime endTime = tempTask.getTaskEndTime();
				if (endDate == null && endTime == null) {
					returnResult.setOpStatus(MESSAGE_NODEADLINE);
				} else {
					if(endTime != null && timeIdentifier.matches(PATTERN_HOUR)) {
						int offsetHour = endTime.getHour() + NumofDaysOrhours;
						int offsetDay = offsetHour / 24;
						offsetHour %= 24;
						endDate = endDate.plusDays(offsetDay);
						endTime = LocalTime.of(offsetHour, endTime.getMinute());
					} else if(endDate != null && timeIdentifier.matches(PATTERN_DAY)) {
						endDate = endDate.plusDays(NumofDaysOrhours);
					}
					
					if(dtCheck.isValidDueDT(startDate, endDate, startTime, endTime)) {
						tempTask.setTaskEndDate(endDate);
						tempTask.setTaskEndTime(endTime);
						returnResult.setOpStatus(String.format(MESSAGE_SUCCESS));
						LOGGER.info(String.format("postpone %d to %s",
								taskID,
								endTime == null ?
										endDate.toString() :
										LocalDateTime.of(endDate, endTime).toString()));
					}
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
