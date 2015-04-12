package organizer.test;


import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import organizer.logic.CompleteTask;
import organizer.logic.ResultSet;
import organizer.logic.Task;
import organizer.parser.CommandParser;
/**
 * This class is used to test the CompleteTask class
 * 
 */
//@author A0113871J
public class CompleteCommandUnitTesting {
	CommandParser commandParser = new CommandParser();
	CompleteTask completeTask = new CompleteTask();
	
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
		resultSet = commandParser.executeCommand("complete 1");
		Task resultTask = resultSet.getReturnList().get(0);
		assertEquals("COMPLETE", resultTask.getTaskStatus());
		resultTask = resultSet.getReturnList().get(1);
		assertEquals("INCOMPLETE", resultTask.getTaskStatus());

	}


}
