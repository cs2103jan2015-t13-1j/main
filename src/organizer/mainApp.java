package organizer;

import organizer.logic.*;
import organizer.storage.*;

import java.lang.reflect.Array;
import java.util.*;

public class mainApp {
	
	public static final String MESSAGE_EMPTY = "You have no tasks.";
	static Logic logic = new Logic();

	public static void main(String[] args) {
		displayTasks();
		while(true) {
			System.out.println("Please enter a command: ");
			ArrayList<String> parsedInput = parseInput();
			String command = parsedInput.get(0);
			String inputArgs = getInputArgsString(parsedInput);
			displayTasks(logic.executeComand());
		}
	}
	
	/*
	 * parses user input returning as an array command will always be at 0 index
	 */
	private static String getInputArgsString(ArrayList<String> inputArray) {
		if (inputArray.get(1) != null) {
			String inputArgsString = inputArray.get(1);
			for (int i = 2; i < inputArray.size(); i++) {
				inputArgsString = inputArgsString + " " + inputArray.get(i);
			}
			return inputArgsString;
		} else {
			return null;
		}
	}
	
	public static void displayTasks() {
		ArrayList<Task> tasks = logic.loadStorage();
		if (tasks.get(0) == null) {
			printMessage(MESSAGE_EMPTY, null, null);
		} else {
			for (int i = 0; i < tasks.size(); i++) {
				System.out.print((i+1) + ". " + tasks.get(i).getTaskName());
				System.out.print(" " + tasks.get(i).getTaskStatus());
				System.out.println(" " + tasks.get(i).getDueDate());
			}
		}
	}
	
	public static ArrayList<String> parseInput() {
		Scanner lines = new Scanner(System.in);
		Scanner tokens = new Scanner(lines.nextLine());
		List<String> parsedInputArray = new ArrayList<String>();
		
		while (tokens.hasNext()) {
			parsedInputArray.add(tokens.next());
		}
		return (ArrayList<String>) parsedInputArray;
	}
	
	/*
	 * printMessage will customize output strings
	 * defined in final class variables
	 */
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
