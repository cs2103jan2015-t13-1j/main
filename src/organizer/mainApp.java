package organizer;

import organizer.logic.*;

import java.io.IOException;
import java.util.*;

public class mainApp {

	public static final String COMMAND_PROMPT = "Please enter a command: ";
	public static final String MESSAGE_EMPTY = "You have no tasks.";
	public static final String TASK_TEMPLATE = "%d. %s %s %s";

	public static void main(String[] args) throws IOException {
		Logic logic = new Logic();
		ArrayList<Task> tasksArray = logic.loadStorage();
		displayTasks(tasksArray);
<<<<<<< HEAD
=======

>>>>>>> origin/master
		
		try (Scanner sc = new Scanner(System.in)) {
			while (true) {
				printMessage(COMMAND_PROMPT);
				String userInput = sc.nextLine().toString();
				displayTasks(logic.executeCommand(userInput));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void displayTasks(ArrayList<Task> tasks) {
		if (tasks.size() == 0) {
			printMessage(MESSAGE_EMPTY);
		} else {
			for (int i = 0; i < tasks.size(); i++) {
				final Task task = tasks.get(i);
				printMessage(TASK_TEMPLATE,
						i + 1,
						task.getTaskName(),
						task.getTaskStatus(),
						task.getDueDate().toString());
				printMessage();
			}
		}
	}

	private static void printMessage(String message, Object... args) {
		System.out.printf(message, args);
	}
	
	private static void printMessage(String message) {
		System.out.println(message);
	}
	
	private static void printMessage() {
		System.out.println();
	}

}
