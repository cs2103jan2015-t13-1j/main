package organizer.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import organizer.logic.Logic;
import organizer.logic.Task;
import organizer.storage.Storage;

public class TestAll {

	Logic logic = new Logic();
	Storage storage = new Storage();
	
	static LocalDate today = LocalDate.now();
	static LocalDate tomorrow = LocalDate.now().plusDays(1);
	static LocalDate nextThursday = LocalDate.of(2015, 3, 19);

	static ArrayList<Task> taskList = new ArrayList<Task>();
	static ArrayList<Task> testList = new ArrayList<Task>();

	/*
	 * testStorage.txt file at the beginning of testing as check manually, should
	 * include the following: 
	 * id: 0 name: buy milk duedate: 2015-03-12 status: INCOMPLETE 
	 * ---- 
	 * id: 1 name: buy ice cream duedate: 2015-03-13 status: INCOMPLETE 
	 * ---- 
	 * id: 2 name: buy detergent duedate: 2015-03-19 status: INCOMPLETE 
	 * ---- 
	 * id: 3 name: buy pie duedate: 2015-04-01 status: INCOMPLETE 
	 * ---- 
	 * 
	 */
	
	public void addToTestList(){
		Task tempTask_0 = new Task(0, "buy milk", "INCOMPLETE", today);
		Task tempTask_1 = new Task(1, "buy ice cream", "INCOMPLETE", tomorrow);
		Task tempTask_2 = new Task(2, "buy detergent", "INCOMPLETE", nextThursday);
		Task tempTask_3 = new Task(3, "buy pie", "INCOMPLETE", LocalDate.of(2015, 4, 1));
		testList.add(tempTask_0);
		testList.add(tempTask_1);
		testList.add(tempTask_2);
		testList.add(tempTask_3);

	}

	public String toString(Task tempTask) {
		String taskAsString = tempTask.getTaskID()+" "+tempTask.getTaskName()+ " " 
				+tempTask.getTaskStatus()+ " "+tempTask.getDueDate();

		return taskAsString;
	}


	@Test
	public void testAddSucessfullyAddsToEndOfList() throws IOException {

		logic.executeCommand("add buy milk %today");
		logic.executeCommand("add buy ice cream %tomorrow");
		logic.executeCommand("add buy detergent %thursday");
		logic.executeCommand("add buy milk %2015-04-01");

		for(int index = 0; index < taskList.size(); index++) {
			assertEquals("New task successfully added.", toString(testList.get(index)), 
					toString(taskList.get(index)));
		}
		
		logic.executeCommand("exit");

	}
	
	public void testLoadAndSaveSuccessfullyFromAndToTextFile() throws IOException{
		taskList = storage.readFile("storage.txt");
		testList = storage.readFile("testStorage.txt");
		
		for(int index = 0; index < testList.size(); index++) {
			assertEquals("New task successfully added.", toString(testList.get(index)), 
					toString(taskList.get(index)));
		}
		
	}
	//	@Test
	//	public void testDeleteRemovesCorrectLine() {
	//
	//	}
	//
	//	@Test
	//	public void testViewMethodReturnsCompleteArrayList() {
	//
	//	}
	//
	//
	//	@Test
	//	public void testCompleteMethodMarksTaskAsComplete() {
	//		Task testTask = logic.viewList("all").get(0);
	//		logic.completeTask("bake apple pie");
	//		assertEquals("Task status changed to complete.", "COMPLETE", testTask.getTaskStatus());
	//	}
	//
	//	@Test
	//	public void testEditTaskSuccessfullyUpdates() {
	//
	//	}
	//
	//	// creates expected test data to compare against actual
	//	// produces an ArrayList
	//	@SuppressWarnings("null")
	//	@Parameters
	//	public static ArrayList<Task> taskListContainingSearchPie() {
	//		ArrayList<Task> filteredTaskListOnPie = null;
	//
	//		Task task1 = new Task(3, "buy pie", "INCOMPLETE", today);
	//		Task task2 = new Task(4, "make apple pie", "INCOMPLETE", today);
	//		filteredTaskListOnPie.add(task1);
	//		filteredTaskListOnPie.add(task2);
	//		return filteredTaskListOnPie;
	//	}
	//
	//	@Test
	//	public void testSearchMethodReturnsFilteredArrayList() {
	//		ArrayList<Task> expected = taskListContainingSearchPie();
	//		ArrayList<Task> actual = logic.searchTask("pie");
	//		for (int i = 0; i < expected.size(); i++) {
	//			assertEquals("Filtered result arrays content is identical.",
	//					expected.get(i), actual.get(i));
	//		}
	//	}
}
