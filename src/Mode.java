
import java.util.*;
public class Mode {
	
	private boolean REMOVE=true;
	
	private ArrayList<String> contentList;

	public Mode(){
		this.contentList= new ArrayList();
	}
	public Mode(ArrayList<String> s){
		this.contentList=s;
	}
	public void setContentList(ArrayList<String> s){
		contentList=s;
	}
	public ArrayList<String> getContentList(){
		return this.contentList;
	}
	
	 public String checkKeywordForMatch(String inputKeyword) {
	        for (String match : contentList) {
	            if (inputKeyword.toLowerCase().trim().replaceAll("\\p{Punct}+", "").matches(".*"+match.trim().toLowerCase()+".*")) 
	            	return match.trim().toLowerCase();
	        }
	        return null;
	 }
	
	 public String getResponse() {
	        return getResponse(this.contentList,true);
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
	        if (this.contentList.isEmpty()) return null;
	        for (String line : this.contentList){
	   	        
	   	        	line=line.toLowerCase().trim().replaceAll("\\p{Punct}+", "");
	   	        	
	   	        	if(line.matches(".*"+keyword.trim().toLowerCase()+".*")){ 
	   	        		this.contentList.remove(this.contentList.indexOf(line));
	   	        		return line;
	   	        	}
	   	        }
	        
	        return getResponse(this.contentList,true);
	    }

}
