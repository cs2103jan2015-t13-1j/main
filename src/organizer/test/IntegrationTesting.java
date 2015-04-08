package organizer.test;

//import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
//import java.util.List;
import java.util.Scanner;

import org.junit.Test;

import organizer.mainApp;
import organizer.logic.Logic;
import organizer.logic.ResultSet;
//import organizer.logic.Task;
import organizer.parser.CommandParser;

public class IntegrationTesting {

	
	@SuppressWarnings({ "unused", "resource" })
	private static String readFile(InputStream in) {
		final Scanner sc = new Scanner(in).useDelimiter("\\A");
		final String result = sc.next();
		sc.close();
		return result;
	}
	
	@SuppressWarnings("unused")
	@Test
	// TO TEST: 
	public void testUserCommandExecutedProperly() {
		mainApp main = new mainApp();
		final Logic logic = new Logic();
		CommandParser commandParser = new CommandParser();
		try (Scanner sc = new Scanner(getClass().getResource("commands.txt").openStream())) {
			while (sc.hasNext()) {
				final ResultSet resultList = commandParser.executeCommand(sc.nextLine());
				// compare resultList with another list
			}
			// compare storage
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
