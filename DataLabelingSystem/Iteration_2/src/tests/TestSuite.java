package tests;

import org.junit.platform.commons.annotation.Testable;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
    LogTests.class,
    UserTests.class,
    DatasetTests.class,
    RandomLabelingMechanismTests.class,
    InstanceTests.class,
})


@Testable
public class TestSuite {

}  	