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
 * This class is used to test the AddTask class
 * 
 */
//@author A0113871J
public class AddCommandUnitTesting {

	CommandParser commandParser = new CommandParser();

	@Before
	public void initializeTest() throws Exception {
		commandParser  = new CommandParser();
	}

	@Test
	public void addCommandcheckTaskNumber() throws IOException {
		ResultSet resultSet;
		resultSet = commandParser.executeCommand("clear");
		assertEquals(0, resultSet.getReturnList().size());
		resultSet = commandParser.executeCommand("add buy milk by today");
		assertEquals(1, resultSet.getReturnList().size());
		resultSet = commandParser.executeCommand("add buy books");
		assertEquals(2, resultSet.getReturnList().size());
		resultSet = commandParser.executeCommand("clear");
		assertEquals(0, resultSet.getReturnList().size());
	}

	@Test
	public void addCommandCheckTaskName() throws IOException {
		ResultSet resultSet;

		resultSet = commandParser.executeCommand("clear");	
		/*
		 * Unit testing for deadling tasks	
		 */
		resultSet = commandParser.executeCommand("add buy milk by today");
		Task resultTask = resultSet.getReturnList().get(0);
		assertEquals("buy milk", resultTask.getTaskName());
		resultSet = commandParser.executeCommand("add buy milk by 2015-04-18");
		resultTask = resultSet.getReturnList().get(1);
		assertEquals("buy milk", resultTask.getTaskName());
		resultSet = commandParser.executeCommand("add buy milk by 2015-04-18 19:00");
		resultTask = resultSet.getReturnList().get(2);
		assertEquals("buy milk", resultTask.getTaskName());
		resultSet = commandParser.executeCommand("add buy milk by monday 19:00");
		resultTask = resultSet.getReturnList().get(3);
		assertEquals("buy milk", resultTask.getTaskName());
		/*
		 * Unit testing for timed tasks	
		 */	
		resultSet = commandParser.executeCommand("add buy milk on 2015-04-18 form 19:00 to 20:00");
		resultTask = resultSet.getReturnList().get(4);
		assertEquals("buy milk", resultTask.getTaskName());
		resultSet = commandParser.executeCommand("add buy milk on monday from 19:00 to 20:00");
		resultTask = resultSet.getReturnList().get(5);
		assertEquals("buy milk", resultTask.getTaskName());
		resultSet = commandParser.executeCommand("add buy milk today from 19:00 to 20:00");
		resultTask = resultSet.getReturnList().get(6);
		assertEquals("buy milk", resultTask.getTaskName());
		resultSet = commandParser.executeCommand("add buy milk on 2015-04-18 from 19:00 to 2015-05-01 20:00");
		resultTask = resultSet.getReturnList().get(7);
		assertEquals("buy milk", resultTask.getTaskName());
		resultSet = commandParser.executeCommand("add apply gym membership from 2015-04-18 to 2015-05-01");
		resultTask = resultSet.getReturnList().get(8);
		assertEquals("apply gym membership", resultTask.getTaskName());
		resultSet = commandParser.executeCommand("add apply gym membership on 2015-04-18 19:00");
		resultTask = resultSet.getReturnList().get(9);
		assertEquals("apply gym membership", resultTask.getTaskName());
		resultSet = commandParser.executeCommand("add apply gym membership on monday 19:00");
		resultTask = resultSet.getReturnList().get(10);
		assertEquals("apply gym membership", resultTask.getTaskName());	
		resultSet = commandParser.executeCommand("add apply gym membership today");
		resultTask = resultSet.getReturnList().get(11);
		assertEquals("apply gym membership", resultTask.getTaskName());	
		resultSet = commandParser.executeCommand("add apply gym membership on 2015-04-18");
		resultTask = resultSet.getReturnList().get(12);
		assertEquals("apply gym membership", resultTask.getTaskName());	
		resultSet = commandParser.executeCommand("add apply gym membership on monday");
		resultTask = resultSet.getReturnList().get(13);
		assertEquals("apply gym membership", resultTask.getTaskName());	
		/*
		 * Unit testing for floating tasks	
		 */	
		resultSet = commandParser.executeCommand("add buy milk");
		resultTask = resultSet.getReturnList().get(14);
		assertEquals("buy milk", resultTask.getTaskName());
}

