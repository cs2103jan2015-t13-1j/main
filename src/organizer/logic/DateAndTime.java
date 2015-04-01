package organizer.logic;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class DateAndTime {
	private static final int daysPerWeek = 7;
	private static final String dayPattern = "monday|tuesday|wednesday|thursday|friday|saturday|sunday";
	private static final String datePattern = "\\d{4}-\\d{2}-\\d{2}";
	private static final String hrMinSeparator = ":";
	private static final String singleDigitTime = "0";
	DateTimeFormatter timeFormatter_24 = DateTimeFormatter.ofPattern("HH:mm");
	
	
	public LocalTime determineTime(String timeInfo) {
		LocalTime formattedTime;
		
		if(timeInfo.contains(hrMinSeparator)) {
			String timeHRS = timeInfo.substring(0, timeInfo.indexOf(hrMinSeparator));
			if(timeHRS.length() == 1) {
				timeHRS = singleDigitTime.concat(timeHRS);
			}
			String timeMINS = timeInfo.substring(timeInfo.indexOf(hrMinSeparator)+1);
			if(determineMin(timeMINS) == null || determineHour(timeHRS) == null) {
				return null;
			} else {
				formattedTime = LocalTime.parse(((String) determineHour(timeHRS)).concat(":").concat((String) determineMin(timeMINS)));
			}
			
		} else {
			if(timeInfo.length() == 1) {
				timeInfo = singleDigitTime.concat(timeInfo);
			}
			if(determineHour(timeInfo) == null) {
				return null;
			} else {
				formattedTime = LocalTime.parse(((String) determineHour(timeInfo)).concat(":00"));
			}
		}

		return formattedTime;
	}

	public Object determineHour(String timeHRS) {
		int hours = Integer.parseInt(timeHRS);
		if(hours < 0 || hours > 23) {
			return null;
		} else {
			return timeHRS;
		}
	}
	
	public Object determineMin(String timeMINS) {
		int mins = Integer.parseInt(timeMINS);
		if(mins < 0 || mins > 59) {
			return null;
		} else {
			return timeMINS;
		}
	}
	public LocalDate determineDate(String dateInfo) {
		dateInfo = dateInfo.trim().toLowerCase();
		LocalDate taskDate;
		if(dateInfo.equals("today")) {
			taskDate = LocalDate.now();
		} else if(dateInfo.equals("tomorrow")) {
			taskDate = LocalDate.now().plusDays(1);
		} else if(dateInfo.matches(datePattern)) {
			taskDate = LocalDate.parse(dateInfo);
		} else if(dateInfo.matches(dayPattern)) {
			taskDate = determineDay(dateInfo);
		} else {
			taskDate = null;
		}

		return taskDate;
	}

	public LocalDate determineDay(String dateInfo) {
		LocalDate taskDate = null;
		String dayOfWeek = LocalDate.now().getDayOfWeek().toString().toLowerCase();

		Map<String,Integer> dayMap=new HashMap<String,Integer>();

		dayMap.put("sunday",1);
		dayMap.put("monday",2);
		dayMap.put("tuesday",3);
		dayMap.put("wednesday",4);
		dayMap.put("thursday",5);
		dayMap.put("friday",6);
		dayMap.put("saturday",7);


		int numOfDay = dayMap.get(dayOfWeek).intValue();
		int numOfTaskDay = dayMap.get(dateInfo).intValue();

		if(numOfDay == numOfTaskDay) {
			taskDate = LocalDate.now().plusDays(daysPerWeek);
		} else {
			int taskDuration = (numOfTaskDay + daysPerWeek - numOfDay) % daysPerWeek;
			taskDate = LocalDate.now().plusDays(taskDuration);
		}

		return taskDate;
	}

}
