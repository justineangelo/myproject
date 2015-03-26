package com.tspi.helpers;

import com.tspi.template.CoreTemplate;
import java.util.ArrayList;

/**
 *
 * @author JRANGEL
 */
public class ThreadUtility extends CoreTemplate implements Runnable{
    private ArrayList<ThreadContent> threadContents;
    private Thread t = null;
    private int priority = 5;
    private String threadName = "";
    public ThreadUtility(String threadName){
        this.loggerDebug("INITIALIZED THREAD : " + threadName);
        this.threadName = threadName;
    }
    
    public void setThreadContents(ArrayList<ThreadContent> threadContents){
        this.threadContents = threadContents;
    }
    
    @Override
    public void run() {
        this.loggerDebug("START RUN THREAD " + this.threadName);
        //Place code here
        
  
        this.loggerDebug("END RUN THREAD " + this.threadName);
    }
    /**
     * Call this to run the thread
     */
    public void start(){
        this.loggerDebug("STARTING THREAD " + this.threadName);
        if(t == null){
            t = new Thread(this, threadName);
            t.setPriority(priority);
            t.start();
        }
    }
    /**
     * Set Priority of the Thread
     * MIN = 1
     * MED = 5
     * MAX = 10
     * @param priority 
     */
    public void setPriority(int priority){
        this.priority = priority;
    }
}