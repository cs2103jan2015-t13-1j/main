package organizer.logic;

public class TaskUtil {
	
	public enum TaskType {
		DEADLINE,
		DURATION,
		FLOATING,
		SPANNING,
		RECURRING
	};
	
	public static TaskType getTaskType(Task task) {
		return TaskType.RECURRING;
	}
}
