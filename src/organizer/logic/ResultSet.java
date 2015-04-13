package organizer.logic;

import java.util.ArrayList;

import organizer.parser.CommandParser;

//@author A0113871J
public class ResultSet {
	String opStatus;
	ArrayList<Task> returnList;
	CommandParser.COMMAND_TYPE commandType;
	
	public ResultSet(String opStatus, ArrayList<Task> returnList, CommandParser.COMMAND_TYPE commandType) {
		this.opStatus = opStatus;
		this.returnList = returnList;
		this.commandType = commandType;
	}

	public ResultSet() {
		opStatus = null;
		returnList = null;
		commandType = null;
		
	}
	
	public CommandParser.COMMAND_TYPE getCommandType() {
		return commandType;
	}
	
	public void setCommandType(CommandParser.COMMAND_TYPE commandType) {
		this.commandType = commandType;
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
