import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by Jarre on 9/16/2016.
 */
public class Logger {

    String dataPath;

    public Logger() {
        this("resources/log.txt");
    }

    public Logger(String dataPath) {
        this.dataPath = dataPath;
    }

    public void startLog() {
        String printString = "\tSTART OF LOG\n\n";
        try {
            Files.write(Paths.get(dataPath), printString.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(String inputString, String response, boolean unknown) {
        String printString = (unknown ? "*UNKNOWN* \n" : "");
        printString = printString + "Input: " + inputString + " \tResponse : " + response + "\n\n";
        try {
            Files.write(Paths.get(dataPath), printString.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
