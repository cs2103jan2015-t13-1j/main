package organizer.test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import organizer.logic.Task;
import organizer.parser.CommandParser;

@RunWith(Parameterized.class)
public class CommandParserTest {

	@Parameterized.Parameters
	public static List<Object[]> getParameters() {
		return Arrays.asList(new Object[][]{
				{
					CommandParser.COMMAND_TYPE.ADD_TASK,
					"add buy milk"
				},
				{
					CommandParser.COMMAND_TYPE.CLEAR_TASK,
					"clear  "
				},
				{
					CommandParser.COMMAND_TYPE.COMPLETE_TASK,
					"complete 2"
				},
				{
					CommandParser.COMMAND_TYPE.DELETE_TASK,
					"delete 1"
				},
				{
					CommandParser.COMMAND_TYPE.EDIT_TASK,
					"edit 1 from 2015-11-11"
				},
				{
					CommandParser.COMMAND_TYPE.EXIT,
					"exit"
				},
				{
					CommandParser.COMMAND_TYPE.FLOAT_TASK,
					"float 3"
				},
				{
					CommandParser.COMMAND_TYPE.INCOMPLETE_TASK,
					"incomplete 1"
				},
				{
					CommandParser.COMMAND_TYPE.INVALID,
					"asdfadsf"
				},
				{
					CommandParser.COMMAND_TYPE.LOAD,
					"load file"
				},
				{
					CommandParser.COMMAND_TYPE.POSTPONE_TASK,
					"postpone 1 by 20 days"
				},
				{
					CommandParser.COMMAND_TYPE.RANK_TASK,
					"rank 1 high"
				},
				{
					CommandParser.COMMAND_TYPE.SAVE,
					"save"
				},
				{
					CommandParser.COMMAND_TYPE.SAVEAS,
					"saveas file"
				},
				{
					CommandParser.COMMAND_TYPE.SEARCH_TASK,
					"search term"
				},
				{
					CommandParser.COMMAND_TYPE.UNDO,
					"undo"
				},
				{
					CommandParser.COMMAND_TYPE.VIEW_TASK,
					"view all"
				}
		});
	}
	
	public final CommandParser.COMMAND_TYPE expected;
	public final String command;
	
	public CommandParserTest(CommandParser.COMMAND_TYPE expected, String command) {
		this.expected = expected;
		this.command = command;
	}
	
	@Test
	public void test() {
		assertEquals(expected, CommandParser.determineCommandType(command));
	}
}
