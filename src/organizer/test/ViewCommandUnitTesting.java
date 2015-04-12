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
		return Arrays.asList(new Object[][]{
				{	// test 1
					new ArrayList<>(Arrays.asList(new Task[]{})),
					new ArrayList<>(Arrays.asList(new Task[]{})),
					"all"
				},
				{	// test 2
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(0, "buy milk", LocalDate.now(), LocalTime.now(), LocalDate.now(), LocalTime.now(), "TIMED")
					})),
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(0, "buy milk", LocalDate.now(), LocalTime.now(), LocalDate.now(), LocalTime.now(), "TIMED")
					})),
					"today"
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
