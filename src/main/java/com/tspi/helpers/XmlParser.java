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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Attr;
import org.xml.sax.SAXException;

/**
 * A library/Helper for XML parsing attributes if attribute boolean is enabled will add to the existing child nodes 
 * 
 * @author JRANGEL
 */
enum XmlParserSingleton{
    INSTANCE;
    private boolean includeAttr = false; 
    
    /**
     * 
     * @param xmlString
     * 
     * @return Object 
     */
    public Object xmlToObject(String xmlString){
        CoreTemplate.logDebug("RAW " + xmlString);
        CoreTemplate.logDebug("CLEANED " + this.cleanXmlString(xmlString));
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
     * Cleans unnecessary string err
     * 
     * @param xmlString
     * @return String
     */
    private String cleanXmlString(String xmlString){
        
        String cleanedXmlString = xmlString
                .replaceAll("\n", " ")
                .replaceAll("\r", " ")
                .replaceAll("\t", " ")
                .replaceAll("\\<\\?xml(.+?)\\?\\>", "")
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
            parsedXml = new HashMap<>();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            ByteArrayInputStream input =  new ByteArrayInputStream(toParse.getBytes("UTF-8"));
            Document doc = builder.parse(input);
            doc.normalize();
            String rootElement = doc.getDocumentElement().getNodeName();
            CoreTemplate.logDebug(doc.getDocumentElement().getNodeName());
            parsedXml.put(rootElement, this.xmlElementParser(doc.getDocumentElement()));
            CoreTemplate.logDebug(parsedXml.toString());
        } catch (ParserConfigurationException | SAXException | IOException e) {
            CoreTemplate.logDebug("Error Occured Parsing XML : " + e.getMessage());
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
        if(xmlElement.hasChildNodes() && xmlElement.getFirstChild().getNodeType() == Node.TEXT_NODE){
            if(xmlElement.getFirstChild().getNodeValue()!=null){
                retObject = xmlElement.getFirstChild().getNodeValue();  
            }else{
                retObject = "";
            }
        }else{
            HashMap<String, Object> elMap = new HashMap<>();
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
                    elMap.put(nList.item(i).getNodeName(), this.xmlElementParser(nList.item(i)));
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
        }
        return retObject;
    }
}
/**
 * A library/Helper for XML parsing attributes if attribute boolean is enabled will add to the existing child nodes 
 * 
 * @author JRANGEL
 */
public class XmlParser extends CoreTemplate{
    private Object obj;
    static XmlParser self = null;
    private XmlParser(){//private to avoid initialization
        //CONSTRUCTOR
    }
    
    public static synchronized XmlParser getInstance(){
        if(self == null){
            self = new XmlParser();
        }
        return self;
    }
    /**
     * Convert XML string to object
     * @param xmlString
     * @return 
     */
    public XmlParser xmlToObject(String xmlString){
        obj = XmlParserSingleton.INSTANCE.xmlToObject(xmlString);
        return self;
    }
    public XmlParser setIncludeAttributes(boolean includeAttr)
    {
        XmlParserSingleton.INSTANCE.includeAttributes(includeAttr);
        return self; 
    }
    public XmlParser getObjectForIndex(int indx){
        ArrayList<Object> arrObj = (ArrayList<Object>)obj;
        obj = arrObj.get(indx);
        return self;
    }
    public XmlParser getObjectForKey(String key){
        HashMap<String, Object> hashObj = (HashMap<String, Object>)obj;
        obj = hashObj.get(key);
        return self;
    }
    public Object toObject(){
        
        return obj;
    }
    
}