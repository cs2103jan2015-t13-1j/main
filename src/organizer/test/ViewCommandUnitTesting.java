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

//@author A0113627L
@RunWith(Parameterized.class)
public class ViewCommandUnitTesting {
	private final ArrayList<Task> expected;
	private final ArrayList<Task> storage;
	private final String viewParameter;
	private final ViewTask viewTask = new ViewTask();
	
	public ViewCommandUnitTesting (ArrayList<Task> expected, ArrayList<Task> storage, String viewParameter) {
		this.expected = expected;
		this.storage = storage;
		this.viewParameter = viewParameter;
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
					new ArrayList<>(Arrays.asList(new Task[]{})),
					new ArrayList<>(Arrays.asList(new Task[]{})),
					"all"
				},
				{	// test 2
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(0, "buy milk", startDate, startTime, endDate, endTime, "TIMED")
					})),
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(0, "buy milk", startDate, startTime, endDate, endTime, "TIMED"),
							new Task(1, "what", null, null, null, null, "FLOATING")
					})),
					"today"
				},
				{	// test 3
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(0, "buy milk", startDate, startTime, endDate, endTime, "TIMED", "LOW", "COMPLETE")
					})),
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(0, "buy milk", startDate, startTime, endDate, endTime, "TIMED", "LOW", "COMPLETE"),
							new Task(1, "buy milk", startDate, startTime, endDate, endTime, "TIMED", "MEDIUM", "INCOMPLETE")
					})),
					"complete"
				},
				{	// test 4
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(1, "buy milk", startDate, startTime, endDate, endTime, "TIMED", "MEDIUM", "INCOMPLETE")
					})),
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(0, "buy milk", startDate, startTime, endDate, endTime, "TIMED", "LOW", "COMPLETE"),
							new Task(1, "buy milk", startDate, startTime, endDate, endTime, "TIMED", "MEDIUM", "INCOMPLETE")
					})),
					"incomplete"
				},
				{	// test 5
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(1, "buy milk", startDate, startTime, endDate.plusDays(1), endTime, "TIMED", "MEDIUM", "INCOMPLETE")
					})),
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(0, "buy milk", startDate, startTime, endDate, endTime, "TIMED", "LOW", "COMPLETE"),
							new Task(1, "buy milk", startDate, startTime, endDate.plusDays(1), endTime, "TIMED", "MEDIUM", "INCOMPLETE")
					})),
					"tomorrow"
				},
				{	// test 6
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(2, "buy milk", startDate, startTime, endDate.plusDays(1), endTime, "TIMED", "HIGH")
					})),
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(0, "buy milk", startDate, startTime, endDate, endTime, "TIMED", "LOW", "COMPLETE"),
							new Task(1, "buy milk", startDate, startTime, endDate.plusDays(1), endTime, "TIMED", "MEDIUM", "INCOMPLETE"),
							new Task(2, "buy milk", startDate, startTime, endDate.plusDays(1), endTime, "TIMED", "HIGH")
					})),
					"high"
				},
				{
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(1, "buy milk", startDate, startTime, endDate.plusDays(1), endTime, "TIMED", "MEDIUM", "INCOMPLETE")
					})),
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(0, "buy milk", startDate, startTime, endDate, endTime, "TIMED", "LOW", "COMPLETE"),
							new Task(1, "buy milk", startDate, startTime, endDate.plusDays(1), endTime, "TIMED", "MEDIUM", "INCOMPLETE"),
							new Task(2, "buy milk", startDate, startTime, endDate.plusDays(1), endTime, "TIMED", "HIGH")
					})),
					"medium"
				},
				{
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(0, "buy milk", startDate, startTime, endDate, endTime, "TIMED", "LOW", "COMPLETE")
					})),
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(0, "buy milk", startDate, startTime, endDate, endTime, "TIMED", "LOW", "COMPLETE"),
							new Task(1, "buy milk", startDate, startTime, endDate.plusDays(1), endTime, "TIMED", "MEDIUM", "INCOMPLETE"),
							new Task(2, "buy milk", startDate, startTime, endDate.plusDays(1), endTime, "TIMED", "HIGH")
					})),
					"low"
				},
				{
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(0, "buy milk", null, null, endDate, endTime, "DEADLINE", "LOW", "COMPLETE"),
							new Task(1, "buy milk", null, null, endDate.plusDays(1), null, "DEADLINE", "MEDIUM", "INCOMPLETE")
					})),
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(0, "buy milk", null, null, endDate, endTime, "DEADLINE", "LOW", "COMPLETE"),
							new Task(1, "buy milk", null, null, endDate.plusDays(1), null, "DEADLINE", "MEDIUM", "INCOMPLETE"),
							new Task(2, "buy milk", startDate, startTime, endDate.plusDays(1), null, "TIMED", "HIGH")
					})),
					"deadline"
				},
				{
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(2, "buy milk", startDate, startTime, endDate.plusDays(1), null, "TIMED", "HIGH")
					})),
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(0, "buy milk", null, null, endDate, endTime, "DEADLINE", "LOW", "COMPLETE"),
							new Task(1, "buy milk", null, null, endDate.plusDays(1), null, "DEADLINE", "MEDIUM", "INCOMPLETE"),
							new Task(2, "buy milk", startDate, startTime, endDate.plusDays(1), null, "TIMED", "HIGH")
					})),
					"timed"
				},
				{
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(3, "buy milk", null, null, null, null, "FLOATING")
					})),
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(0, "buy milk", null, null, endDate, endTime, "DEADLINE", "LOW", "COMPLETE"),
							new Task(1, "buy milk", null, null, endDate.plusDays(1), null, "DEADLINE", "MEDIUM", "INCOMPLETE"),
							new Task(2, "buy milk", startDate, startTime, endDate.plusDays(1), null, "TIMED", "HIGH"),
							new Task(3, "buy milk", null, null, null, null, "FLOATING")
					})),
					"floating"
				},
				{
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(3, "buy milk", null, null, endDate.plusDays(-100), null, "OVERDUE")
					})),
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(0, "buy milk", null, null, endDate, endTime, "DEADLINE", "LOW", "COMPLETE"),
							new Task(1, "buy milk", null, null, endDate.plusDays(1), null, "DEADLINE", "MEDIUM", "INCOMPLETE"),
							new Task(2, "buy milk", startDate, startTime, endDate.plusDays(1), null, "TIMED", "HIGH"),
							new Task(3, "buy milk", null, null, endDate.plusDays(-100), null, "OVERDUE")
					})),
					"overdue"
				},
				{
					new ArrayList<>(Arrays.asList(new Task[]{})),
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(0, "buy milk", null, null, endDate, endTime, "DEADLINE", "LOW", "COMPLETE"),
							new Task(1, "buy milk", null, null, endDate.plusDays(1), null, "DEADLINE", "MEDIUM", "INCOMPLETE"),
							new Task(2, "buy milk", startDate, startTime, endDate.plusDays(1), null, "TIMED", "HIGH"),
							new Task(3, "buy milk", null, null, endDate.plusDays(-100), null, "OVERDUE")
					})),
					"abc"
				}
		});
	}
	
	@Test
	public void testView() {
		final TaskListSet set = new TaskListSet();
		set.setTaskList(storage);
		final ResultSet rs = viewTask.execute(viewParameter, set);
		Iterator<Task> result = rs.getReturnList().iterator(), expected = this.expected.iterator();
		while (result.hasNext() && expected.hasNext()) {
			assertTrue(compareTask(result.next(), expected.next()));
		}
		assertFalse(result.hasNext() || expected.hasNext());
	}
}
