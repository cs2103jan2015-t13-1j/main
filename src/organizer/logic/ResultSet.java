package organizer.logic;

import java.util.ArrayList;

//@author A0098824N
public class ResultSet {
	String opStatus;
	ArrayList<Task> returnList;
	
	public ResultSet(String opStatus, ArrayList<Task> returnList) {
		this.opStatus = opStatus;
		this.returnList = returnList;
	}

	public ResultSet() {
		opStatus = null;
		returnList = null;
	}

	public String getOpStatus() {
		return opStatus;
	}

	public ArrayList<Task> getReturnList() {
		return returnList;
	}
	
	public void setOpStatus(String opStatus) {
		this.opStatus = opStatus;
	}
	
	public void setReturnList(ArrayList<Task> returnList) {
		this.returnList = returnList;
	}

}
