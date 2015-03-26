package organizer.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized.Parameters;
import org.junit.Test;

import organizer.logic.ResultSet;
import organizer.logic.Task;
import organizer.parser.CommandParser;
import organizer.storage.Storage;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAll {

	CommandParser commandParse = new CommandParser();
	Storage storage = new Storage();

	static LocalDate today = LocalDate.now();
	static LocalDate tomorrow = LocalDate.now().plusDays(1);
	static LocalDate nextThursday = LocalDate.of(2015, 3, 26);

	static ArrayList<Task> testList = new ArrayList<Task>();
	static ArrayList<Task> taskList = new ArrayList<Task>();
	static String operationStatus = null;
	static ResultSet resultObj = new ResultSet(null, taskList);

	@Parameters
	public void addToTestList() {
		testList.clear();
		Task tempTask_0 = new Task(0, "buy milk", "INCOMPLETE", today);
		Task tempTask_1 = new Task(1, "buy ice cream", "INCOMPLETE", tomorrow);
		Task tempTask_2 = new Task(2, "buy detergent", "INCOMPLETE", tomorrow);
		Task tempTask_3 = new Task(3, "buy pie", "INCOMPLETE", today);
		testList.add(tempTask_0);
		testList.add(tempTask_1);
		testList.add(tempTask_2);
		testList.add(tempTask_3);
	}

	@Parameters
	public void addToTestListEdited() {
		testList.clear();
		Task tempTask_0 = new Task(0, "buy milk", "INCOMPLETE", tomorrow);
		Task tempTask_1 = new Task(1, "buy chocolate ice cream", "INCOMPLETE",
				tomorrow);
		Task tempTask_2 = new Task(2, "buy one bag detergent", "INCOMPLETE",
				tomorrow);
		Task tempTask_3 = new Task(3, "buy apple pie", "INCOMPLETE", today);
		testList.add(tempTask_0);
		testList.add(tempTask_1);
		testList.add(tempTask_2);
		testList.add(tempTask_3);
	}

	@Parameters
	public void addToTestListDeleted() {
		testList.clear();
		Task tempTask_0 = new Task(0, "buy milk", "INCOMPLETE", tomorrow);
		Task tempTask_1 = new Task(1, "buy chocolate ice cream", "INCOMPLETE",
				tomorrow);
		Task tempTask_2 = new Task(2, "buy one bag detergent", "INCOMPLETE",
				tomorrow);
		testList.add(tempTask_0);
		testList.add(tempTask_1);
		testList.add(tempTask_2);
	}

	@Parameters
	public void addToTestListFiltered() {
		testList.clear();
		Task tempTask_0 = new Task(0, "buy milk", "INCOMPLETE", tomorrow);
		testList.add(tempTask_0);
	}
	
	@Parameters
	public void addToTestListCompleted() {
		testList.clear();
		Task tempTask_0 = new Task(0, "buy milk", "COMPLETE", today);
		Task tempTask_1 = new Task(1, "buy ice cream", "COMPLETE", tomorrow);
		Task tempTask_2 = new Task(2, "buy detergent", "COMPLETE", tomorrow);
		Task tempTask_3 = new Task(3, "buy pie", "COMPLETE", today);
		testList.add(tempTask_0);
		testList.add(tempTask_1);
		testList.add(tempTask_2);
		testList.add(tempTask_3);
	}

	public String toString(Task tempTask) {
		String taskToString = null;
		taskToString = tempTask.getTaskID() + " " + tempTask.getTaskName()
				+ " " + tempTask.getTaskStatus() + " " + tempTask.getDueDate();
		return taskToString;
	}

	@Test
	// WRITE STORAGE & LOAD STORAGE
	// TO TEST: public void writeStorage() throws IOException {...}
	// TO TEST: public ArrayList<Task> loadStorage() throws IOException {...}
	public void testLoadAndSaveSuccessfullyFromAndToTextFile()
			throws IOException {
		taskList = storage.readFile("testStorage.txt");
		for (int index = 0; index < taskList.size(); index++) {
			assertEquals("New task successfully added.",
					toString(testList.get(index)),
					toString(taskList.get(index)));
		}
	}

	@Test
	// ADD TASK
	// TO TEST: public ResultSet addTask(String taskInfo) {...}
	public void testAddSucessfullyAddsToEndOfList() throws IOException {
		taskList.clear();
		addToTestList();
		resultObj = commandParse.executeCommand("add buy milk %today");
		resultObj = commandParse.executeCommand("add buy ice cream %tomorrow");
		resultObj = commandParse.executeCommand("add buy detergent %thursday");
		resultObj = commandParse.executeCommand("add buy pie %2015-04-01");
		taskList = resultObj.getReturnList();

		for (int index = 0; index < taskList.size(); index++) {
			assertEquals("New task successfully added.",
					toString(testList.get(index)),
					toString(taskList.get(index)));
		}
	}

	@Test
	// EDIT TASK
	// TO TEST: public ResultSet editTask(String userContent) {...}
	public void testEditTaskSuccessfullyUpdates() throws IOException {
		addToTestListEdited();
		resultObj = commandParse.executeCommand("edit 1");
		resultObj = commandParse.executeCommand("edit 2");
		resultObj = commandParse.executeCommand("edit 3");
		resultObj = commandParse.executeCommand("edit 4");
		taskList = resultObj.getReturnList();
		for (int index = 0; index < taskList.size(); index++) {
			assertEquals("Tasks successfully edited.",
					toString(testList.get(index)),
					toString(taskList.get(index)));
		}
	}

	// DETERMINE DATE
	// TO TEST: private LocalDate determineDate(String dateInfo) {...}

	// DETERMINE DAY
	// TO TEST: private LocalDate determineDay(String dateInfo) {...}

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

	// CHECK TASK
	// TO TEST: public boolean isValidTask(int lineNum) {...}

	// CHECK TASK ID
	// TO TEST: private int checkForTaskID(int lineNum) {...}

	// REMOVE TASK
	// TO TEST: private void removeFromTaskList(int taskID) {...}

	@Test
	// COMPLETE TASK
	// TO TEST: public ResultSet completeTask(String taskInfo) {...}
	public void testCompleteMethodMarksTaskAsComplete() {
		
	}

	// CLEAR TASK
	// TO TEST: public ResultSet clearTask(){...}

	@Test
	// SEARCH FUNCTION
	// TO TEST: public ResultSet searchTask(String searchTerm) {...}
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

	@Test
	// VIEW LIST
	// TO TEST: public ResultSet viewList(String viewType){...}
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

	// POSTPONE TASK
	// TO TEST: public ResultSet postponeTask(String taskInfo) {...}

	// RANK TASK
	// TO TEST: public ResultSet rankTask(String taskInfo) {...}
}
