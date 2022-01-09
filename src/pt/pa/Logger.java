package pt.pa;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import pt.pa.javafxinterface.JavaFxAux;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.stream.Collectors;

public class Logger {
    private static Logger singleton;
    private static final String LOGFILE = "log.txt";
    private BufferedWriter logFile;
    protected enum Type {INFO, WARNING , ERROR};

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
        JavaFxAux.initLog(listView);
    }

    private void openFile(){
        try{

            logFile  = new BufferedWriter( new FileWriter(LOGFILE, true));
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void clear() {

        try {
            (new PrintWriter(LOGFILE)).close(); // Limpa o conteúdo do ficheiro.
        } catch (IOException e) {
            e.printStackTrace();
        }

        JavaFxAux.clear();
    }

    public void submitLogFile() {
        try {
            logFile.flush();
            logFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void localPrint(String message) {
        JavaFxAux.chooseInfoColor();
        JavaFxAux.log("> " + message);
    }

    public void log(Type type, Object sender, String message){

        String t = type.toString();
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/YYYY HH:mm:ss"));
        String logged = String.format("[%s] - %s - (%s):\n  %s\n", t, now, sender.getClass().getSimpleName(), message);


        openFile();

        try{
            switch(type) {
                case INFO:
                    JavaFxAux.chooseInfoColor();
                    break;

                case WARNING:
                    JavaFxAux.chooseWarningColor();
                    break;

                case ERROR:
                default:
                    JavaFxAux.chooseErrorColor();
                    break;
            }

            logFile.append(logged);

            submitLogFile();

            JavaFxAux.log(logged);

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
