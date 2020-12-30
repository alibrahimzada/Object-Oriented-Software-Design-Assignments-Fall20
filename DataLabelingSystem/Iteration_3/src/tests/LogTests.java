package tests;

import main.Log;
import org.junit.Test;
import org.junit.platform.commons.annotation.Testable;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.logging.Handler;

import org.junit.Assert;

@Testable
public class LogTests {
    
    private Log logger;
    
    public LogTests(){
        try {
            this.logger = new Log(); 
            for(Handler h:this.logger.getLogger().getHandlers()) {
                h.close();
            }

        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    
    @Test
    public void fileCreationTest(){
        File f = new File(this.logger.getFileName());
        assertTrue(f.exists() && !f.isDirectory()); // testing if the file was creatd upon the initialization of the object logger
    }
}
