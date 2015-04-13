package organizer.test;

import organizer.logic.Task;

//@author A0113627L
public class TestUtil {
	public static boolean compareObject(Object a, Object b) {
		return a == b || a != null && b != null && a.equals(b);
	}
	public static boolean compareTask(Task a, Task b) {
		return a == b ||
				a != null && b != null &&
					compareObject(a.getTaskName(), b.getTaskName()) &&
					compareObject(a.getTaskID(), b.getTaskID()) &&
					compareObject(a.getTaskPriority(), b.getTaskPriority()) &&
					compareObject(a.getTaskStatus(), b.getTaskStatus()) &&
					compareObject(a.getTaskType(), b.getTaskType()) &&
					compareObject(a.getTaskStartDate(), b.getTaskStartDate()) &&
					compareObject(a.getTaskStartTime(), b.getTaskStartTime()) &&
					compareObject(a.getTaskEndDate(), b.getTaskEndDate());
	}
}
