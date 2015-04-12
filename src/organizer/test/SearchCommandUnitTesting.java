package organizer.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;

import organizer.logic.ResultSet;
import organizer.logic.SearchTask;
import organizer.logic.Task;
import organizer.logic.TaskListSet;

@RunWith(Parameterized.class)
public class SearchCommandUnitTesting {
	
	@Parameterized.Parameters
	public static List<Object[]> getParameters() {
		return Arrays.asList(new Object[][] {
				{	// test 1
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(0, "abcc", null, null, null, null, "FLOATING")
					})),
					new ArrayList<>(Arrays.asList(new Task[]{
							new Task(0, "abcc", null, null, null, null, "FLOATING"),
							new Task(1, "deff", null, null, null, null, "FLOATING")
					})),
					"abc"
				}
		});
	}

	private final ArrayList<Task> expected;
	private final ArrayList<Task> input;
	private final String command;
	
	public SearchCommandUnitTesting(ArrayList<Task> expected, ArrayList<Task> input, String command) {
		this.expected = expected;
		this.input = input;
		this.command = command;
	}
	
	@Test
	public void test() {
		final SearchTask searchTask = new SearchTask();
		final TaskListSet set = new TaskListSet();
		set.setTaskList(input);
		final ResultSet rs = searchTask.execute(command, set);
		final Iterator<Task> expected = this.expected.iterator(), result = rs.getReturnList().iterator();
		while (expected.hasNext() && result.hasNext()) {
			assertTrue(TestUtil.compareTask(expected.next(), result.next()));
		}
		assertFalse(expected.hasNext() || result.hasNext());
	}
}
