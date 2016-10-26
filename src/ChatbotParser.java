/**
 * Created by Jarrett on 9/10/2016.
 */

import java.util.*;

public class ChatbotParser {

	private long thresholdTime = 100;// in seconds
	private int modeFactor = 0;// flag to figure out which mode is active
	private ArrayList<Record> recordList;
	private Record unknownResponses;
	private String[] defaultResponses;
	private String[] defaultReactions;
	private ArrayList<Mode> lstMode;
	private ArrayList<Connotation> lstCon;
	private XMLParser xmlParser;
	private Logger logger;

	private OpenNLP nlp;
	boolean isModeUsed = false;
	boolean isConnotationUsed = false;
	boolean quit = false;
	Record nextRecord = null;

	public ChatbotParser(String path, String logPath) {
		this.xmlParser = new XMLParser(path);
		this.recordList = xmlParser.populateRecords();
		this.unknownResponses = xmlParser.getUnknownResponses();
		this.defaultResponses = xmlParser.getDefaultResponses();
		this.defaultReactions = xmlParser.getReactions();
		this.lstMode = xmlParser.populateModes();
		this.lstCon = xmlParser.populateConnotations();
		this.logger = new Logger(logPath);
		this.logger.startLog();
		this.nlp = new OpenNLP();
		// for(Record record : recordList) { record.dumpRecord(); }
	}

	public enum Flag {
		MODE, CONNOTATION, RECORD, UNKNOWNRESPONSE, REACTION, DEFAULTRESPONSE
	}

	public String parse(String inputString, long elapsedTime) {

		String retString = null;
		if (nextRecord != null) {
			retString = nextRecord.getMultipartResponse(inputString);
			nextRecord = null;
			logger.log(inputString, retString, false);
			return retString;
		}

		//try{
		// get the noun phrase to match against phrases
		//Object[] chunks = nlp.getPhraseChunks(inputString).toArray()==null?(new Object[1]):
			//nlp.getPhraseChunks(inputString).toArray();
		

//		String[] chunkStrings = new String[1];
//		if (chunks.length > 0) {
//			chunkStrings = new String[chunks.length];
//			// check for connotation here..
//			for (int i = 0; i < chunks.length; i++) {
//				chunkStrings[i] = chunks[i].toString();
//			}
//		}
//		chunkStrings[0] = inputString;

		for (Record record : recordList) {
			String matchKey=record.checkPhraseForMatch(inputString);
			if (matchKey !=null) {
				if ((retString = record.getResponse(matchKey)) != null) {
					getFlags(record);
					logger.log(inputString, retString, false);
					return retString;
				}
			}
		}

		for (Record record : recordList) {
			String matchKey=record.checkKeywordForMatch(inputString);
			if (matchKey!=null) {
				if ((retString = record.getResponse(matchKey)) != null) {
					getFlags(record);
					logger.log(inputString, retString, false);
					return retString;
				}
			}
		}
		Random modRan = new Random();
		if (modRan.nextBoolean()) {
			for (Connotation con : lstCon) {
				String matchKey=con.checkKeywordForMatch(inputString);
				if (matchKey != null) {
					if ((retString = con.getResponse(matchKey)) != null) {
						logger.log(inputString, retString, false);
						isConnotationUsed = true;
						return retString;

					}
				}
			}
		}
		

		// check for time lapse here first to get modes data
		
		if (modRan.nextBoolean()) {
		
			modeFactor=modRan.nextInt(3);
				if ((retString = lstMode.get(modeFactor).getResponse()) != null) {
					logger.log(inputString, retString, false);
					if (modeFactor < lstMode.size())
						modeFactor++;
					isModeUsed = true;
					return retString;
				}
			
		}

		Random ran = new Random();
		int i = ran.nextInt(2);
		if (i == 0)
			retString = unknownResponses.getResponse(inputString);
		if (i == 1)
			retString = getResponseArray(defaultResponses, true);
		if (i == 2)
			retString = getResponseArray(defaultReactions, true);

		String tempString = getRandomConversationStarter(inputString);
		if (tempString != null){
			 if(i==2 && retString!=null){retString += tempString;}
			 else{retString = tempString;}
			logger.log(inputString, retString, true);
			
		}

		if (retString != null)
			return retString;
		retString = "I gotta go. Will catch you later. Bye!";
		this.quit = true;

		return retString;
	}

	// use modes here ..could be great
	private String getRandomConversationStarter(String inputString) {
		String retString;
		Random modRan = new Random();
		if (modRan.nextBoolean()) {
			for (Connotation con : lstCon) {
				String matchKey=con.checkKeywordForMatch(inputString);
				if (matchKey != null) {
					if ((retString = con.getResponse(matchKey)) != null) {
						logger.log(inputString, retString, false);
						isConnotationUsed = true;
						return retString;

					}
				}
			}
		}
		

		// check for time lapse here first to get modes data
		
		if (modRan.nextBoolean()) {
			modeFactor=modRan.nextInt(3);
				if ((retString = lstMode.get(modeFactor).getResponse()) != null) {
					logger.log(inputString, retString, false);
					if (modeFactor < lstMode.size())
					return retString;
				}
			
		}
		
		return null;
	}

	private void getFlags(Record record) {
		
		quit = record.isShouldQuit();
		if (record.isMulti()) {
			nextRecord = record;
		}
	}

	public boolean shouldQuit() {
		return quit;
	}

	public String getResponseArray(String[] list, boolean remove) {
		if (list.length == 0)
			return null;
		Random randomGen = new Random();
		int selection = randomGen.nextInt(list.length);
		String retString = list[selection];
		if (remove)
			list[selection] = null;
		return retString;
	}

}
