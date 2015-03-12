package organizer.test;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import organizer.logic.Logic;
import organizer.logic.Task;

public class TestAll {
	
	Logic logic = new Logic();
static LocalDate today = LocalDate.now();
	/*
	 * Storage.txt file at the beginning of testing as check manually, should
	 * include the following: 
	 * id: 0 name: buy milk duedate: 2015-03-12 status: INCOMPLETE 
	 * ---- 
	 * id: 1 name: buy ice cream duedate: 2015-03-12 status: INCOMPLETE 
	 * ---- 
	 * id: 2 name: buy detergent duedate: 2015-03-12 status: INCOMPLETE 
	 * ---- 
	 * id: 3 name: buy pie duedate: 2015-03-12 status: INCOMPLETE 
	 * ---- 
	 * id: 4 name: bake apple pie duedate: 2015-03-12 status: INCOMPLETE 
	 * ----
	 */

	

	@Test
	public void testAddSucessfullyAddsToEndOfList() {
		Task tempTask = new Task(5, "new task", "INCOMPLETE", today);
		logic.addTask("new task");
		assertEquals("New task successfully added.", tempTask, logic.viewList("all").get(1));
	}

	@Test
	public void testDeleteRemovesCorrectLine() {

	}

	@Test
	public void testViewMethodReturnsCompleteArrayList() {

	}


	@Test
	public void testCompleteMethodMarksTaskAsComplete() {
		Task testTask = logic.viewList("all").get(0);
		logic.completeTask("bake apple pie");
		assertEquals("Task status changed to complete.", "COMPLETE", testTask.getTaskStatus());
	}

	@Test
	public void testEditTaskSuccessfullyUpdates() {

	}

	// creates expected test data to compare against actual
	// produces an ArrayList
	@SuppressWarnings("null")
	@Parameters
	public static ArrayList<Task> taskListContainingSearchPie() {
		ArrayList<Task> filteredTaskListOnPie = null;

		Task task1 = new Task(3, "buy pie", "INCOMPLETE", today);
		Task task2 = new Task(4, "make apple pie", "INCOMPLETE", today);
		filteredTaskListOnPie.add(task1);
		filteredTaskListOnPie.add(task2);
		return filteredTaskListOnPie;
	}

	@Test
	public void testSearchMethodReturnsFilteredArrayList() {
		ArrayList<Task> expected = taskListContainingSearchPie();
		ArrayList<Task> actual = logic.searchTask("pie");
		for (int i = 0; i < expected.size(); i++) {
			assertEquals("Filtered result arrays content is identical.",
					expected.get(i), actual.get(i));
		}
	}
}
