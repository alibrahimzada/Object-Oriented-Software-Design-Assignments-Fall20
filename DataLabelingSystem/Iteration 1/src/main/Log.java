package main;
import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
    private Logger logger;
    private FileHandler fileHandler;
    private String filename = "DataLabelingSystem.log";
    private String loggerName = "DataLabelingSystemLogger";

    public Log() throws SecurityException, IOException {
        File file = new File(filename);

        if (!file.exists()) {
            file.createNewFile(); // creating a file with the above name if it does not exist.
        }

        fileHandler = new FileHandler(filename, true); // append=true, to avoid overwriting. 
        logger = Logger.getLogger(loggerName);// creating a logger with name: DataLabelingSystemLogger.
        logger.addHandler(fileHandler);
        SimpleFormatter formatter = new SimpleFormatter(); 
        fileHandler.setFormatter(formatter); // Print a brief summary of the LogRecord in a readable format.
    }

    public String getFileName(){
        return filename;
    }

    public Logger getLogger() {
        return logger;
    }
}
