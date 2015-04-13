package organizer.test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;
import organizer.logic.*;
import static organizer.test.TestUtil.*;

//@author A0113871J
@RunWith(Parameterized.class)
public class RankCommandUnitTesting {
	private final Task expected;
	private final Task input;
	private final Boolean isRemove;

	public RankCommandUnitTesting(Task expected, Task input, Boolean isRemove) {
		this.expected = expected;
		this.input = input;
		this.isRemove = isRemove;
	}

	@Parameterized.Parameters
	public static List<Object[]> getParameters() {
		return Arrays.asList(new Object[][] {
				{ // test 1
					new Task(0, "buy milk", null, null, LocalDate.now(), LocalTime.of(23, 59), "DEADLINE", "HIGH"),
					new Task(0, "buy milk", null, null, LocalDate.now(), LocalTime.of(23, 59), "DEADLINE", "LOW"),
					false
				},{ // test 2
					new Task(0, "buy milk", null, null, LocalDate.now(), LocalTime.of(23, 59), "DEADLINE", null),
					new Task(0, "buy milk", null, null, LocalDate.now(), LocalTime.of(23, 59), "DEADLINE", "LOW"),
					true
				},
				
		});
	}

	@Test
	public void testAdd() {
		final TaskListSet set = new TaskListSet();
		set.setTaskList(new ArrayList<>(Arrays.asList(input)));
		final ResultSet rs = new RankTask().execute(isRemove ? "1 remove" : "1 high", set, new Validation());
		assertTrue(compareTask(rs.getReturnList().get(0), expected));
	}
}
