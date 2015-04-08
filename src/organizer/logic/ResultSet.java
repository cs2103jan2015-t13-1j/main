//@author A0098824N
package organizer.logic;

import java.util.ArrayList;

public class ResultSet {
	String opStatus;
	Boolean isSuccessful;
	ArrayList<Task> returnList;
	
	public ResultSet(String opStatus, ArrayList<Task> returnList, Boolean isSuccessful) {
		this.opStatus = opStatus;
		this.returnList = returnList;
		this.isSuccessful = isSuccessful;
	}

	public ResultSet() {
		opStatus = null;
		returnList = null;
		isSuccessful = null;
	}

	public String getOpStatus() {
		return opStatus;
	}

	public ArrayList<Task> getReturnList() {
		return returnList;
	}
	
	public boolean getIsSuccessful() {
		return isSuccessful;
	}
	
	public void setOpStatus(String opStatus) {
		this.opStatus = opStatus;
	}
	
	public void setReturnList(ArrayList<Task> returnList) {
		this.returnList = returnList;
	}
	
	public void setIsSuccessful(Boolean isSuccessful) {
		this.isSuccessful = isSuccessful;
	}
}
