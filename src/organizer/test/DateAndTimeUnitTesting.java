package organizer.test;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;

import organizer.logic.DateAndTime;

//@author A0098824N
public class DateAndTimeUnitTesting {

	private DateAndTime dtChecker = new DateAndTime();
	@Test
	public void testIsValidDueDT() {
		//first condition: due date is earlier than start date, vice versa
		boolean output_1  = dtChecker.isValidDueDT(LocalDate.of(2015, 4, 1), LocalDate.of(2015, 3, 19), LocalTime.of(19, 00), LocalTime.of(20, 00));
		boolean output_2 = dtChecker.isValidDueDT(LocalDate.of(2015, 3, 19), LocalDate.of(2015, 4, 1), LocalTime.of(19, 00), LocalTime.of(20, 00));

		assertFalse(output_1);
		assertTrue(output_2);

		//second condition: due date and start date is the same, but due time is earlier than start time, vice versa
		output_1 = dtChecker.isValidDueDT(LocalDate.of(2015, 3, 19), LocalDate.of(2015, 3, 19), LocalTime.of(20, 00), LocalTime.of(19, 00));
		output_2 = dtChecker.isValidDueDT(LocalDate.of(2015, 3, 19), LocalDate.of(2015, 3, 19), LocalTime.of(19, 00), LocalTime.of(20, 00));
		assertFalse(output_1);
		assertTrue(output_2);

		//third condition: due date and start date is not the same, and time is not an issue, but due date is earlier than start date, vice versa
		output_1 = dtChecker.isValidDueDT(LocalDate.of(2015, 4, 1), LocalDate.of(2015, 3, 19), LocalTime.of(19, 00), null);
		output_2 = dtChecker.isValidDueDT(LocalDate.of(2015, 3, 19), LocalDate.of(2015, 4, 1), null, LocalTime.of(19, 00));
		assertFalse(output_1);
		assertTrue(output_2);

	}

	@Test
	public void testDetermineHour() {
		//to ensure user input for time meets format requirements
		String time_input = "9:00";
		String time_expected = "09:00";
		String time_output = dtChecker.determineHour(time_input).toString();
		assertEquals(time_expected, time_output);

		time_input = "09:00";
		time_expected = "09:00";
		time_output = dtChecker.determineHour(time_input).toString();
		assertEquals(time_expected, time_output);
	}

	@Test
	public void testToValidDate() {
		//to ensure user input for date meets format requirements
		String date_input = "2015-4-01";
		String date_expected = "2015-04-01";
		String date_output = dtChecker.toValidDate(date_input).toString();
		assertEquals(date_expected, date_output);

		date_input = "2015-4-1";
		date_expected = "2015-04-01";
		date_output = dtChecker.toValidDate(date_input).toString();
		assertEquals(date_expected, date_output);
		
		date_input = "2015-04-1";
		date_expected = "2015-04-01";
		date_output = dtChecker.toValidDate(date_input).toString();
		assertEquals(date_expected, date_output);

	}

	@Test
	public void testDetermineDate() {
		//to check if day conversion to date is successful
		String day_input = "today";
		String day_expected = LocalDate.now().toString();
		String day_output = dtChecker.determineDate(day_input).toString();
		assertEquals(day_expected, day_output);
		
		day_input = "tomorrow";
		day_expected = LocalDate.now().plusDays(1).toString();
		day_output = dtChecker.determineDate(day_input).toString();
		assertEquals(day_expected, day_output);
		
		day_input = "monday";
		day_expected = LocalDate.now().plusDays(7+1 - LocalDate.now().getDayOfWeek().getValue()).toString();
		day_output = dtChecker.determineDate(day_input).toString();
		assertEquals(day_expected, day_output);
	}

	@Test
	public void testDetermineDay() {
		//to test if can grab the date of the nearest day correctly
		
		String day_input = "monday";
		String date_expected = "2015-04-20";
		String day_output = dtChecker.determineDay(day_input).toString();
		assertEquals(date_expected, day_output);
	}

}
