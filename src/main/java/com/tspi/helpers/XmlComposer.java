package com.tspi.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author JRANGEL
 */
enum XmlComposerSingleton{
    INSTANCE;
    private boolean errorOccurred = false;
    private String errorMsg = "";
    
    public String objectToXml(Object xmlObject){
        return this.xmlCompose(xmlObject);
    }
    /**
     * Does error occurred during composition?
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
     * the Core for XML compose
     * 
     * @param xmlObject
     * @return returns the XML String composed
     */
    private String xmlCompose(Object xmlObject){
        StringBuilder sb = new StringBuilder();
        this.errorOccurred = false;
        this.errorMsg = "";
        try {
            if(xmlObject instanceof HashMap){
                HashMap<String, Object> newXmlObj = new HashMap<>();
                newXmlObj.putAll((HashMap<String, Object>)xmlObject);//copy all content of the hash map

                for (Map.Entry<String, Object> entrySet : ((HashMap<String, Object>)xmlObject).entrySet()) {
                    if(entrySet.getKey().charAt(0) == '-'){
                        //this is an attribute
                        newXmlObj.remove(entrySet.getKey());
                    }
                }  
                xmlObject = newXmlObj;
                for (HashMap.Entry<String, Object> entry  : ((HashMap<String, Object>)xmlObject).entrySet()) {
                    if(entry.getValue() instanceof ArrayList){
                        for (Object hashObj : ((ArrayList<HashMap<String, Object>>)entry.getValue())) {
                            sb.append("<").append(entry.getKey());
                            if(hashObj instanceof HashMap)
                            {
                                HashMap<String, Object> newHashObj = new HashMap<>();
                                newHashObj.putAll((HashMap<String, Object>)hashObj);//copy all content of the hash map

                                for (Map.Entry<String, Object> entrySet : ((HashMap<String, Object>)hashObj).entrySet()) {
                                    if(entrySet.getKey().charAt(0) == '-'){
                                        //this is an attribute
                                        newHashObj.remove(entrySet.getKey());
                                        sb.append(" ");
                                        sb.append(entrySet.getKey().replace("-", "")).append("=\"").append(entrySet.getValue()).append("\"");
                                    }
                                }  
                                hashObj = newHashObj;
                            }
                            sb.append(">");
                            sb.append(this.xmlCompose(hashObj));
                            sb.append("</").append(entry.getKey()).append(">");
                        }
                    }
                    else if(entry.getValue() instanceof HashMap){
                        HashMap<String, Object> newHashObj = new HashMap<>();
                        newHashObj.putAll((HashMap<String, Object>)entry.getValue());//copy all content of the hash map
                        sb.append("<").append(entry.getKey());
                        for (Map.Entry<String, Object> entrySet : ((HashMap<String, Object>)entry.getValue() ).entrySet()) {
                            if(entrySet.getKey().charAt(0) == '-'){
                                //this is an attribute
                                newHashObj.remove(entrySet.getKey());
                                sb.append(" ");
                                sb.append(entrySet.getKey().replace("-", "")).append("=\"").append(entrySet.getValue()).append("\"");

                            }
                        }  
                        sb.append(">");
                        sb.append(this.xmlCompose(newHashObj));
                        sb.append("</").append(entry.getKey()).append(">");
                    }
                    else if(entry.getValue() instanceof String && entry.getValue().toString().equalsIgnoreCase("")){
                        sb.append("<").append(entry.getKey()).append("/>");
                    }
                    else{
                        sb.append("<").append(entry.getKey()).append(">");
                        sb.append(this.xmlCompose(entry.getValue()));
                        sb.append("</").append(entry.getKey()).append(">");
                    }
                }
            }
            else{
                sb.append(xmlObject.toString());
            }
        } catch (Exception e) {
            this.errorOccurred = true;
            this.errorMsg = e.getMessage();
        }
        return sb.toString();
    }
}

/**
 *
 * @author JRANGEL
 */
public class XmlComposer {
    private String composedXmlString = "";
    private static final XmlComposer self = new XmlComposer();
    private XmlComposer(){
        //Constructor
    }
    public static synchronized XmlComposer getInstance(){
        return self;
    }
    /**
     * Does error occurred during composing?
     * 
     * @return returns true when error occurred during composition
     */
    public boolean errorOccurred(){
        return XmlComposerSingleton.INSTANCE.errorOccurred();
    }
    /**
     * What is the cause of error?
     * 
     * @return returns Error message if error occurred
     */
    public String errorMsg(){
        return XmlComposerSingleton.INSTANCE.errorMsg();
    }
    
    /**
     * Compose the XML string for a Object
     * 
     * @param xmlObject
     * @return returns the composed XML string of the Object 
     */
    public String objectToXml(Object xmlObject){
        return XmlComposerSingleton.INSTANCE.objectToXml(xmlObject);
    }
    
    
}
