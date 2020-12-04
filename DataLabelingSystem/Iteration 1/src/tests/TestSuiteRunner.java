package tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;



public class TestSuiteRunner {

    public boolean runTests() {
        Result result = JUnitCore.runClasses(TestSuite.class);
        for (Failure failure : result.getFailures()) {
            //failure.getException().printStackTrace();
        }
        System.out.println("Tests Passed: " + result.wasSuccessful());
        return result.wasSuccessful();
    }
}

