package com.tspi.helpers;
import com.tspi.template.CoreTemplate;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.CDATASection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Attr;
import org.xml.sax.SAXException;

/**
 * A library/Helper for XML parsing. 
 * 
 * @author JRANGEL
 */
enum XmlParserSingleton{
    INSTANCE;
    private boolean includeAttr = false; 
    private boolean errorOccurred = false;
    private String errorMsg = "";
    /**
     * 
     * @param xmlString
     * 
     * @return Returns an Object or null value 
     */
    public Object xmlToObject(String xmlString){
        this.errorMsg = "";
        this.errorOccurred = false;
        return this.xmlParse(this.cleanXmlString(xmlString));
    }
    /**
     * Include attributes setter
     * 
     * @param includeAttr 
     */
    public void includeAttributes(boolean includeAttr){
        this.includeAttr = includeAttr;
    }
    /**
     * Does error occurred during parsing?
     * 
     * @return return true when error occurred is encountered
     */
    public boolean errorOccurred(){
        return this.errorOccurred;
    }
    /**
     * Error message for the current XML parsing
     * 
     * @return returns error message for the current parsing
     */
    public String errorMsg(){
        return this.errorMsg;
    }
    /**
     * Cleans unnecessary string err
     * 
     * @param xmlString
     * @return String
     */
    private String cleanXmlString(String xmlString){
        //remove uncessary characters
        String cleanedXmlString = xmlString
                .replaceAll("\n", " ")
                .replaceAll("\r", " ")
                .replaceAll("\t", " ")
                .replaceAll("\\<\\?xml(.+?)\\?\\>", "")//replace the XML tag maybe not necessary though
                .replaceAll("\\>( +?)\\<", "><")//replace spaces
                .trim();
        //uncomment to use remove attributes
        //cleanedXmlString = this.removeXmlAttrb(cleanedXmlString);
        return cleanedXmlString;
    }
    /**
     * Removes XML attributes from the tag
     * 
     * @param xmlString
     * @return String
     */
    private String removeXmlAttrb(String xmlString){
        StringBuilder sb = new StringBuilder();
        int cInK = 0;
        int cInV = 0;
        for (int i = 0; i < xmlString.length(); i++) {
            char c = xmlString.charAt(i);
            if(c == '<'){
                cInK = i;
            }
            if(c == '>'){
                cInV = i + 1;
                String toCleanStr = xmlString.substring(cInK, i+1);
                toCleanStr = toCleanStr
                        .replaceAll(" (.+?)\\>", ">")//replace " asfafaf> = >"
                        .replaceAll("\\<\\/(.+?):", "</")//replace "</fsafsa: = </"
                        .replaceAll("\\<(.+?):", "<")//replace "<fsafsa: = <"
                        ;
                sb.append(toCleanStr);
            }
            if(c == '<' && cInV != i){
                sb.append(xmlString.substring(cInV, i));
            }
        }
        return sb.toString();
    }
    /**
     * Main method to parse XML String
     * 
     * @param toParse
     * @return 
     */
    private HashMap<String, Object> xmlParse(String toParse){
        HashMap<String, Object> parsedXml = null;
        try {
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            ByteArrayInputStream input =  new ByteArrayInputStream(toParse.getBytes("UTF-8"));
            Document doc = builder.parse(input);
            doc.normalize();
            String rootElement = doc.getDocumentElement().getNodeName();
            parsedXml = new HashMap<>();
            parsedXml.put(rootElement, this.xmlElementParser(doc.getDocumentElement()));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            this.errorOccurred = true;
            this.errorMsg = e.getMessage();
        }
        return parsedXml;
    }
    /**
     * Recursively parse element via tag and child nodes
     * 
     * @param obj
     * @return 
     */
    private Object xmlElementParser(Object obj){
        Object retObject;
        Element xmlElement = (Element) obj; 
        HashMap<String, Object> elMap = new HashMap<>();
        if(xmlElement.hasChildNodes() && xmlElement.getFirstChild().getNodeType() == Node.TEXT_NODE){
            if(xmlElement.getFirstChild().getNodeValue()!=null){
                retObject = xmlElement.getFirstChild().getNodeValue();  
            }else{
                retObject = "";
            }
        }else{
            NodeList nList = xmlElement.getChildNodes();//get child nodes
            //find same tag name to become array
            ArrayList<String> nodeStore = new ArrayList<>();
            String storedString = "";
            for (int i = 0; i < nList.getLength(); i++) {
                if(storedString.equalsIgnoreCase(nList.item(i).getNodeName()) && !nodeStore.contains(nList.item(i).getNodeName())){
                    
                    nodeStore.add(nList.item(i).getNodeName());
                }
                storedString = nList.item(i).getNodeName();
            }
            ArrayList<Object> arrContent = new ArrayList<>();
            for (int i = 0; i < nList.getLength(); i++) {           
                if(nodeStore.contains(nList.item(i).getNodeName())){//for array
                    arrContent.add(this.xmlElementParser(nList.item(i)));
                    elMap.put(nList.item(i).getNodeName(), arrContent);
                }
                else{//fro mapping
                    if(nList.item(i) instanceof CDATASection){
                        CDATASection cDS = (CDATASection)nList.item(i);
                        return "<![CDATA[" + cDS.getWholeText() + "]]>";
                    }
                    else{
                        CoreTemplate.logDebug("XML NODE NAME = " + nList.item(i).getNodeName());
                        elMap.put(nList.item(i).getNodeName(), this.xmlElementParser(nList.item(i)));
                    }
                    
                }
            } 

        }
        //this will add attributes
        if(this.includeAttr){//set on the setter
            NamedNodeMap attrs = xmlElement.getAttributes();  
            for(int i = 0 ; i<attrs.getLength() ; i++) {
              Attr attribute = (Attr)attrs.item(i);
              elMap.put("-" + attribute.getName(), attribute.getValue());
            }
        }
        retObject = elMap;
        return retObject;
    }
}
/**
 * A library/Helper for XML parsing; 
 * 
 * @author JRANGEL
 */
