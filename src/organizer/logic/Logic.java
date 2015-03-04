package organizer.logic;

import java.util.ArrayList;

import organizer.storage.Storage;

public class Logic {
	Storage tempStorage = new Storage();
	ArrayList<Task> tempList = new ArrayList<Task>();
	
	public void loadStorage() {
		tempList = tempStorage.readFile();
	}
	
	public void writeStorage() {
		tempStorage.writeFile(tempList);
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
