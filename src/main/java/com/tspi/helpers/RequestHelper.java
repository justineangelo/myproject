/*
The MIT License (MIT)

Copyright (c) 2015 JRANGEL

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR 
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
DEALINGS IN THE SOFTWARE.
*/
package com.pgc.xmp.utilities;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import org.json.simple.*;

/**
 * Request Helper :D
 * 
 * @author JRANGEL
 */
public class RequestHelper{
    public enum RequestMethod{
        POST, GET
    }
    public enum ContentType{
        JSON, XML, DEFAULT
    }
    private static final RequestHelper self = new RequestHelper();
   
    private String strURI;
    private RequestMethod rt;//default request method
    private int requestTimeout;//default request timeout in miliseconds
    private int readTimeout;//default read timeout in mili seconds
    private HashMap<String, String> requestData;
    private HashMap<String, String> requestProperty;
    private ContentType ct;
    private String requestRawData;
    
    private boolean errorOccurred = false;
    private String errorDescription = "";
    private String responseString = "";
    private int headerResponseCode = 0;
    private String headerResponseDesc = "";
    
    private RequestHelper(){
        //Private Constructor
        this.setDefault();
    }
    
    /**
     * Creates new instance of the Request Helper
     * 
     * @return returns the new instance of the Request Helper
     */
    public static RequestHelper getNewInstance(){
        return new RequestHelper();
    }
    /**
     * 
     * @return returns the static instance of the RequestHelper 
     */
    public static synchronized RequestHelper getInstance(){
        return self;
    }
    /**
     * Set Default values 
     */
    private void setDefault(){ 
        this.strURI = "";
        this.rt = RequestMethod.GET;
        this.requestTimeout = 5000;// 10seconds request default
        this.readTimeout = 5000;//10 seconds read default
        this.requestData = null;
        this.requestProperty = null;
        this.ct = ContentType.DEFAULT;
        this.requestRawData = "";
    }
    /**
     * 
     * @param uri
     * @return instance of the Request Helper
     */
    public RequestHelper setStringURI(String uri){
        this.strURI = uri;
        return this;
    }
    /**
     * Set the request property
     * 
     * @param requestPropety
     * @return instance of the Request Helper
     */
    public RequestHelper setRequestProperty(HashMap<String, String> requestPropety){
        this.requestProperty = requestPropety;
        return this;
    }
    /**
     * Set the request method
     * 
     * @param rt
     * @return instance of the Request Helper
     */
    public RequestHelper setRequestMethod(RequestMethod rt){
        this.rt = rt;
        return this;
    }
    /**
     * 
     * @param timeout
     * @return instance of the Request Helper
     */
    public RequestHelper setRequestConnnectionTimeout(int timeout){
        this.requestTimeout = timeout;
        return this;
    }
    /**
     * 
     * @param timeout
     * @return instance of the Request Helper
     */
    public RequestHelper setRequestReadTimeout(int timeout){
        this.readTimeout = timeout;
        return this;
    }
    
    public RequestHelper setContentType(ContentType ct){
        this.ct = ct;
        return this;
    }
    /**
     * Set request raw data preferably a raw string of json/xml
     * 
     * @param requestRawData
     * @return instance of the Request Helper
     */
    public RequestHelper setRequestRawData(String requestRawData){
        this.requestRawData = requestRawData;
        return this;
    }
    /**
     * Sets the request Data for POST and GET. When this has value in GET Method this will override the GET request
     * parameters
     * 
     * @param requestData
     * @return instance of the Request Helper
     */
    public RequestHelper setRequestData(HashMap<String, String> requestData){
        this.requestData = requestData;
        return this;
    }
    /**
     * Get the error description
     * 
     * @return returns error description
     */
    public String getErrorDescription(){
        return this.errorDescription;
    }    
    /**
     * Return true if error occurred
     * 
     * @return returns true if error occurred
     */
    
    public boolean errorOccurred(){
        return this.errorOccurred;
    }
    /**
     * The response string generated by the Request Helper
     * 
     * @return response string
     */
    public String getResponseString(){
        return this.responseString;
    }
    /**
     * Returns the header response code
     * 
     * @return returns the header response code
     */
    public int getHeaderResponseCode(){
        return this.headerResponseCode;
    }
    /**
     * Returns the header response description
     * 
     * @return returns the header response description
     */
    public String getHeaderResponseDescription(){
        return this.headerResponseDesc;
    }
    /**
     * Starts the request/Execute the connection
     * 
     * @return instance of the Request Helper
     */
    
