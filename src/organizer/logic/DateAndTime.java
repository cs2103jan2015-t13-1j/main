//@author A0098824N
package organizer.logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class DateAndTime {
	private static final int daysPerWeek = 7;
	private static final String dayPattern = "monday|tuesday|wednesday|thursday|friday|saturday|sunday|mon|tues|wed|thurs|fri|sat|sun";
	private static final String singleDigitTimeDate = "0";
	
	public boolean isValidDueDT(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
		boolean isValid = true;

		if(endDate != null && startDate != null && endTime != null && startTime != null) {
			String startDT = startDate.toString().concat("T").concat(startTime.toString());
			String endDT = endDate.toString().concat("T").concat(endTime.toString());
			LocalDateTime startDateTime = LocalDateTime.parse(startDT);
			LocalDateTime endDateTime = LocalDateTime.parse(endDT);
			
			if(startDateTime.compareTo(endDateTime) >= 0) {
				return false;
			}
			
		} else if(startTime != null && endTime != null && endDate == startDate) {
			if(startTime.compareTo(endTime) >= 0) {
				return false;
			}
			
		} else if((endDate != null && endTime != null) && (endTime == null || startTime == null)) {
			if(startDate.compareTo(endDate) >= 0) {
				return false;
			}
		}
		
		return isValid;
	}
	public LocalTime determineHour(String time) {
		if(time.substring(0, time.indexOf(":")).length() == 1) {
			time = singleDigitTimeDate.concat(time);
		}
		
		return LocalTime.parse(time);
	}
	
	public LocalDate toValidDate(String date) {
		String year = date.substring(0, date.indexOf("-"));
		String month = date.substring(date.indexOf("-")+1, date.lastIndexOf("-"));
		String day = date.substring(date.lastIndexOf("-")+1);
		
		if(month.length() == 1) {
			month = singleDigitTimeDate.concat(month);
		} 
		
		if(day.length() == 1) {
			day = singleDigitTimeDate.concat(day);
		}
		
		date = year.concat("-").concat(month).concat("-").concat(day);
		
		return LocalDate.parse(date);
	}
	
	public LocalDate determineDate(String dateInfo) {
		dateInfo = dateInfo.trim().toLowerCase();
		LocalDate taskDate;
		if(dateInfo.equals("today")) {
			taskDate = LocalDate.now();
		} else if(dateInfo.equals("tomorrow")) {
			taskDate = LocalDate.now().plusDays(1);
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
		dayMap.put("sun",8);
		dayMap.put("mon",9);
		dayMap.put("tues",10);
		dayMap.put("wed",11);
		dayMap.put("thurs",12);
		dayMap.put("fri",13);
		dayMap.put("sat",14);


		int numOfDay = dayMap.get(dayOfWeek).intValue();
		int numOfTaskDay = dayMap.get(dateInfo).intValue();
		
		if(numOfTaskDay > daysPerWeek) {
			numOfTaskDay -= daysPerWeek;
		}

		if(numOfDay == numOfTaskDay) {
			taskDate = LocalDate.now().plusDays(daysPerWeek);
		} else {
			int taskDuration = (numOfTaskDay + daysPerWeek - numOfDay) % daysPerWeek;
			taskDate = LocalDate.now().plusDays(taskDuration);
		}

		return taskDate;
	}

}
