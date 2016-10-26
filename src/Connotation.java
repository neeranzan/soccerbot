import java.util.*;
public class Connotation {
	
	boolean REMOVE=true;
	private String key;
	private ArrayList<String> triggerList;
	private ArrayList<String> responseList;
	
	public Connotation(){
		this.triggerList = new ArrayList<String>();
		this.responseList = new ArrayList<String>();
	}
	
	public void setKey(String key){
		this.key=key;
	}
	
	public String getKey(){
		return this.key;
	}
	
	public void setTriggerList(ArrayList<String> s){
		this.triggerList=s;
	}
	public ArrayList<String> getTriggerList(){
		return this.triggerList;
	}
	
	public void setResponseList(ArrayList<String> s){
		this.responseList=s;
	}
	public ArrayList<String> getResponseList(){
		return this.responseList;
	}
	 public String checkKeywordForMatch(String inputKeyword) {
	        for (String match : triggerList) {
	          // inputKeyword=inputKeyword.toLowerCase().trim().replaceAll("\\p{Punct}+", "");
	        	inputKeyword=inputKeyword.toLowerCase().trim().replaceAll("\\p{Punct}+", "");
	        	
	        	if(inputKeyword.matches(".*"+match.trim().toLowerCase()+".*")) 
	        		return match.trim().toLowerCase();
	        }
	        return null;
	 }
	 
	 public String getResponse(String keyword) {
	        return checkResponse(keyword);
	    }

	    public String getResponse(ArrayList<String> list, boolean remove) {
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
	   	        
	   	        	line=line.toLowerCase().trim().replaceAll("\\p{Punct}+", "");
	   	        	
	   	        	if(line.matches(".*"+keyword.trim().toLowerCase()+".*")) {
	   	        		this.responseList.remove(this.responseList.indexOf(line));
	   	        		return line;
	   	        	}
	   	        }
	        
	        return getResponse(this.responseList,true);
	    }


}
