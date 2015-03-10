package organizer.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import organizer.logic.Task;

public class TestAll {
	
	// creates expected test data to compare against actual
	// produces an ArrayList
	@SuppressWarnings("null")
	@Parameters
	public static ArrayList<Task> taskListContainingSearchWordAssignment() {
		ArrayList<Task> filteredTaskListOnAssignment = null;
		Task task1 = new Task();
		filteredTaskListOnAssignment.add(task1);
		return filteredTaskListOnAssignment;
	}
	
	@Test
	public void testAddSucessfullyAddsToEndOfList() {
		
	}
	
	@Test
	public void testDeleteRemovesCorrectLine() {
		
	}
	
	@Test
	public void testViewMethodReturnsCompleteArrayList() {
		
	}
	
	@Test
	public void testSearchMethodReturnsFilteredArrayList() {
		
	}
	
	@Test
	public void testCompleteMethodMarksTaskAsComplete() {
		
	}

}
