/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
            System.out.println(doc.getDocumentElement().getNodeName());
            parsedXml.put(rootElement, this.xmlElementParser(doc.getDocumentElement()));
            System.out.println(parsedXml.toString());
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println("Error Occured Parsing XML : " + e.getMessage());
        }
        return parsedXml;
    }
    /**
     * Recursively parse element via tag and child nodes
     * @param obj
     * @return 
     */
    private Object xmlElementParser(Object obj){
        Object retObject;
        Element xmlElement = (Element) obj;    
        if(xmlElement.getFirstChild().getNodeType() == Node.TEXT_NODE){
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
            CoreTemplate.logDebug("node name " + nodeStore.toString());
            ArrayList<Object> arrContent = new ArrayList<>();
            for (int i = 0; i < nList.getLength(); i++) {
                
                Object objFromParser;
                if(!nList.item(i).hasChildNodes()){
                    objFromParser = "";
                }
                else{
                    objFromParser = this.xmlElementParser(nList.item(i));
                }
                if(nodeStore.contains(nList.item(i).getNodeName())){
                    arrContent.add(objFromParser);
                    elMap.put(nList.item(i).getNodeName(), arrContent);
                }
                else{
                    elMap.put(nList.item(i).getNodeName(), objFromParser);
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
    private XmlParser(){
        //CONSTRUCTOR
    }
    /**
     * Convert XML string to object
     * @param xmlString
     * @return 
     */
    public static Object xmlToObject(String xmlString){

        return (Object)XmlParserSingleton.INSTANCE.xmlToObject(xmlString);
    }
    public static void setIncludeAttributes(boolean includeAttr)
    {
        XmlParserSingleton.INSTANCE.includeAttributes(includeAttr);
    }
}