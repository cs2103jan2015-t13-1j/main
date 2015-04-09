package organizer.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import organizer.logic.ResultSet;
import organizer.parser.CommandParser;

public class IntegrationTesting {
	CommandParser commandParser;

	private static String readStream(InputStream in) {
		try(Scanner sc = new Scanner(in)) {
			final String result = sc.useDelimiter("\\A").next();
			sc.close();
			return result;
		}
	}
	
	@Before
	public void initialise() {
		commandParser = new CommandParser();
	}
	
	@Test
	public void testUserCommandExecutedProperly() throws IOException {
		commandParser.executeCommand("clear");
		try (Scanner sc = new Scanner(getClass().getResource("resources/commands.txt").openStream())) {
			while (sc.hasNext()) {
				commandParser.executeCommand(sc.nextLine());
				// compare resultList with another list
			}
			// compare storage
			final String expected = readStream(new FileInputStream("expected.txt"));
			final String result = commandParser;
		}
	}

}
