import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * Created by Jarre on 9/10/2016.
 */

/* Command line testing application */

public class TestApplication {
	
	
    public static void main(String[] args) {

    	Date initialDate= new Date();
    	
        ChatbotParser chatbot = new ChatbotParser("war/resources/soccer.xml", "war/resources/log.txt");

        System.out.println("Testing");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (!chatbot.shouldQuit()) {
        	Date now = new Date();
            try {
                Thread.sleep(1000);
                String myString = br.readLine();
                System.out.println(chatbot.parse(myString, (now.getTime()-initialDate.getTime())/1000));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
