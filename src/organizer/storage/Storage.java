package organizer.storage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import organizer.logic.Task;

/**
* File Format
* id: ...
* name: ...
* date: ...
* description: ...
* ----
*/

public class Storage {

	public static final String defaultFileName = "storage.txt";

	private static final String nameFieldIdentifier = "name: ";
	private static final String startDateFieldIdentifier = "start date: ";
	private static final String startTimeFieldIdentifier = "start time: ";
	private static final String endDateFieldIdentifier = "end date: ";
	private static final String endTimeFieldIdentifier = "end time: ";
	private static final String statusFieldIdentifier = "status: ";
	private static final String typeFieldIdentifier = "type: ";
	private static final String taskIdFieldIdentifier = "id: ";
	private static final String priorityFieldIdentifier = "priority: ";

	private static final String endIdentifier = "----";
	
	/**
	 * readFile
	 * @return ArrayList<Task> a list of tasks
	 * @throws IOException
	 */
	public ArrayList<Task> readFile() throws IOException {
		return readFile(defaultFileName);
	}
	
	public ArrayList<Task> readFromStream(InputStream in) throws IOException {
		ArrayList<Task> taskList = new ArrayList<Task>();
		try (Scanner sc = new Scanner(in)) {
			Task task = new Task();
			boolean begin = false;
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				if (line.startsWith(nameFieldIdentifier)) {
					begin = true;
					task.setTaskName(line.substring(nameFieldIdentifier.length()));
				} else if (line.startsWith(startDateFieldIdentifier)) {
					begin = true;
					task.setTaskStartDate(LocalDate.parse(line.substring(startDateFieldIdentifier.length())));
				} else if(line.startsWith(startTimeFieldIdentifier)) {
					begin = true;
					task.setTaskStartTime(LocalTime.parse(line.substring(startTimeFieldIdentifier.length())));
				} else if(line.startsWith(endDateFieldIdentifier)) {
					begin = true;
					task.setTaskEndDate(LocalDate.parse(line.substring(endDateFieldIdentifier.length())));
				} else if(line.startsWith(endTimeFieldIdentifier)) {
					begin = true;
					task.setTaskEndTime(LocalTime.parse(line.substring(endTimeFieldIdentifier.length())));
				}else if (line.startsWith(statusFieldIdentifier)) {
					begin = true;
					task.setTaskStatus(line.substring(statusFieldIdentifier.length()));
				} else if(line.startsWith(typeFieldIdentifier)) {
					begin = true;
					task.setTaskType(line.substring(typeFieldIdentifier.length()));
				} else if (line.equals(endIdentifier)) {
					if (begin) {
						taskList.add(task);
						task = new Task();
						begin = false;
					}
				} else if (line.startsWith(taskIdFieldIdentifier)) {
					begin = true;
					task.setTaskID(Integer.parseInt(line.substring(taskIdFieldIdentifier.length())));
				} else if (line.startsWith(priorityFieldIdentifier)) {
					begin = true;
					task.setTaskPriority(line.substring(priorityFieldIdentifier.length()));
				}
			}
			// clean up
			if (begin) {
				taskList.add(task);
			}
			return taskList;
		}
	}

	public ArrayList<Task> readFile(String fileName) throws IOException {
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		return readFromStream(new FileInputStream(file));
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
		writeFileToStream(taskList, new FileOutputStream(fileName));
	}

	public void writeFileToStream(List<Task> taskList, OutputStream out) throws IOException {
		try (PrintWriter pw = new PrintWriter(out)) {
			Iterator<Task> taskIterator = taskList.iterator();
			while (taskIterator.hasNext()) {
				// name
				final Task task = taskIterator.next();
				pw.print(taskIdFieldIdentifier);
				pw.println(task.getTaskID());
				pw.print(nameFieldIdentifier);
				pw.println(task.getTaskName());
				if (task.getTaskStartDate() != null) {
					pw.print(startDateFieldIdentifier);
					pw.println(task.getTaskStartDate().toString());
				}
				if (task.getTaskStartTime() != null) {
					pw.print(startTimeFieldIdentifier);
					pw.println(task.getTaskStartTime());
				}
				
				if (task.getTaskEndDate() != null) {
					pw.print(endDateFieldIdentifier);
					pw.println(task.getTaskEndDate().toString());
				}
				if (task.getTaskEndTime() != null) {
					pw.print(endTimeFieldIdentifier);
					pw.println(task.getTaskEndTime());
				}
				pw.print(statusFieldIdentifier);
				pw.println(task.getTaskStatus());
				pw.print(typeFieldIdentifier);
				pw.println(task.getTaskType());
				
				if (task.getTaskPriority() != null) {
					pw.print(priorityFieldIdentifier);
					pw.println(task.getTaskPriority());
				}
				
				pw.println(endIdentifier);
			}
		}
		
	}
	

}