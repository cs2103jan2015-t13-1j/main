package organizer.test;

import static org.junit.Assert.assertTrue;

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

@RunWith(Parameterized.class)
public class DeadlineCommandUnitTesting {

	@Parameterized.Parameters
	public static List<Object[]> getParameters() {
		final LocalDate startDate = LocalDate.now(), endDate = startDate;
		final LocalDate nearestWednesday = endDate.plusDays(3 - endDate.getDayOfWeek().getValue());
		final LocalDate nearestMonday = endDate.plusDays(1 - endDate.getDayOfWeek().getValue());
		return Arrays.asList(new Object[][]{
				{	// test 1
					new Task(0, "a", null, null, LocalDate.of(2015, 1, 3), LocalTime.of(23, 59), "DEADLINE"),
					new Task(0, "a", LocalDate.of(2015, 1, 1), null, LocalDate.of(2015, 1, 20), null, "TIMED"),
					"1 by 2015-1-3"
				},
				{	// test 2
					new Task(0, "a", null, null, nearestWednesday, LocalTime.of(23, 59), "DEADLINE"),
					new Task(0, "a", nearestMonday, null, endDate, LocalTime.of(23, 59), "TIMED"),
					"1 by wednesday"
				},
				{	// test 3
					new Task(0, "a", null, null, LocalDate.of(2015, 4, 18), LocalTime.of(19, 0), "DEADLINE"),
					new Task(0, "a", nearestMonday, null, endDate, LocalTime.of(23, 59), "TIMED"),
					"1 by 2015-04-18 19:00"
				},
				{	// test 4
					new Task(0, "a", null, null, nearestWednesday, LocalTime.of(23, 59), "DEADLINE"),
					new Task(0, "a", nearestMonday, null, endDate, LocalTime.of(7, 0), "TIMED"),
					"1 by wednesday 07:00"
				},
				{	// test 5
					new Task(0, "a", startDate, null, endDate, LocalTime.of(19, 0), "TIMED"),
					new Task(0, "a", startDate, null, endDate, LocalTime.of(11, 11), "TIMED"),
					"1 by 19:00"
				}
		});
	}

	private final Task expected;
	private final Task input;
	private final String command;
	
	public DeadlineCommandUnitTesting(Task expected, Task input, String command) {
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
