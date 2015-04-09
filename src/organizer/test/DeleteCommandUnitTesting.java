package organizer.test;


import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import organizer.logic.ResultSet;
import organizer.logic.Task;
import organizer.parser.CommandParser;
/**
 * This class is used to test the CompleteTask class
 * 
 */
//@author A0113871J
public class DeleteCommandUnitTesting {
	CommandParser commandParser = new CommandParser();

	@Before
	public void initializeTest() throws Exception {
		commandParser  = new CommandParser();
	}
	
	
	@Test
	public void shouldChangeTheTaskStatusToComplete () throws IOException {
		ResultSet resultSet;
		resultSet = commandParser.executeCommand("clear");
		resultSet = commandParser.executeCommand("add apply gym membership by 19:00");
		resultSet = commandParser.executeCommand("add buy milk");
		resultSet = commandParser.executeCommand("delete 1");
		Task resultTask = resultSet.getReturnList().get(0);
		assertEquals("buy milk", resultTask.getTaskName());
	}
}
