package organizer.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import organizer.logic.ResultSet;
import organizer.logic.Task;
import organizer.parser.CommandParser;
/**
 * This class is used to test the PostponeTask class
 * 
 */
//@author A0113871J
public class PostponeCommandUnitTesting {

	CommandParser commandParser = new CommandParser();
	
	@Before
	public void initializeTest() throws Exception {
		commandParser  = new CommandParser();
	}
	
	@Test
	public void shouldPostponeByHoursKeyedIn () throws IOException {
		ResultSet resultSet;
		resultSet = commandParser.executeCommand("clear");
		resultSet = commandParser.executeCommand("add apply gym membership by 19:00");
		resultSet = commandParser.executeCommand("postpone 1 by 2 hours");
		Task resultTask = resultSet.getReturnList().get(0);
		assertEquals(LocalTime.now().plusHours(2), resultTask.getTaskEndTime());
	}
	
	
	@Test
	public void shouldPostponeByDaysKeyedIn () throws IOException {
		ResultSet resultSet;
		resultSet = commandParser.executeCommand("clear");
		resultSet = commandParser.executeCommand("add apply gym membership by 2015-04-18");
		resultSet = commandParser.executeCommand("postpone 1 by 2 days");
		Task resultTask = resultSet.getReturnList().get(0);
		assertEquals(LocalDate.now().plusDays(2), resultTask.getTaskEndDate());
	}
}
