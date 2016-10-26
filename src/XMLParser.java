import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Jarre on 9/13/2016.
 */
public class XMLParser {

    private String dataPath;
    Document doc;

    XMLParser() {
        this("resources/soccer.xml");
    }

    XMLParser(String dataPath) {
        setDataPath(dataPath);
        doc=getDocument();
    }
    
    public ArrayList<Connotation> populateConnotations(){
    	
    	ArrayList<Connotation> lstCon= new ArrayList<Connotation>();
    	NodeList nList= doc.getElementsByTagName("connotation");
    	 for (int temp = 0; temp < nList.getLength(); temp++) {
             Node nNode = nList.item(temp);
             if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                 Element eElement = (Element) nNode;
                 Connotation con= new Connotation();
                 con.setKey(getSingleData(eElement,"key"));
                 con.setTriggerList(new ArrayList<String>(Arrays.asList(getData(eElement,"trigger"))));
                 con.setResponseList(new ArrayList<String>(Arrays.asList(getData(eElement,"response"))));
                lstCon.add(con);
             }
         }
    	 return lstCon;
    	
    }
    
    public ArrayList<Mode> populateModes(){
    	ArrayList<Mode> lstMode= new ArrayList<Mode>();
    	NodeList nList= doc.getElementsByTagName("mode");
    	 for (int temp = 0; temp < nList.getLength(); temp++) {
             Node nNode = nList.item(temp);
             if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                 Element eElement = (Element) nNode;
                 Mode mode= new Mode();
                 mode.setContentList(new ArrayList<String>(Arrays.asList(getData(eElement))));
                 lstMode.add(mode);
             }
         }
    	 return lstMode;
        
    }
    public ArrayList<Record> populateRecords() {
        
        NodeList nList = doc.getElementsByTagName("record");
        ArrayList<Record> recordList = new ArrayList<>();
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                recordList.add(generateRecord(eElement));
            }
        }
        return recordList;
    }

    private Document getDocument() {
        try {
            File inputFile = new File(this.dataPath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Record generateRecord(Element eElement) {
        Record retRecord = new Record();
        String[] parts = getData(eElement, "phrase");
        for (int i = 0; i < (parts.length ); i++) retRecord.addPhrase(parts[i].trim());
        parts = getData(eElement, "keyword");
        for (int i = 0; i < (parts.length); i++) retRecord.addKeyword(parts[i].trim());
        parts = getData(eElement, "response");
        for (int i = 0; i < (parts.length ); i++) retRecord.addResponse(parts[i].trim());
        setUpRecordFlags(eElement, retRecord);
        return retRecord;
    }

    private void setUpRecordFlags(Element eElement, Record retRecord) {
        // Handle special cases here
        retRecord.setShouldQuit(eElement.getElementsByTagName("quit").getLength() > 0);
        if (eElement.getElementsByTagName("multipart").getLength() > 0) {
            String[] tempAffirmativeParts = getData(eElement, "affirmative");
            String[] tempNegativeParts = getData(eElement, "negative");
            String[] tempUnknownParts = getData(eElement, "unknown");
            String[] tempPossAffirm = (eElement.getElementsByTagName("possibleaffirmatives").getLength() > 0) ?
                   getData(eElement, "possibleaffirmatives") :  getDefaultAffirmatives();
            String[] tempPossNega = (eElement.getElementsByTagName("possiblenegatives").getLength() > 0) ?
                    getData(eElement, "possiblenegatives") : getDefaultNegatives();
            retRecord.setMultipartLists(tempAffirmativeParts, tempNegativeParts, tempUnknownParts, tempPossAffirm, tempPossNega);
        }
        if (eElement.getElementsByTagName("memorythreshold").getLength() > 0) {
            String[] tempMemoryThreshold = getData(eElement, "memorythreshold");
            retRecord.setMemoryThreshold(Integer.parseInt(tempMemoryThreshold[0].trim()));
            String[] tempExceededMemory = getData(eElement, "exceededmemory");
            for (int i = 0; i < (tempExceededMemory.length); i++)
                retRecord.addExceededMemory(tempExceededMemory[i].trim());
        }
       
    }
    
    public String[] getReactions(){
    	return getDefault("reaction");
    }
    
    public String[] getDefaultResponses(){
    	return getDefault("defaultresponse");
    }

   private String[] getDefaultAffirmatives() {
        return getDefault("defaultaffirmative");
    }

    private String[] getDefaultNegatives() {
        return getDefault("defaultnegative");
    }
    

    private String[] getDefault(String type) {
        NodeList nList = doc.getElementsByTagName(type);
        Node nNode = nList.item(0);
        Element eElement = (Element) nNode;
        return getData(eElement, type);
    }

    private String[] getData(Element eElement, String tagName) {
        return eElement.getElementsByTagName(tagName).item(0).getTextContent().trim().split("\\n");
    }
    private String[] getData(Element eElement) {
        return eElement.getTextContent().trim().split("\\n");
    }
    
    private String getSingleData(Element elem, String tagName){
    	return elem.getElementsByTagName(tagName).item(0).getTextContent().trim();
    }

    public Record getUnknownResponses() {
        Record retRecord = new Record();
        NodeList nList = doc.getElementsByTagName("unknownresponse");
        Node nNode = nList.item(0);
        Element eElement = (Element) nNode;
        String[] parts = getData(eElement, "response");
        for (int i = 0; i < (parts.length); i++) retRecord.addResponse(parts[i].trim());
        return retRecord;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

}