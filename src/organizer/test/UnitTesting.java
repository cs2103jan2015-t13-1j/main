
package organizer.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized.Parameters;
import org.junit.Test;

import organizer.logic.*;
import organizer.parser.CommandParser;
import organizer.storage.Storage;

//@author A0135005X --unused
//Unit testing was used for previous version, not workable for V0.5
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UnitTesting {

	CommandParser commandParse = new CommandParser();
	Storage storage = new Storage();

	static LocalDate today = LocalDate.now();
	static LocalDate tomorrow = LocalDate.now().plusDays(1);
	static LocalDate nextThursday = LocalDate.of(2015, 3, 26);
	static LocalTime now = LocalTime.now();
	static LocalTime midnight = LocalTime.MIDNIGHT;

	static ArrayList<Task> testList = new ArrayList<Task>();
	static ArrayList<Task> taskList = new ArrayList<Task>();
	static String operationStatus = null;
	static ResultSet resultObj = new ResultSet(null, taskList, null);

	@Parameters
	private void addToTestList() {
		testList.clear();
		Task tempTask_0 = new Task(0, "buy milk", today, now, tomorrow, midnight, "deadline");
		Task tempTask_1 = new Task(1, "buy ice cream", tomorrow, now, nextThursday, midnight, "deadline");
		Task tempTask_2 = new Task(2, "buy detergent", tomorrow, now, nextThursday, midnight, "deadline");
		Task tempTask_3 = new Task(3, "buy pie", today, now, today, midnight, "deadline");
		testList.add(tempTask_0);
		testList.add(tempTask_1);
		testList.add(tempTask_2);
		testList.add(tempTask_3);
	}

	@Parameters
	private void addToTestListEdited() {
		testList.clear();
		Task tempTask_0 = new Task(0, "buy milk", today, now, tomorrow, midnight, "deadline");
		Task tempTask_1 = new Task(1, "buy chocolate ice cream", tomorrow, now, nextThursday, midnight, "deadline");
		Task tempTask_2 = new Task(2, "buy one bag detergent", tomorrow, now, nextThursday, midnight, "deadline");
		Task tempTask_3 = new Task(3, "buy apple pie", today, now, today, midnight, "deadline");
		testList.add(tempTask_0);
		testList.add(tempTask_1);
		testList.add(tempTask_2);
		testList.add(tempTask_3);
	}

	@Parameters
	private void addToTestListDeleted() {
		testList.clear();
		Task tempTask_0 = new Task(0, "buy milk", today, now, tomorrow, midnight, "deadline");
		Task tempTask_1 = new Task(1, "buy chocolate ice cream", tomorrow, now, nextThursday, midnight, "deadline");
		Task tempTask_2 = new Task(2, "buy one bag detergent", tomorrow, now, nextThursday, midnight, "deadline");
		testList.add(tempTask_0);
		testList.add(tempTask_1);
		testList.add(tempTask_2);
	}

	@Parameters
	private void addToTestListFiltered() {
		testList.clear();
		Task tempTask_0 = new Task(0, "buy milk", today, now, tomorrow, midnight, "deadline");
		testList.add(tempTask_0);
	}

	@Parameters
	private void addToTestListCompleted() {
		testList.clear();
		Task tempTask_0 = new Task(0, "buy milk", today, now, tomorrow, midnight, "deadline");
		Task tempTask_1 = new Task(1, "buy chocolate ice cream", tomorrow, now, nextThursday, midnight, "deadline");
		Task tempTask_2 = new Task(2, "buy one bag detergent", tomorrow, now, nextThursday, midnight, "deadline");
		Task tempTask_3 = new Task(3, "buy apple pie", today, now, today, midnight, "deadline");
		testList.add(tempTask_0);
		testList.add(tempTask_1);
		testList.add(tempTask_2);
		testList.add(tempTask_3);
	}

	private String toString(Task tempTask) {
		String taskToString = null;
		taskToString = tempTask.getTaskID() + " " + tempTask.getTaskName()
				+ " " + tempTask.getTaskStatus() + " " + tempTask.getTaskEndDate();
		return taskToString;
	}

	// ADD TASK
	// INCLUDES ADDING TASK WITHOUT DATE
	// TO TEST: public ResultSet addTask(String taskInfo) {...}
	@Test
	public void testAddSucessfullyAddsToEndOfList() throws IOException {
		taskList.clear();
		addToTestList();
		resultObj = commandParse.executeCommand("add buy milk %today");
		resultObj = commandParse.executeCommand("add buy ice cream %tomorrow");
		resultObj = commandParse.executeCommand("add buy detergent %thursday");
		resultObj = commandParse.executeCommand("add buy pie");
		taskList = resultObj.getReturnList();

		for (int index = 0; index < taskList.size(); index++) {
			assertEquals("New task successfully added.",
					toString(testList.get(index)),
					toString(taskList.get(index)));
		}
	}
	
	// CLEAR TASK LIST
	// TO TEST: public ResultSet clearCommand() {...}
	@Test
	public void clearCommandSucessfullyDeletesAllTasks() throws IOException {
		testList.clear();
		addToTestList();
		resultObj = commandParse.executeCommand("clear");
		taskList = resultObj.getReturnList();
		assertEquals("TaskList successfully emptied.", 0, taskList.size());
	}

	// COMPLETE TASK
	// TO TEST: public ResultSet completeTask(String taskInfo) {...}
	@Test
	public void testCompleteMethodMarksTaskAsComplete() {

	}

	// DETERMINE DATE
	// TO TEST: public LocalDate determineDay(String dateInfo) {...}
	@Test
	public void testDetermineDateReturnsCorrectDate() {

	}
	
	// DELETE TASK
	// TO TEST: public ResultSet deleteTask(String taskInfo) {...}
	@Test
	public void testDeleteRemovesCorrectLine() throws IOException {
		addToTestListDeleted();
		resultObj = commandParse.executeCommand("delete 4");
		taskList = resultObj.getReturnList();
		for (int index = 0; index < taskList.size(); index++) {
			assertEquals("Task 4 successfully deleted.",
					toString(testList.get(index)),
					toString(taskList.get(index)));
		}
	}

	// EDIT TASK
	// TO TEST: public ResultSet editTask(String userContent) {...}
	@Test
	public void testEditTaskSuccessfullyUpdates() throws IOException {
		addToTestListEdited();
		resultObj = commandParse.executeCommand("edit 1 %tomorrow");
		resultObj = commandParse
				.executeCommand("edit 2 buy chocolate ice cream");
		resultObj = commandParse.executeCommand("edit 3 buy one bag detergent");
		resultObj = commandParse.executeCommand("edit 4 buy apple pie");
		taskList = resultObj.getReturnList();
		for (int index = 0; index < taskList.size(); index++) {
			assertEquals("Tasks successfully edited.",
					toString(testList.get(index)),
					toString(taskList.get(index)));
		}
	}
	
	// INCOMPLETE TASK
	// TO TEST: public ResultSet incompleteCommand(String taskInfo) {...}
	@Test
	public void testIncompleteTaskChangesStatus() {
		
	}
	
	// POSTPONE TASK
	// TO TEST: public ResultSet postponeTask(String taskInfo) {...}
	
	// RANK TASK
	// TO TEST: public ResultSet rankTask(String taskInfo) {...}
	@Test
	public void testRankTaskSetsPriority() {
		
	}
	
	// SEARCH FUNCTION
	// TO TEST: public ResultSet searchTask(String searchTerm) {...}
	@Test
	public void testSearchMethodReturnsFilteredResultObject()
			throws IOException {
		addToTestListFiltered();
		resultObj = commandParse.executeCommand("search milk");
		taskList = resultObj.getReturnList();
		for (int index = 0; index < taskList.size(); index++) {
			assertEquals("Task list successfully filtered.",
					toString(testList.get(index)),
					toString(taskList.get(index)));
		}
	}
	
	private ArrayList<Task> getFullTaskList() throws IOException {
		resultObj = commandParse.executeCommand("add buy milk %today");
		resultObj = commandParse.executeCommand("add buy ice cream %tomorrow");
		resultObj = commandParse.executeCommand("add buy detergent %thursday");
		resultObj = commandParse.executeCommand("add buy pie %today");
		return resultObj.getReturnList();
	}
	
	// UNDO
	// TO TEST: public ResultSet undoCommand() {...}
	@Test
	public void testIfUndoRevertsToPreviousState() throws IOException {
		addToTestList();
		taskList.clear();
		taskList = getFullTaskList();
		resultObj = commandParse.executeCommand("delete 1");
		resultObj = commandParse.executeCommand("undo");
		taskList = resultObj.getReturnList();
		for (int index = 0; index < taskList.size(); index++) {
			assertEquals("Undo successful.",
					toString(testList.get(index)),
					toString(taskList.get(index)));
		}
	}

	// VIEW LIST
	// TO TEST: public ResultSet viewList(String viewType){...}
	@Test
	public void testViewMethodReturnsCompleteArrayList() throws IOException {
		addToTestListCompleted();
		resultObj = commandParse.executeCommand("complete 1");
		resultObj = commandParse.executeCommand("complete 2");
		resultObj = commandParse.executeCommand("complete 3");
		resultObj = commandParse.executeCommand("complete 4");
		taskList = resultObj.getReturnList();
		for (int index = 0; index < taskList.size(); index++) {
			assertEquals("All task successfully changed to completed.",
					toString(testList.get(index)),
					toString(taskList.get(index)));
		}
	}

	// WRITE STORAGE & LOAD STORAGE
	// TO TEST: public void writeStorage() throws IOException {...}
	// TO TEST: public ArrayList<Task> loadStorage() throws IOException {...}
	@Test
	public void testLoadAndSaveSuccessfullyFromAndToTextFile()
			throws IOException {
		taskList = storage.readFile("testStorage.txt");
		for (int index = 0; index < taskList.size(); index++) {
			assertEquals("New task successfully added.",
					toString(testList.get(index)),
					toString(taskList.get(index)));
		}
	}
}