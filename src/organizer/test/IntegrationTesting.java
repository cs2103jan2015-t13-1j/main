package organizer.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.function.Consumer;

import org.junit.Test;

import organizer.logic.ResultSet;
import organizer.parser.CommandParser;
import organizer.storage.Storage;

//@author A0113627L
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
	
	private InputStream getResourceInputStream(String resourceName) {
		return getClass().getResourceAsStream(resourceName);
	}
	
	private Scanner getResourceScanner(String resourceName) {
		return new Scanner(getResourceInputStream(resourceName));
	}
	
	@Test
	public void testUserCommandExecutedProperly() throws IOException {
		commandParser.executeCommand("clear");
		final Queue<Pattern> queueExpected = new LinkedList<>();
		try (Scanner sc = getResourceScanner("resources/compare_view.txt")) {
			sc.useDelimiter(MATCH_TO_EOL);
			while(sc.hasNext()) {
				queueExpected.add(Pattern.compile(sc.next().trim()));
			}
		}
		try (Scanner sc = getResourceScanner("resources/commands.txt")) {
			while (sc.hasNext()) {
				final ResultSet rs = commandParser.executeCommand(sc.nextLine());
				if (rs.getCommandType() == CommandParser.COMMAND_TYPE.VIEW_TASK)
					compareResult(queueExpected.poll(), out -> {
						try {
							storage.writeFileToStream(rs.getReturnList(), out);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					});
			}
			// compare storage
			compareResult(Pattern.compile(readStream(getResourceInputStream("resources/expected.txt"))),
					out -> {
						try {
							commandParser.writeStorageToStream(out);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					});
		}
	}

	private void compareResult(Pattern expected, Consumer<OutputStream> operator) throws IOException {
		// compare resultList with another list
		final PipedInputStream in = new PipedInputStream();
		final PipedOutputStream out = new PipedOutputStream(in);
		operator.accept(out);
		final String result = readStream(in);
		assertTrue(expected.matcher(result).find());
		in.close();
	}

}
