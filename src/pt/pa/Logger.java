package pt.pa;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Logger {
    private static Logger singleton;
    private static final String LOGFILE = "log.txt";
    private BufferedWriter logFile;
    protected enum Type {INFO, WARNING , ERROR};

    private Logger(){
        openFile();
    }

    public  static Logger getInstance(){
        if(singleton == null){
            singleton = new Logger();
        }
        return singleton;
    }

    private void openFile(){
        try{
            logFile  = new BufferedWriter( new FileWriter(LOGFILE, true));
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void log(Type type, Object sender, String message){
        String t = type.toString();
        Date now = new Date();
        String logged = String.format("[%s] - %s - (%s): %s \n", t, now.toString(), sender.getClass().getSimpleName(), message);

        try{
            logFile.write(logged);
            logFile.flush();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }

    }

    public void logInfo(Object sender, String message){
        log(Type.INFO, sender,message);
    }

    public void logError(Object sender, String message){
        log(Type.ERROR, sender, message);
    }

    public void logWarning(Object sender, String message){
        log(Type.WARNING, sender, message);
    }
}