public class XmlParser{
    private Object obj;
    private static final XmlParser self = new XmlParser();
    private XmlParser(){//prevent public access
        //CONSTRUCTOR
    }
    /**
     * Initialization of the Singleton
     * 
     * @return returns the Instance of the XmlParser
     */
    public static synchronized XmlParser getInstance(){
        return self;
    }
    /**
     * Does Error occurred during parsing?
     * 
     * @return returns true when error occurred during parsing
     */
    public boolean errrorOccurred(){
        return XmlParserSingleton.INSTANCE.errorOccurred();
    }
    /**
     * What is the cause of error?
     * 
     * @return returns Error message if error occurred
     */
    public String errorMsg(){
        return XmlParserSingleton.INSTANCE.errorMsg();
    }
    /**
     * Convert XML string to object
     * 
     * @param xmlString
     * @return returns the instance of the XmlParser
     */
    public XmlParser xmlToObject(String xmlString){
        obj = XmlParserSingleton.INSTANCE.xmlToObject(xmlString);
        return this;
    }
    /**
     * Sets if should include attributes in the XML parsing
     * 
     * @param includeAttr
     * @return returns the instance of the XmlParser
     */
    public XmlParser setIncludeAttributes(boolean includeAttr)
    {
        XmlParserSingleton.INSTANCE.includeAttributes(includeAttr);
        return this; 
    }
    /**
     * Get the object at index  and altering the current object
     * 
     * @param indx
     * @return returns the instance of the XmlParser
     */
    public XmlParser getObjectAtIndex(int indx){
        obj = ((ArrayList<Object>)obj).get(indx);
        return this;
    }
    /**
     * Copy the object at index and not altering the current object
     * 
     * @param indx
     * @return returns the instance of the XmlParser
     */
    public XmlParser copyObjectAtIndex(int indx){
        XmlParser xmlParser = new XmlParser();
        xmlParser.obj = ((ArrayList<Object>)obj).get(indx);
        return xmlParser;
    }
    /**
     * Get the object for key  and altering the current object
     * 
     * @param key
     * @return returns the instance of the XmlParser
     */
    public XmlParser getObjectForKey(String key){
        obj = ((HashMap<String, Object>)obj).get(key);
        return this;
    }
    /**
     * Copy the object for key and not altering the current object
     * 
     * @param key
     * @return returns the instance of the XmlParser
     */
    public XmlParser copyObjectForKey(String key){
        XmlParser xmlParser = new XmlParser();
        xmlParser.obj = ((HashMap<String, Object>)obj).get(key);
        return xmlParser;
    }
    /**
     * Gets the Object value of the XmlParser
     * 
     * @return  returns the Object of the XmlParser
     */
    public Object toObject(){
        return obj;
    }
}