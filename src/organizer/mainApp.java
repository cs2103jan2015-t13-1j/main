package organizer;

import organizer.logic.*;
import organizer.storage.*;

import java.lang.reflect.Array;
import java.util.*;

public class mainApp {

	public static final String MESSAGE_EMPTY = "You have no tasks.";

	public static void main(String[] args) {
		Logic logic = new Logic();
		ArrayList<Task> tasksArray = logic.loadStorage();
		displayTasks(tasksArray);
		while (true) {
			Scanner sc = new Scanner(System.in);
			System.out.println("Please enter a command: ");
			String userInput = sc.nextLine().toString();
			displayTasks(logic.executeCommand(userInput));
		}
	}

	private static void displayTasks(ArrayList<Task> tasks) {
		if (tasks.get(0) == null) {
			printMessage(MESSAGE_EMPTY, null, null);
		} else {
			for (int i = 0; i < tasks.size(); i++) {
				System.out.print((i + 1) + ". " + tasks.get(i).getTaskName());
				System.out.print(" " + tasks.get(i).getTaskStatus());
				System.out.println(" " + tasks.get(i).getDueDate());
			}
		}
	}

	private static void printMessage(String message, String unique1,
			String unique2) {
		if (unique1 != null && unique2 != null) {
			System.out.println(String.format(message, unique1, unique2));
		} else if (unique1 != null && unique2 == null) {
			System.out.println(String.format(message, unique1));
		} else {
			System.out.println(message);
		}
	}

}
