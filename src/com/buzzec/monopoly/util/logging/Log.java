package com.buzzec.monopoly.util.logging;

import com.buzzec.monopoly.util.Reference;

import java.io.*;
import java.time.LocalDateTime;

/**
 * @author Buzzec
 */
public class Log {
    private String
            logFileName;

    public Log(){
        this("logs", null, true);
    }
    public Log(String logFileFolder, String name, boolean useGeneratedName){
        if(useGeneratedName){
            setLogFileName(logFileFolder + "\\" + LocalDateTime.now().toString().replace(':', '.'));
        }
        else {
            setLogFileName(logFileFolder + "\\" + name);
        }
        if(new File(logFileFolder).mkdirs()){
            Reference.MAIN_LOG.log("[util.logging.Log] Made dir: " + logFileFolder);
        }
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }
    public void log(String string){
        BufferedWriter bw = null;
        FileWriter fw = null;
        try{
            fw = new FileWriter(logFileName, true);
            bw = new BufferedWriter(fw);
            bw.write("[" + LocalDateTime.now() + "] " + string + "\n");
        }
        catch(IOException e){
            e.printStackTrace();
        }
        finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    public void log(Object input){
        log(input.toString());
    }
}