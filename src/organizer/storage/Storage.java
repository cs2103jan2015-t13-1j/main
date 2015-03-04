package organizer.storage;

import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import organizer.logic.Task;


/**
* File Format
* name
* date
* description
* ----
*/

public class Storage {

	public static final String defaultFileName = "storage.txt";

	private static final String nameFieldIdentifier = "name: ";
	private static final String dueDateFieldIdentifier = "duedate: ";
	private static final String descriptionFieldIdentifier = "description: ";
	private static final String statusFieldIdentifier = "status: ";

	private static final String endIdentifier = "----";
	
	/**
	 * readFile
	 * @return ArrayList<Task> a list of tasks
	 * @throws IOException
	 */
	public ArrayList<Task> readFile() throws IOException {
		return readFile(defaultFileName);
	}

	public ArrayList<Task> readFile(String fileName) throws IOException {
		ArrayList<Task> taskList = new ArrayList<Task>();
		try (Scanner sc = new Scanner(new File(fileName))) {
			Task task = new Task();
			boolean begin = false;
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				if (line.startsWith(nameFieldIdentifier)) {
					begin = true;
					task.setTaskName(line.substring(nameFieldIdentifier.length()));
				} else if (line.startsWith(dueDateFieldIdentifier)) {
					begin = true;
					task.setDueDate(LocalDate.parse(line.substring(dueDateFieldIdentifier.length())));
				} else if (line.startsWith(statusFieldIdentifier)) {
					begin = true;
					task.setTaskStatus(line.substring(dueDateFieldIdentifier.length()));
				} else if (line.startsWith(descriptionFieldIdentifier)) {
					// multiple lines
					begin = true;
					StringBuilder strb = new StringBuilder();
					while(!line.equals(endIdentifier)) {
						if (line.startsWith(endIdentifier)) {
							line = line.substring(endIdentifier.length());
						}
						strb.append(line).append('\n');
						if (sc.hasNextLine()) {
							line = sc.nextLine();
						} else {
							break;
						}
					}
				}
	
				if (line.equals(endIdentifier)) {
					taskList.add(task);
					task = new Task();
					begin = false;
				}
			}
			// clean up
			if (begin) {
				taskList.add(task);
			}
			return taskList;
		} catch (IOException e) {
			throw e;
		}
	}
	
	public void writeFile(List<Task> taskList) throws IOException {
		writeFile(taskList, defaultFileName);
	}
	
	/**
	 * writeFile writes a list to the file
	 * @param taskList
	 * @param fileName
	 * @throws IOException
	 */
	public void writeFile(List<Task> taskList, String fileName) throws IOException {
		try (PrintWriter pw = new PrintWriter(new File(fileName))) {
			Iterator<Task> taskIterator = taskList.iterator();
			while (taskIterator.hasNext()) {
				// name
				final Task task = taskIterator.next();
				pw.print(nameFieldIdentifier);
				pw.println(task.getTaskName());
				pw.print(dueDateFieldIdentifier);
				pw.println(task.getDueDate().toString());
				pw.print(statusFieldIdentifier);
				pw.println(task.getTaskStatus());
				pw.println(endIdentifier);
			}
		} catch (IOException e) {
			throw e;
		}
	}
	

}
