package organizer.test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;
import organizer.logic.*;
import static organizer.test.TestUtil.*;
/*
 * @author A0113871J
 */
@RunWith(Parameterized.class)
public class DeleteCommandUnitTesting {
	private final ArrayList<Task> expected;
	private final ArrayList<Task> storage;
	private final DeleteTask deleteTask = new DeleteTask();
	private final String deleteParameter;
	
	public DeleteCommandUnitTesting (ArrayList<Task> expected, ArrayList<Task> storage, String deleteParameter) {
		this.expected = expected;
		this.storage = storage;
		this.deleteParameter = deleteParameter;
	}
	
	@Before
	public void init() {
		
	}
	
	@Parameterized.Parameters
	public static List<Object[]> getParameters() {
		final LocalDate startDate = LocalDate.now(), endDate = LocalDate.now();
		final LocalTime startTime = LocalTime.now(), endTime = LocalTime.now();
		return Arrays.asList(new Object[][]{
				{	// test 1
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(0, "buy milk", startDate, startTime, endDate, endTime, "TIMED")
					})),
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(0, "buy milk", startDate, startTime, endDate, endTime, "TIMED"),
							new Task(1, "what", null, null, null, null, "FLOATING")
					})),
					"2"
				},
				{	// test 2
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(0, "buy milk", startDate, startTime, endDate, endTime, "TIMED"),
							new Task(1, "what", null, null, null, null, "FLOATING")
										})),
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(0, "buy milk", startDate, startTime, endDate, endTime, "TIMED"),
							new Task(1, "what", null, null, null, null, "FLOATING"),
							new Task(2, "what is up", startDate, startTime, endDate, endTime, "TIMED")
					})),
					"3"
				},
				
		});
	}
	
	@Test
	public void testView() {
		final TaskListSet set = new TaskListSet();
		set.setTaskList(storage);
		final ResultSet rs = deleteTask.execute(deleteParameter, set, new Validation());
		Iterator<Task> result = rs.getReturnList().iterator(), expected = this.expected.iterator();
		while (result.hasNext() && expected.hasNext()) {
			assertTrue(compareTask(result.next(), expected.next()));
		}
		assertFalse(result.hasNext() || expected.hasNext());
	}
}
