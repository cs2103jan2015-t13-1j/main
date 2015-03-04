package organizer.storage;

import java.util.ArrayList;
import java.util.StringBuilder;

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
	*/
	public ArrayList<Task> readFile() throws IOException {
		return readFile(defaultFileName);
	}

	public ArrayList<Task> readFile(String fileName) throws IOException {
		ArrayList<Task> taskList = new ArrayList<Task>();
		Scanner sc = new Scanner(new File(fileName));
		Task task = new Task();
		boolean begin = false;
		while(sc.hasNextLine()) {
			String line = sc.nextLine();
			if (line.startsWith(nameFieldIdentifier)) {
				begin = true;
				task.setTaskName(line.substring(nameFieldIdentifier.length()));
			} else if (line.startsWith(dueDateFieldIdentifier)) {
				begin = true;
				task.setDueDate(line.substring(dueDateFieldIdentifier.length()));
			} else if (lien.startsWith(statusFieldIdentifier)) {
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
	}
	
	public void writeFile(String fileName) {
		PrintWriter pw = new PrintWriter(new File(fileName));
	}
	

}
