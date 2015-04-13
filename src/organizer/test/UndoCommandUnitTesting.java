package organizer.test;

import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;
import organizer.logic.*;
import organizer.parser.CommandParser;

//@author A0098824N
public class UndoCommandUnitTesting {

	private final UndoCommand undoTask = new UndoCommand();
	private final CommandParser cp = new CommandParser();

	@Test
	public void testUndo() throws IOException {
		final TaskListSet set = new TaskListSet();
		ResultSet rs = cp.executeCommand("add buy milk by today");
		int listSize = rs.getReturnList().size();
		assertTrue(listSize == 1);
		rs = undoTask.execute(set, Logic.undoList, false, false);
		listSize = rs.getReturnList().size();
		assertTrue(listSize == 0);
		
	}
}
