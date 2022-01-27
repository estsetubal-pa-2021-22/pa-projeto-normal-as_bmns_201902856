package pt.pa;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import pt.pa.javafxinterface.LoggerJavaFX;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The functionality of the logger feature.
 * It allows you to save certain events with a date associated.
 */
public class Logger {
    private static Logger singleton;
    private static final String LOGFILE = "log.txt";
    private BufferedWriter logFile;
    public enum Type {INFO, WARNING , ERROR};

    private boolean sendToFile;

    private Logger(){
        sendToFile = true;
    }

    public  static Logger getInstance(){
        if(singleton == null){
            singleton = new Logger();
        }
        return singleton;
    }

    /**
     * Counts the amount of lines in the logger.
     * @return Amount of lines.
     */
    public int getFileSize() {
        int count = -1;

        try {
            BufferedReader br = new BufferedReader(new FileReader(LOGFILE));
            count = br.lines().reduce("", String::concat).length();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            return count;
        }

        return count;
    }

    public void setSendToFile(boolean value) {sendToFile = value;}

    public void setLoggerView(ListView<Label> listView) {
        LoggerJavaFX.initLog(listView);
    }

    /**
     * Opens the logger file.
     */
    private void openFile(){
        try{

            logFile  = new BufferedWriter( new FileWriter(LOGFILE, true));
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Clears all content inside the logger file.
     */
    public void clear() {

        try {
            (new PrintWriter(LOGFILE)).close(); // Limpa o conteúdo do ficheiro.
        } catch (IOException e) {
            e.printStackTrace();
        }

        LoggerJavaFX.clear();
    }

    /**
     * Finishes the writing of the log file.
     */
    public void submitLogFile() {
        try {
            logFile.flush();
            logFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows in the JavaFX interface the logged content.
     * @param message
     */
    public void localPrint(String message) {
        LoggerJavaFX.chooseInfoColor();
        LoggerJavaFX.log("> " + message);
    }

    /**
     * Adds a String to the log file and shows it on the JavaFX interface.
     * @param type Type of message: can be a INFO, WARNING or ERROR.
     * @param sender The identity of the class which sent the log.
     * @param message The String you want to log.
     */
    public void log(Type type, Object sender, String message){

        String t = type.toString();
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/YYYY HH:mm:ss"));
        String logged = String.format("[%s] - %s - (%s):\n  %s\n", t, now, sender.getClass().getSimpleName(), message);


        openFile();

        try{
            switch(type) {
                case INFO:
                    LoggerJavaFX.chooseInfoColor();
                    break;

                case WARNING:
                    LoggerJavaFX.chooseWarningColor();
                    break;

                case ERROR:
                default:
                    LoggerJavaFX.chooseErrorColor();
                    break;
            }

            logFile.append(logged);

            submitLogFile();

            LoggerJavaFX.log(logged);

        }catch(IOException e){
            System.out.println(e.getMessage());
        }

        localPrint(String.format("Log Size: %.2f KB", 0.001 * getFileSize()));

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
