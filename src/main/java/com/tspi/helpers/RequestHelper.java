/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspi.helpers;

import com.tspi.template.CoreTemplate;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


enum RequestHelperSingleton{
    INSTANCE;
    
    
}

/**
 *
 * @author JRANGEL
 */
public class RequestHelper extends CoreTemplate{
    public enum RequestMethod{
        POST, GET
    }
    private static final RequestHelper self = new RequestHelper();
   
    private String strURI;
    private RequestMethod rt;//default request method
    private int requestTimeout;//default request timeout in miliseconds
    private int readTimeout;//default read timeout in mili seconds
    private HashMap<String, String> postData;
    
    private boolean errorOccured;
    private String errorDescription;
    private String responceString;
    
    private RequestHelper(){
        //Private Constructor
        this.setDefault();
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
        
        this.errorOccured = false;
        this.errorDescription = "";
        this.responceString = "";
        
        this.strURI = "";
        this.rt = RequestMethod.GET;
        this.requestTimeout = 10000;// 10seconds request default
        this.readTimeout = 10000;//10 seconds read default
        this.postData = null;
    }
    
    
    public RequestHelper setStringURI(String uri){
        this.strURI = uri;
        return this;
    }
    public void setRequestMethod(RequestMethod rt){
        this.rt = rt;
    }
    public RequestHelper setRequestConnnectionTimeout(int timeout){
        this.requestTimeout = timeout;
        return this;
    }
    public RequestHelper setRequestReadTimeout(int timeout){
        this.readTimeout = timeout;
        return this;
    }
    public RequestHelper setPostData(HashMap<String, String> postData){
        this.postData = postData;
        return this;
    }
 
    
    public String getErrorDesciption(){
        return this.errorDescription;
    }
    public boolean errorOccured(){
        return this.errorOccured;
    }
    public String responseString(){
        return this.responceString;
    }
 
    
    public RequestHelper startRequest(){
        this.errorOccured = false;
        this.errorDescription = "";
        this.responceString = "";
        HttpURLConnection connection = null;
        try {
            URL url = new URL(this.strURI);
            connection =  (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("Accept", "*/*");
            connection.setConnectTimeout(this.requestTimeout);
            connection.setReadTimeout(this.readTimeout);
            connection.setAllowUserInteraction(false);
            if(this.rt == RequestMethod.GET){
                connection.setRequestMethod("GET");
            }else if(this.rt == RequestMethod.POST){
                connection.setRequestMethod("POST");
                if(!this.postData.isEmpty()){
                    boolean isFirstAppend = true;
                    StringBuilder sb = new StringBuilder();
                    for (HashMap.Entry entry : this.postData.entrySet()) {
                        if(!isFirstAppend){
                            sb.append("&");
                        }
                        sb.append(entry.getKey());
                        sb.append("=");
                        sb.append(entry.getValue());
                        isFirstAppend = false;
                    }
                    connection.setDoOutput(true);
                    try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                        wr.writeBytes(sb.toString());
                        wr.flush();
                        wr.close();
                    }catch(Exception e){
                        this.errorOccured = true;
                        this.errorDescription = "closing connection " + e.getMessage();
                    }
                }
            }
            connection.connect();
        } catch (Exception e) {
            this.errorOccured = true;
            this.errorDescription = e.getMessage();
        }finally{
            try {
                if(connection!=null){
                    int status = connection.getResponseCode();
                    if(status == 200){
                        String outString;
                        try ( //success
                            InputStream inputStream = connection.getInputStream()) {
                            outString = this.streamReader(inputStream);
                            this.responceString = outString;
                            inputStream.close();
                            // Do something on outString
                        }
                    }
                    else{
                        //fail
                    }
                    connection.disconnect();
                }
            } catch (Exception e) {
                this.errorOccured = true;
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
