package com.tspi.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

/**
 *  Utility for JSON
 * 
 * @author JRANGEL
 */
public class JsonUtility {
    
    private Object jObj;
    private boolean errorOccurred;
    private String errorDescription;
    private static final JsonUtility self = new JsonUtility();
    private JsonUtility(){
        //constructor
        this.clean();
    }
    /**
     * Get the Instance of JsonUtility
     * 
     * @return instance of JsonUtility 
     */
    public static synchronized JsonUtility getInstance(){
        return self;
    }
    /**
     * Get the error state
     * 
     * @return return true when error occurred during process
     */
    public boolean errorOccurred(){
        return this.errorOccurred;
    }
    /**
     * Get error description
     * 
     * @return returns the error description during the process 
     */
    public String getErrorDescription(){
        return this.errorDescription;
    }
    /**
     * Convert Object to String
     * 
     * @param jsonObject
     * @return JSON String
     */
    public String toJsonString(Object jsonObject){
        return JSONValue.toJSONString(jsonObject);
    }
    /**
     * Convert JSON String to JsonObject
     * 
     * @param jsonString
     * @return return the instance of the JsonUtility
     */
    public JsonUtility toJsonObject(String jsonString){
        this.clean();//clean new request start
        try {
            this.jObj = JSONValue.parseWithException(jsonString);
        } catch (ParseException e) {
            this.errorOccurred = true;
            this.errorDescription = e.getMessage();
        }finally{
            
        }
        return this;
    }
    /**
     * Get JSON Object for key and affecting the current JSON Object
     * 
     * @param key
     * @return return the instance of the JsonUtility
     */
    public JsonUtility getJsonObjectForKey(String key){
        if(this.jObj instanceof HashMap){
            HashMap<String, Object> jsonMap = (HashMap<String, Object>)this.jObj;
            this.jObj = jsonMap.get(key);
        }else{
            this.jObj = null;
            this.errorOccurred = true;
            this.errorDescription = "Json Object has no key to get Json Object";
        }
        return this;
    }
    /**
     * Copy JSON Object for key and not affecting the current JSON Object
     * 
     * @param key
     * @return return the instance of the JsonUtility
     */
    public JsonUtility copyJsonObjectForKey(String key){
        JsonUtility jU = new JsonUtility();
        if(this.jObj instanceof HashMap){
            HashMap<String, Object> jsonMap = (HashMap<String, Object>)this.jObj;
            jU.jObj = jsonMap.get(key);
        }else{
            jU.jObj = null;
            jU.errorOccurred = true;
            jU.errorDescription = "Json Object has no key to get Json Object";
        }
        return jU;
    }
    /**
     * Get JSON Object for index and affecting the current JSON Object
     * 
     * @param indx
     * @return return the instance of the JsonUtility
     */
    public JsonUtility getJsonObjectForIndex(int indx){
        if(this.jObj instanceof ArrayList){
            ArrayList<Object> jsonMap = (ArrayList<Object>)this.jObj;
            this.jObj = jsonMap.get(indx);
        }else{
            this.jObj = null;
            this.errorOccurred = true;
            this.errorDescription = "Json Object has no key to get Json Object";
        }
        return this;
    }
    /**
     * Copy JSON Object for index and not affecting the current JSON Object
     * 
     * @param indx
     * @return return the instance of the JsonUtility
     */
    public JsonUtility copyJsonObjectForIndex(int indx){
        JsonUtility jU = new JsonUtility();
        if(this.jObj instanceof ArrayList){
            ArrayList<Object> jsonMap = (ArrayList<Object>)this.jObj;
            jU.jObj = jsonMap.get(indx);
        }else{
            jU.jObj = null;
            jU.errorOccurred = true;
            jU.errorDescription = "Json Object has no key to get Json Object";
        }
        return this;
    }
    /**
     * Convert JsonObject to Object
     * 
     * @return 
     */
    public Object toObject(){
        return this.jObj;
    }
    /**
     * Clean
     */
    private void clean(){
        this.errorDescription = "";
        this.errorOccurred = false;
        this.jObj = null;
    }
}
