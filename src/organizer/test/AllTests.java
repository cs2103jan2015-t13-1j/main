package organizer.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//@author A0113627L
@RunWith(Suite.class)
@Suite.SuiteClasses({
	CompleteCommandUnitTesting.class,
	IncompleteCommandUnitTesting.class,
	DeleteCommandUnitTesting.class,
	EditCommandUnitTesting.class,
	PostponeCommandUnitTesting.class,
	RankCommandUnitTesting.class,
	SearchCommandUnitTesting.class,
	StorageTesting.class,
	UndoCommandUnitTesting.class,
	ViewCommandUnitTesting.class,
	DeadlineCommandUnitTesting.class,
	DateAndTimeUnitTesting.class,
	RankCommandUnitTesting.class,
	FloatCommandUnitTesting.class,
	AddCommandUnitTesting.class
})
public class AllTests {

}
