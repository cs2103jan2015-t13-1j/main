package organizer.test;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import organizer.logic.EditTask;
import organizer.logic.ResultSet;
import organizer.logic.Task;
import organizer.logic.TaskListSet;
import organizer.logic.Validation;

//@author A0113627L
@RunWith(Parameterized.class)
public class EditCommandUnitTesting {

	@Parameterized.Parameters
	public static List<Object[]> getParameters() {
		final LocalDate startDate = LocalDate.now(), endDate = startDate;
		return Arrays.asList(new Object[][]{
				{	// test 1
					new Task(0, "b", null, null, null, null, "FLOATING"),
					new Task(0, "a", null, null, null, null, "FLOATING"),
					"1 b"
				},
				{	// test 2
					new Task(0, "a", startDate, null, endDate, LocalTime.of(19, 0), "TIMED"),
					new Task(0, "a", startDate, null, endDate, LocalTime.of(11, 11), "TIMED"),
					"1 to 19:00"
				},
				{	// test 3
					new Task(0, "a", startDate, LocalTime.of(19, 0), endDate, LocalTime.of(11, 11), "TIMED"),
					new Task(0, "a", startDate, null, endDate, LocalTime.of(20, 11), "TIMED"),
					"1 from 19:00"
				},
				{	// test 4
					new Task(0, "a", startDate, null, endDate, LocalTime.of(11, 11), "TIMED"),
					new Task(0, "a", startDate, null, endDate, LocalTime.of(11, 11), "TIMED"),
					"1 from 19:00"
				},
				{	// test 5
					new Task(0, "a", LocalDate.of(1993, 1, 1), null, LocalDate.of(2015, 4, 18), LocalTime.of(19, 0), "TIMED"),
					new Task(0, "a", LocalDate.of(1993, 1, 1), null, endDate, LocalTime.of(11, 11), "TIMED"),
					"1 to 2015-04-18 19:00"
				},
				{	// test 6
					new Task(0, "a", LocalDate.of(1993, 1, 1), null, LocalDate.of(2015, 4, 18), LocalTime.of(11, 11), "TIMED"),
					new Task(0, "a", LocalDate.of(1993, 1, 1), null, endDate, LocalTime.of(11, 11), "TIMED"),
					"1 to 2015-04-18"
				},
				{	// test 7
					new Task(0, "a", LocalDate.of(1993, 1, 1), LocalTime.of(0, 0), endDate, LocalTime.of(19, 0), "TIMED"),
					new Task(0, "a", endDate, LocalTime.of(0, 0), endDate, LocalTime.of(11, 11), "TIMED"),
					"1 from 1993-1-1 00:00"
				},
				{	// test 8
					new Task(0, "a", LocalDate.of(2014, 1, 1), LocalTime.of(0, 0), LocalDate.of(2015, 11, 12), LocalTime.of(10, 0), "TIMED"),
					new Task(0, "a", endDate, LocalTime.of(0, 0), endDate, LocalTime.of(11, 11), "TIMED"),
					"1 from 2014-1-1 00:00 to 2015-11-12 10:00"
				}
		});
	}

	private final Task expected;
	private final Task input;
	private final String command;
	
	public EditCommandUnitTesting(Task expected, Task input, String command) {
		this.expected = expected;
		this.input = input;
		this.command = command;
	}
	
	@Test
	public void test() {
		final EditTask editTask = new EditTask();
		final Validation validOp = new Validation();
		final TaskListSet set = new TaskListSet();
		set.setTaskList(new ArrayList<>(Arrays.asList(input)));
		final ResultSet rs = editTask.execute(command, set, validOp);
		final Task resultTask = rs.getReturnList().get(0);
		assertTrue(TestUtil.compareTask(resultTask, expected));
	}
}
