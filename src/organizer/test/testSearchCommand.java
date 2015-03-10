package organizer.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Test;

import organizer.mainApp;
import organizer.logic.Logic;
import organizer.logic.Task;

public class testSearchCommand {
	ArrayList<Task> testList = new ArrayList<Task>();
	Logic logic = new Logic();

	
	ArrayList<Task> resultList = new ArrayList<Task>();
	
//	Task task1 = new Task(0, "do cs2103 reading", "INCOMPLETE", LocalDate.now());
//	Task task2 = new Task(1, "buy reference books", "INCOMPLETE", LocalDate.now());
	Task task2 = new Task(0, "buy fruit", "INCOMPLETE", LocalDate.now());
//	Task task3 = new Task(1, "buy chicken", "INCOMPLETE", LocalDate.now());
	
	@Test
	public void test() throws IOException {
		testList = logic.loadStorage();
		resultList.add(task2);
//		resultList.add(task3);
//		assertArrayEquals(testList.toArray(),resultList.toArray());
	//	assertTrue(resultList.equals(testList));
	//	assertEquals(resultList.get(0).getTaskID(), testList.get(0).getTaskID());
	//	assertEquals(resultList.get(0).getTaskName(), testList.get(0).getTaskName());
	//	assertEquals(resultList.get(0).getTaskID(), testList.get(0).getTaskID());
		//assertEquals(resultList.get(0).getTaskID(), testList.get(0).getTaskID());
	
		
	}



}
