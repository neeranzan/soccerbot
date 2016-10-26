/**
 * Created by Jarre on 9/13/2016.
 */

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/** Data class to hold xml information **/



public class Record {

    /** testing flag **/
    boolean REMOVE = true;

    /** Lists for matching and getting responses **/
    private ArrayList<String> phraseList;
    private ArrayList<String> keywordList;
    private ArrayList<String> responseList;
    /** Lists for multipart special case **/
    private ArrayList<String> affirmativeList;
    private ArrayList<String> negativeList;
    private ArrayList<String> unknownMultiList;
    private ArrayList<String> possibleAffirmativesList;
    private ArrayList<String> possibleNegativesList;
    /** List for exceeding memory threshold **/
    private ArrayList<String> memoryExceededList;
    /** State variables about special cases **/
    private boolean shouldQuit = false;
    private int timesUsed = 0;
    private int memoryThreshold = 1;
    
    public Record() {
        this.phraseList = new ArrayList<>();
        this.keywordList = new ArrayList<>();
        this.responseList = new ArrayList<>();
    }

    public void addPhrase(String inputPhrase) {
        this.phraseList.add(inputPhrase);
    }

    public void addKeyword(String inputKeyword) {
        this.keywordList.add(inputKeyword);
    }

    public void addResponse(String inputResponse) {
        this.responseList.add(inputResponse);
    }

    public void addExceededMemory(String inputResponse) {
        if(this.memoryExceededList == null) this.memoryExceededList = new ArrayList<>();
        this.memoryExceededList.add(inputResponse);
    }

  

    public String checkPhraseForMatch(String chunks) {
        for (String match : phraseList) {
        	
        		 chunks=chunks.toLowerCase().replaceAll("\\p{Punct}+", "").trim();
                 if(chunks.matches("(.*)"+match.trim().toLowerCase()+"(.*)")) 
                	 return match.trim().toLowerCase();
        	}
        
        return null;
    }

    public String checkKeywordForMatch(String inputKeyword) {
        for (String match : keywordList) {
            inputKeyword=inputKeyword.toLowerCase().replaceAll("\\p{Punct}+", "").trim();
            if(inputKeyword.matches("(.*)"+match.trim().toLowerCase()+"(.*)"))
            	return match.trim().toLowerCase();
        }
        return null;
    }

    public String getResponse(String keyword) {
    	
        return getResponse(this.responseList,true);
    }

    public String getResponse(ArrayList<String> list, boolean remove) {
        if (this.memoryThreshold <= this.timesUsed && list != this.memoryExceededList) return getResponse(memoryExceededList, false);
        if(list ==this.affirmativeList || list==this.negativeList) this.timesUsed++;
        if (list.isEmpty()) return null;
        Random randomGen = new Random();
        int selection = randomGen.nextInt(list.size());
        String retString = list.get(selection);
        if(remove) list.remove(selection);
        return retString;
    }
    
    public String checkResponse(String keyword) {
        if (this.responseList.isEmpty()) return null;
        for (String line : this.responseList){
   	        
   	        	if(line.toLowerCase().matches(".*"+keyword.trim().toLowerCase()+".*"))
   	        		this.responseList.remove(this.responseList.indexOf(line));
   	        		return line;
   	        }
       // String[] splitString = keyword.trim().split("");
        //need to do something here for unknown response. how to find keyword
      
        return getResponse(this.responseList,true);
    }

    public void setMultipartLists(String[] affirmative, String[] negative, String[] unknown, String[] possAffirm, String[] possNega) {
        this.affirmativeList = generateMultiList(affirmative);
        this.negativeList = generateMultiList(negative);
        this.unknownMultiList = generateMultiList(unknown);
        this.possibleAffirmativesList = generateMultiList(possAffirm);
        this.possibleNegativesList = generateMultiList(possNega);
    }

    private ArrayList<String> generateMultiList(String[] array) {
        for(int i = 0; i < array.length; i++) array[i] = array[i].trim();
        ArrayList<String> retList = new ArrayList<>(Arrays.asList(array));
        return retList;
    }

    public String getMultipartResponse(String inputString) {
        String retString;
        if((retString = getMultipartMatch(inputString)) != null) return retString;
        String[] splitString = inputString.trim().split("");
        for(String string : splitString) {
            if((retString = getMultipartMatch(string)) != null) return retString;
        }
        return getResponse(unknownMultiList, this.REMOVE);
    }

    private String getMultipartMatch(String inputKeyword) {
        for(String match : possibleAffirmativesList) {
        	 inputKeyword=inputKeyword.toLowerCase().replaceAll("\\p{Punct}+", "").trim();
             if(inputKeyword.matches("(.*)"+match.trim().toLowerCase()+"(.*)")) 
            	 return  getResponse(this.affirmativeList, this.REMOVE);
            	
            }
        
        for(String match : possibleNegativesList) {
        	 inputKeyword=inputKeyword.toLowerCase().replaceAll("\\p{Punct}+", "").trim();
             if(inputKeyword.matches("(.*)"+match.trim().toLowerCase()+"(.*)")) 
            	 return  getResponse(this.negativeList, this.REMOVE);
            }
        
        return null;
    }

    public boolean isMulti() {
        if (affirmativeList != null && negativeList != null && unknownMultiList != null) return true;
        return false;
    }

    public boolean isShouldQuit() {
        return shouldQuit;
    }

    public void setShouldQuit(boolean shouldQuit) {
        this.shouldQuit = shouldQuit;
    }

    public void setMemoryThreshold(int memoryThreshold) {
        this.memoryThreshold = memoryThreshold;
    }

    public void dumpRecord() {
        System.out.println("*Dumping Record*");
        dumpList(phraseList, "Phrases");
        dumpList(keywordList, "Keywords");
        dumpList(responseList, "Responses");
        if(affirmativeList != null) dumpList(affirmativeList, "Affirmative Responses");
        if(negativeList != null) dumpList(negativeList, "Negative Responses");
        if(unknownMultiList != null) dumpList(unknownMultiList, "Unknown Responses");
        if(possibleAffirmativesList != null) dumpList(possibleAffirmativesList, "Possible Affirmatives");
        if(possibleNegativesList != null) dumpList(possibleNegativesList, "Possible Negatives");
        if(memoryExceededList != null) dumpList(memoryExceededList, "Memory Exceeded Responses");

        System.out.println("");
    }

    private void dumpList(ArrayList<String> list, String listName) {
        System.out.println("(" + list.size() + ") " + listName);
        for(String string : list) System.out.println("\t\t" + string);
    }
}
