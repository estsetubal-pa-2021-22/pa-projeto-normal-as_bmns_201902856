package pt.pa.javafxinterface;

import javafx.scene.control.TextArea;

import java.time.LocalDateTime;

public class JavaFxAux {
    private static TextArea logArea;
    private static boolean timestampLog;


    //TODO: Converter isto num sistema com uma ListView de String (mostrar 50 ou 100 logs mais recentes apenas!!)
    public static void initLog(TextArea logArea, boolean withTimestamp) {
        JavaFxAux.logArea = logArea;
        JavaFxAux.timestampLog = withTimestamp;
    }

    public static void log(String content) {
        if (logArea == null) return;
        if (timestampLog) {
            logArea.setText("[" + LocalDateTime.now() + "]: " + " " + content);
        }
        else {
            logArea.setText(content);
        }
    }

    public static void appendLog(String content) {
        if (logArea == null) return;
        if (timestampLog) {
            appendLnLog(content);
        }
        else {
            logArea.setText(logArea.getText() + content);
        }

    }

    public static void appendLnLog(String content) {
        if (logArea == null) return;
        if (timestampLog) {
            logArea.setText(logArea.getText() + "[" + LocalDateTime.now() + "]: " + content + "\n");
        }
        else {
            logArea.setText(logArea.getText() + content + "\n");
        }
    }
}
