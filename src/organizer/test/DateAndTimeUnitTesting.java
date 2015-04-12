package organizer.test;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;

import organizer.logic.DateAndTime;

public class DateAndTimeUnitTesting {

	private DateAndTime dtChecker = new DateAndTime();
	@Test
	public void testIsValidDueDT() {
		boolean expected = false;
		boolean output  = dtChecker.isValidDueDT(LocalDate.of(2015, 4, 1), LocalDate.of(2015, 3, 19), LocalTime.of(19, 00), LocalTime.of(20, 00));
		assertEquals(expected, output);
	}

	@Test
	public void testDetermineHour() {
		fail("Not yet implemented");
	}

	@Test
	public void testToValidDate() {
		fail("Not yet implemented");
	}

	@Test
	public void testDetermineDate() {
		fail("Not yet implemented");
	}

	@Test
	public void testDetermineDay() {
		fail("Not yet implemented");
	}

}
