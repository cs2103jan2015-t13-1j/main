package organizer.logic;

import java.util.ArrayList;

import organizer.storage.Storage;

public class Logic {
	Storage tempStorage = new Storage();
	ArrayList<Task> tempList = new ArrayList<Task>();
	Task tempTask = new Task();
	
	public ArrayList<Task> loadStorage() {
		tempList = tempStorage.readFile();
		return tempList;
	}
	
	public void writeStorage() {
		tempStorage.writeFile(tempList);
	}
	
	public void readCommand(String command) {
		
		
	}
	
	public void addTask() {
		
	}
	
	public void deleteTask() {
		
	}
	
	public void searchTask() {
		
	}
	
	public void viewList(){
		
	}
}
