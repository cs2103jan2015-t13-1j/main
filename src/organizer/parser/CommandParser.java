package organizer.parser;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import organizer.logic.*;

//@author A0113871J
public class CommandParser {
	private static final String MESSAGE_INVALID_COMMAND = "Unregconized command!";
	
	private static final String noContentCommandPattern = "clear|undo|save";
	private static final String withContentCommandPattern = "add|delete|search|view|edit|rank|postpone|incomplete|complete|save as|float|load";
	
	ArrayList<Task> taskList = new ArrayList<Task>();
	ArrayList<Task> resultList = new ArrayList<Task>(); // for search
	ArrayList<Task> viewList = new ArrayList<Task>();

	Logic logic = new Logic();

	public ArrayList<Task> loadStorage() throws IOException {
		taskList = logic.loadStorage();
		return taskList;
	}

	public void writeStorage() throws IOException {
		logic.writeStorage();
	}
	
	public void writeStorageToStream(OutputStream out) throws IOException {
		logic.writeStorage(out);
	}

	public static enum COMMAND_TYPE {
		ADD_TASK, DELETE_TASK, VIEW_TASK, SEARCH_TASK, COMPLETE_TASK, INCOMPLETE_TASK, CLEAR_TASK, EDIT_TASK, POSTPONE_TASK, RANK_TASK, DEADLINE_TASK,
		UNDO, SAVE, LOAD, FLOAT_TASK, SAVEAS, INVALID, EXIT
	};

	public static COMMAND_TYPE determineCommandType(String commandTypeString) {
		if (commandTypeString.equals(null)) {
			throw new Error(MESSAGE_INVALID_COMMAND);
		} else {
			commandTypeString = commandTypeString.toLowerCase();
		}

		switch (commandTypeString) {
		case "add":
			return COMMAND_TYPE.ADD_TASK;
		case "delete":
			return COMMAND_TYPE.DELETE_TASK;
		case "view":
			return COMMAND_TYPE.VIEW_TASK;
		case "search":
			return COMMAND_TYPE.SEARCH_TASK;
		case "clear":
			return COMMAND_TYPE.CLEAR_TASK;
		case "complete":
			return COMMAND_TYPE.COMPLETE_TASK;
		case "incomplete":
			return COMMAND_TYPE.INCOMPLETE_TASK;
		case "edit":
			return COMMAND_TYPE.EDIT_TASK;
		case "postpone":
			return COMMAND_TYPE.POSTPONE_TASK;
		case "rank":
			return COMMAND_TYPE.RANK_TASK;
		case "undo":
			return COMMAND_TYPE.UNDO;
		case "save":
			return COMMAND_TYPE.SAVE;
		case "float":
			return COMMAND_TYPE.FLOAT_TASK;
		case "deadline":
			return COMMAND_TYPE.DEADLINE_TASK;
		case "saveas":
			return COMMAND_TYPE.SAVEAS;
		case "load":
			return COMMAND_TYPE.LOAD;
		case "exit":
			return COMMAND_TYPE.EXIT;
		default:
			return COMMAND_TYPE.INVALID;
		}

	}

	public ResultSet executeCommand(String userCommand) throws IOException {
		// split the userCommand into operation and task info
		String userOperation;
		String userContent;

		if (userCommand.indexOf(' ') >= 0) {
			userOperation = userCommand.substring(0, userCommand.indexOf(' '));
			userContent = userCommand.substring(userCommand.indexOf(' ') + 1);
			if(userOperation.matches(noContentCommandPattern)) {
				return showNoChanges();
			}

		} else {
			userOperation = userCommand;
			if(userOperation.matches(withContentCommandPattern)) {
				return showNoChanges();
			} else {
				userContent = "";
			}
		}

		COMMAND_TYPE commandType = determineCommandType(userOperation);
		
		switch (commandType) {
		case ADD_TASK:
			return logic.addCommand(userContent);
		case DELETE_TASK:
			return logic.deleteCommand(userContent);
		case SEARCH_TASK:
			return logic.searchCommand(userContent);
		case VIEW_TASK:
			return logic.viewCommand(userContent);
		case CLEAR_TASK:
			return logic.clearCommand();
		case COMPLETE_TASK:
			return logic.completeCommand(userContent);
		case INCOMPLETE_TASK:
			return logic.incompleteCommand(userContent);
		case EDIT_TASK:
			return logic.editCommand(userContent);
		case POSTPONE_TASK:
			return logic.postponeCommand(userContent);
		case RANK_TASK:
			return logic.rankCommand(userContent);
		case FLOAT_TASK:
			return logic.floatCommand(userContent);
		case DEADLINE_TASK:
			return logic.deadlineCommand(userContent);
		case UNDO:
			return logic.undoCommand();
		case SAVE:
			return logic.saveCommand();
		case SAVEAS:
			return logic.saveAsCommand(userContent);
		case LOAD:
			return logic.loadFileCommand(userContent);
		case EXIT:
			logic.saveCommand();
			System.exit(0);
		default:
			// throw an error if the command is not recognized
			return showNoChanges();
		}

	}

	private ResultSet showNoChanges() {
		ResultSet returnResult = new ResultSet();
		returnResult.setOpStatus(MESSAGE_INVALID_COMMAND);
		returnResult.setReturnList(logic.viewCommand(Logic.MODE_VIEW).getReturnList());
		return logic.setViewMode(returnResult);
	}
}
