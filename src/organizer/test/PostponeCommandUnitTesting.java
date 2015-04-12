package organizer.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import organizer.logic.PostponeTask;
import organizer.logic.ResultSet;
import organizer.logic.Task;
import organizer.logic.TaskListSet;
import organizer.logic.Validation;

//@author A0113871J
public class PostponeCommandUnitTesting {
	private final Validation validOp = new Validation();
	
	@Test
	public void shouldPostponeByDaysKeyedIn () throws IOException {
		final LocalDate date = LocalDate.now();
		final PostponeTask postponeTask = new PostponeTask();
		final TaskListSet set = new TaskListSet();
		set.setTaskList(new ArrayList<>(Arrays.asList(new Task(0, "apply gym membership", null, null, date, null, "DEADLINE"))));
		final ResultSet resultSet = postponeTask.execute("1 by 2 days", set, validOp);
		final Task resultTask = resultSet.getReturnList().get(0);
		assertTrue(TestUtil.compareTask(resultTask,
				new Task(0, "apply gym membership", null, null, date.plusDays(2), null, "DEADLINE")));
	}
	
	
	@Test
	public void shouldPostponeByHoursKeyedIn () throws IOException {
		final PostponeTask postponeTask = new PostponeTask();
		final TaskListSet set = new TaskListSet();
		set.setTaskList(new ArrayList<>(Arrays.asList(
				new Task(0, "apply gym membership", null, null, LocalDate.of(2015, 3, 31), LocalTime.of(20, 0), "DEADLINE"))));
		final ResultSet resultSet = postponeTask.execute("1 by 4 hours", set, validOp);
		final Task resultTask = resultSet.getReturnList().get(0);
		assertTrue(TestUtil.compareTask(resultTask,
				new Task(0, "apply gym membership", null, null, LocalDate.of(2015, 4, 1), LocalTime.of(0, 0), "DEADLINE")));
	}
}