	@Test
	public void addCommandCheckTaskDateAndTime() throws IOException {
		ResultSet resultSet;
		resultSet = commandParser.executeCommand("clear");	
		/*
		 * Unit testing for deadling tasks	
		 */
		resultSet = commandParser.executeCommand("add buy milk by today");
		Task resultTask = resultSet.getReturnList().get(0);
		assertEquals(LocalDate.now(), resultTask.getTaskEndDate());
		resultSet = commandParser.executeCommand("add buy milk by 2015-04-18");
		resultTask = resultSet.getReturnList().get(1);
		assertEquals(LocalDate.of(2015, 04, 18), resultTask.getTaskEndDate());
		resultSet = commandParser.executeCommand("add buy milk by 2015-04-18 19:00");
		resultTask = resultSet.getReturnList().get(2);
		assertEquals(LocalDate.of(2015, 04, 18), resultTask.getTaskEndDate());
		assertEquals(LocalTime.of(19, 00), resultTask.getTaskEndTime());
		resultSet = commandParser.executeCommand("add buy milk by monday 19:00");
		resultTask = resultSet.getReturnList().get(3);
		assertEquals(LocalDate.of(2015, 04, 18), resultTask.getTaskEndDate());
		assertEquals(LocalTime.now(), resultTask.getTaskEndTime());
		/*
		 * Unit testing for timed tasks	
		 */		
		resultSet = commandParser.executeCommand("add buy milk on 2015-04-18 form 19:00 to 20:00");
		resultTask = resultSet.getReturnList().get(4);
		assertEquals(LocalDate.of(2015, 04, 18), resultTask.getTaskStartDate());
		assertEquals(LocalTime.of(19, 00), resultTask.getTaskStartTime());
		assertEquals(LocalTime.of(20, 00), resultTask.getTaskEndTime());
		resultSet = commandParser.executeCommand("add buy milk today from 19:00 to 20:00");
		assertEquals(LocalDate.now(), resultTask.getTaskStartDate());
		assertEquals(LocalTime.of(19, 00), resultTask.getTaskStartTime());
		assertEquals(LocalTime.of(20, 00), resultTask.getTaskEndTime());
		resultTask = resultSet.getReturnList().get(5);
		assertEquals("buy milk", resultTask.getTaskName());
		resultSet = commandParser.executeCommand("add buy milk on 2015-04-18 from 19:00 to 2015-05-01 20:00");
		resultTask = resultSet.getReturnList().get(6);
		assertEquals(LocalDate.of(2015, 04, 18), resultTask.getTaskStartDate());
		assertEquals(LocalDate.of(2015, 05, 01), resultTask.getTaskEndDate());
		assertEquals(LocalTime.of(19, 00), resultTask.getTaskStartTime());
		assertEquals(LocalTime.of(20, 00), resultTask.getTaskEndTime());
		resultSet = commandParser.executeCommand("add apply gym membership from 2015-04-18 to 2015-05-01");
		resultTask = resultSet.getReturnList().get(7);
		assertEquals(LocalDate.of(2015, 04, 18), resultTask.getTaskStartDate());
		assertEquals(LocalDate.of(2015, 05, 01), resultTask.getTaskEndDate());
		resultSet = commandParser.executeCommand("add apply gym membership on 2015-04-18 19:00");
		resultTask = resultSet.getReturnList().get(8);
		assertEquals(LocalDate.of(2015, 04, 18), resultTask.getTaskStartDate());
		assertEquals(LocalTime.of(19, 00), resultTask.getTaskStartTime());
		resultSet = commandParser.executeCommand("add apply gym membership today");
		resultTask = resultSet.getReturnList().get(9);
		assertEquals(LocalDate.now(), resultTask.getTaskStartDate());
		resultSet = commandParser.executeCommand("add apply gym membership on 2015-04-18");
		resultTask = resultSet.getReturnList().get(10);
		assertEquals(LocalDate.of(2015, 04, 18), resultTask.getTaskStartDate());
		/*
		 * Unit testing for floating tasks	
		 */	
		resultSet = commandParser.executeCommand("add buy milk");
		resultTask = resultSet.getReturnList().get(14);
		assertEquals(null, resultTask.getTaskEndDate());}

	@Test
	public void addCommandCheckTaskType() throws IOException {
		ResultSet resultSet;
		resultSet = commandParser.executeCommand("clear");
		resultSet = commandParser.executeCommand("add buy milk by today");
		Task resultTask = resultSet.getReturnList().get(0);	
		assertEquals("DEADLINE", resultTask.getTaskType());
		resultSet = commandParser.executeCommand("add buy milk");
		resultTask = resultSet.getReturnList().get(0);	
		assertEquals("FLOATING", resultTask.getTaskType());
		resultSet = commandParser.executeCommand("add buy milk on 2015-04-18 from 19:00 to 2015-05-01 20:00");
		resultTask = resultSet.getReturnList().get(0);	
		assertEquals("TIMED", resultTask.getTaskType());

	}
}
