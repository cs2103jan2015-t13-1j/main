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
public class CompleteCommandUnitTesting {
	private final Task expected;
	private final Task input;

	public CompleteCommandUnitTesting(Task expected, Task input) {
		this.expected = expected;
		this.input = input;
	}

	@Parameterized.Parameters
	public static List<Object[]> getParameters() {
		return Arrays.asList(new Object[][] {
				{ // test 1
					new Task(0, "buy milk", null, null, LocalDate.now(), LocalTime.of(23, 59), "DEADLINE", "HIGH","COMPLETE"),
					new Task(0, "buy milk", null, null, LocalDate.now(), LocalTime.of(23, 59), "DEADLINE", "HIGH","INCOMPLETE")
				},
				
		});
	}

	@Test
	public void testComplete() {
		final TaskListSet set = new TaskListSet();
		set.setTaskList(new ArrayList<>(Arrays.asList(input)));
		final ResultSet rs = new CompleteTask().execute("1", set, new Validation());
		assertTrue(compareTask(rs.getReturnList().get(0), expected));
	}
}
