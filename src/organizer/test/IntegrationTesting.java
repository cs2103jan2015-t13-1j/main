package organizer.test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import organizer.logic.ResultSet;
import organizer.parser.CommandParser;

public class IntegrationTesting {
	CommandParser commandParser;
	private static final Pattern MATCH_TO_END = Pattern.compile("\\Z");

	private static String readStream(InputStream in) {
		try(Scanner sc = new Scanner(in)) {
			final String result = sc.useDelimiter(MATCH_TO_END).next();
			sc.close();
			return result;
		}catch(NoSuchElementException e){
			return "";
		}
	}
	
	@Before
	public void initialise() {
		commandParser = new CommandParser();
	}
	
	@Test
	public void testUserCommandExecutedProperly() throws IOException {
		commandParser.executeCommand("clear");
		try (Scanner sc = new Scanner(getClass().getResourceAsStream("resources/commands.txt"))) {
			while (sc.hasNext()) {
				commandParser.executeCommand(sc.nextLine());
				// compare resultList with another list
			}
			// compare storage
			final Pattern expected = Pattern.compile(readStream(getClass().getResourceAsStream("resources/expected.txt")));
			final PipedInputStream in = new PipedInputStream();
			final PipedOutputStream out = new PipedOutputStream(in);
			commandParser.writeStorageToStream(out);
			final String result = readStream(in);
			assertTrue(expected.matcher(result).find());
		}
	}

}
