package organizer.test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import organizer.logic.ResultSet;
import organizer.parser.CommandParser;
import organizer.storage.Storage;

public class IntegrationTesting {
	CommandParser commandParser = new CommandParser();
	final Storage storage = new Storage();
	private static final Pattern MATCH_TO_END = Pattern.compile("\\Z");
	private static final Pattern MATCH_TO_EOL = Pattern.compile("<<<EOL");

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
	}
	
	@Test
	public void testUserCommandExecutedProperly() throws IOException {
		commandParser.executeCommand("clear");
		final Queue<Integer> queueLine = new LinkedList<>();
		final Queue<Pattern> queueExpected = new LinkedList<>();
		try (Scanner sc = new Scanner(getClass().getResourceAsStream("resources/compare_view.txt"))) {
			while(sc.hasNextInt()) {
				queueLine.add(sc.nextInt());
				queueExpected.add(Pattern.compile(sc.useDelimiter(MATCH_TO_EOL).next().trim()));
				sc.reset().skip(MATCH_TO_EOL);
			}
		}
		try (Scanner sc = new Scanner(getClass().getResourceAsStream("resources/commands.txt"))) {
			for (int line = 0; sc.hasNext(); ++line) {
				final ResultSet rs = commandParser.executeCommand(sc.nextLine());
				// compare resultList with another list
				if (!queueLine.isEmpty() && line == queueLine.peek()) {
					queueLine.poll();
					final Pattern expected = queueExpected.poll();
					final PipedInputStream in = new PipedInputStream();
					final PipedOutputStream out = new PipedOutputStream(in);
					storage.writeFileToStream(rs.getReturnList(), out);
					final String result = readStream(in);
					assertTrue(expected.matcher(result).find());
					in.close();
				}
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
