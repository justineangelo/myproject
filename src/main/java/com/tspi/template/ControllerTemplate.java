/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspi.template;

import java.io.File;
import java.io.IOException;


enum ControllerSingleton{
    INSTANCE;
    /**
     * Getter for is running
     * 
     * @return boolean 
     */
    private final String resourcePath = this.getClass().getClassLoader().getResource("").getPath();
    public boolean isRunning(String tag){
        File file = new File(this.resourcePath + tag);
        return file.exists();
    }
    /**
     * Setter for is Running
     */
    public void setIsRunning(String tag, boolean isRunning){
        File file = new File(this.resourcePath + tag );
        if(isRunning){
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    CoreSingleton.INSTANCE.myLogger().error("ERROR FILE RUNNING CREATION " + ex.getMessage());
                }
            }
        }else{//DELETE FILE
            if(file.exists()){
                file.delete(); 
            }
        }
    } 
}
/**
 *
 * @author JRANGEL
 */
public class ControllerTemplate extends CoreTemplate{
    public void test(){
        ControllerSingleton.INSTANCE.setIsRunning("test", true);
        ControllerSingleton.INSTANCE.setIsRunning("test", true);
        ControllerSingleton.INSTANCE.isRunning("test");
    }
    public boolean isRunning(String tag){
        return ControllerSingleton.INSTANCE.isRunning(tag);
    }
    public void setIsRunning(String tag, boolean isRunning){
        ControllerSingleton.INSTANCE.setIsRunning(tag, isRunning);
    } 
}
