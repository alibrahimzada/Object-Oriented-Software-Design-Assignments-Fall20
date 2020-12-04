package tests;

import main.User;
import org.junit.platform.commons.annotation.Testable;
import org.junit.Test;
import org.junit.Assert;

@Testable
public class UserTests {
    @Test
    public void DatasetCreationTest(){
        try {
            new User(1, "name", "type");
          } catch (Exception e) {
            Assert.fail(e.getMessage());
          }


    }
    
}
