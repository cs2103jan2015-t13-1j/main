package organizer.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	CommandParserTest.class,
	CompleteCommandUnitTesting.class,
	IncompleteCommandUnitTesting.class,
	DeleteCommandUnitTesting.class,
	EditCommandUnitTesting.class,
	PostponeCommandUnitTesting.class,
	RankCommandUnitTesting.class,
	SearchCommandUnitTesting.class,
	StorageTesting.class,
	UndoCommandUnitTesting.class,
	ViewCommandUnitTesting.class
})
public class AllTests {

}
