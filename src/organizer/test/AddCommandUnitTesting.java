package organizer.test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;
import organizer.logic.*;
import static organizer.test.TestUtil.*;
/*
 * @author A0113871
 */
@RunWith(Parameterized.class)
public class AddCommandUnitTesting {
	private final Task expected;
	private final String command;
	private final AddTask addTask = new AddTask();

	public AddCommandUnitTesting(Task expected, String command) {
		this.expected = expected;
		this.command = command;
	}

	@Parameterized.Parameters
	public static List<Object[]> getParameters() {
		return Arrays.asList(new Object[][] {
				{ // test 1
					new Task(0, "buy milk", null, null, LocalDate.now(), LocalTime.of(23, 59), "DEADLINE"),
					"buy milk by today" 
				},
				{ // test 2
					new Task(0, "buy milk", null, null, LocalDate.of(2015,04,18), LocalTime.of(23, 59), "DEADLINE"),
					"buy milk by 2015-04-18" },
				{ // test 3
					new Task(0, "buy milk", null, null, LocalDate.of(2015,04,18), LocalTime.of(19, 00), "DEADLINE"),
					"buy milk by 2015-04-18 19:00" },
				{ // test 4
					new Task(0, "buy milk", null, null, LocalDate.of(2015,04,17), LocalTime.of(19, 00), "DEADLINE"),
					"buy milk by friday 19:00" },
				{ // test 5
					new Task(0, "buy milk", LocalDate.of(2015,04,18), LocalTime.of(19, 00), LocalDate.of(2015,04,18), LocalTime.of(20, 00), "TIMED"),
					"buy milk on 2015-04-18 from 19:00 to 20:00" },
				{ // test 6
					new Task(0, "buy milk", LocalDate.of(2015,04,17), LocalTime.of(19, 00), LocalDate.of(2015,04,17), LocalTime.of(20, 00), "TIMED"),
					"buy milk on friday from 19:00 to 20:00" },
				{ // test 7
					new Task(0, "buy milk", LocalDate.now(),  LocalTime.of(19, 00), LocalDate.now(), LocalTime.of(20, 00), "TIMED"),
					"buy milk today from 19:00 to 20:00" },
				{ // test 8
					new Task(0, "buy milk", LocalDate.of(2015,04,18), LocalTime.of(19, 00), LocalDate.of(2015,05,01), LocalTime.of(20, 00), "TIMED"),
					"buy milk on 2015-04-18 from 19:00 to 2015-05-01 20:00" },
				{ // test 9
					new Task(0, "buy milk", LocalDate.of(2015,04,18), null, LocalDate.of(2015,05,01), null, "TIMED"),
					"buy milk from 2015-04-18 to 2015-05-01" },
				{ // test 10
					new Task(0, "buy milk", LocalDate.of(2015,04,18), LocalTime.of(19, 00), null, null, "TIMED"),
					"buy milk on 2015-04-18 19:00" }, 
				{ // test 11
					new Task(0, "buy milk", LocalDate.of(2015,04,17), LocalTime.of(19, 00), null, null, "TIMED"),
					"buy milk on friday 19:00" }, 
				{ // test 12
					new Task(0, "buy milk", LocalDate.now(), null, null, LocalTime.of(23, 59), "TIMED"),
					"buy milk today" }, 
				{ // test 13
					new Task(0, "buy milk", null, null, null, null, "FLOATING"),
					"buy milk"
				}, 
				{ // test 14
					new Task(0, "buy milk", LocalDate.of(2015,04,18), null, null, null, "TIMED"),
				 	"buy milk on 2015-04-18" }, 
				{ // test 15
					new Task(0, "buy milk", LocalDate.of(2015,04,17), null, null, null, "TIMED"),
				 	"buy milk on friday" },

		});
	}

	@Test
	public void testView() {
		final TaskListSet set = new TaskListSet();
		final ResultSet rs = addTask.execute(command, set);
		rs.getReturnList().get(0).setTaskID(0);
		assertTrue(compareTask(rs.getReturnList().get(0), expected));
	}
}