    public RequestHelper startRequest(){
        this.errorOccurred = false;
        this.errorDescription = "";
        this.responseString = "";
        this.headerResponseCode = 0;
        this.headerResponseDesc = "";
        HttpURLConnection connection = null;
        try {
            StringBuilder sBRD = new StringBuilder();
            boolean isFirstAppend = true;
            if(this.requestData != null){
                for (HashMap.Entry entry : this.requestData.entrySet()) {
                    if(!isFirstAppend){
                        sBRD.append("&");
                    }
                    sBRD.append(entry.getKey());
                    sBRD.append("=");
                    sBRD.append(entry.getValue());
                    isFirstAppend = false;
                }
            }
            if(this.rt == RequestHelper.RequestMethod.GET && this.requestData != null){
                if(this.strURI.contains("?")){
                    this.strURI = this.strURI.split("\\?")[0];
                }
                this.strURI += "?" + sBRD.toString();
            }
            URL url = new URL(this.strURI);
            connection =  (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("Accept", "*/*");
            //set request property
            if(this.ct == ContentType.JSON){
                connection.setRequestProperty("Content-Type", "applicaiton/json");
            }else if(this.ct == ContentType.XML){
                connection.setRequestProperty("Content-Type", "applicaiton/xml");
            }
            if(this.requestProperty != null){
                for (HashMap.Entry<String, String> entry : this.requestProperty.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            connection.setConnectTimeout(this.requestTimeout);
            connection.setReadTimeout(this.readTimeout);
            connection.setAllowUserInteraction(false);
            if(this.rt == RequestMethod.GET){
                connection.setRequestMethod("GET");
            }else if(this.rt == RequestMethod.POST){
                connection.setRequestMethod("POST");
                if(this.requestData != null || this.requestRawData.length()>0){
                    connection.setDoOutput(true);
                    try {
                        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                        if(this.requestRawData.length()>0){
                            wr.writeBytes(this.requestRawData);
                        }else{
                            wr.writeBytes(sBRD.toString());
                        }
                        
                        wr.flush();
                        wr.close();
                    }catch(Exception e){
                        System.out.println("ERROR Closing Connection " + e.getMessage());
                        this.errorOccurred = true;
                        this.errorDescription = "closing connection " + e.getMessage();
                    }
                }
                else{
                    throw new Exception("Using POST Method but, No Request Data");
                }
            }
            connection.connect();
        } catch (Exception e) {
            this.errorOccurred = true;
            this.errorDescription = e.getMessage();
        }finally{
            try {
                if(connection!=null){
                    int status = this.headerResponseCode = connection.getResponseCode();
                    this.headerResponseDesc = connection.getResponseMessage();
                    if(status == 200){
                        String outString = "";
                        try {
                            InputStream inputStream = connection.getInputStream();
                            outString = this.streamReader(inputStream);
                            
                            inputStream.close();
                        } catch(Exception e){
                            System.out.println("ERROR : " + e.getMessage());
                            errorOccurred = true;
                            errorDescription = e.getMessage();
                        } finally{
                            
                        }
                        this.responseString = outString;
                    }else{
                        this.errorOccurred = true;
                        this.errorDescription = "Response Code " + status + " Response Description : " + connection.getResponseMessage();
                    }
                    connection.disconnect();
                }
            } catch (Exception e) {
                this.errorOccurred = true;
                this.errorDescription = e.getMessage();
            }finally{
                /*parsing start*/
                this.setDefault();//set default
            }
        }
        return this;
    }
    /**
     * read input stream and convert it to string
     * 
     * @param stream
     * @return
     * @throws Exception 
     */
    private String streamReader(InputStream stream) throws Exception{
        BufferedReader br = null;
        StringBuilder sb = null;
        String retString = "";
        try {
            br = new BufferedReader(new InputStreamReader(stream));
            sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            throw e;
        }finally{
            if(br!=null){
                br.close();
            }
            if(sb!=null){
                retString = sb.toString();
            } 
        }
       return retString;
    }
}