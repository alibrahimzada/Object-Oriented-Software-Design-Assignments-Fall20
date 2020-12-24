package tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class TestSuiteRunner {
    public boolean runTests() {
        Result result = JUnitCore.runClasses(TestSuite.class);
        System.out.println("Tests Passed: " + result.wasSuccessful());
        return result.wasSuccessful();
    }
}

