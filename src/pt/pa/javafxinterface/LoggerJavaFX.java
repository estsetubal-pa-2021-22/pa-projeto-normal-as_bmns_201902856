package pt.pa.javafxinterface;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;

/**
 * The javaFX implementation of the Logger.
 */
public class LoggerJavaFX {
    private static ListView<Label> logArea;

    private static final int LOG_JAVAFX_LIMIT = 50;

    private static Color logColor;

    private static final Color INFO_COLOR = Color.DARKGREEN;
    private static final Color WARNING_COLOR = Color.ORANGERED;
    private static final Color ERROR_COLOR = Color.RED;

    public static void initLog(ListView<Label> logArea) {
        LoggerJavaFX.logArea = logArea;
        LoggerJavaFX.logColor = INFO_COLOR;
    }

    public static void chooseInfoColor() {
        LoggerJavaFX.logColor = INFO_COLOR;}
    public static void chooseWarningColor() {
        LoggerJavaFX.logColor = WARNING_COLOR;}
    public static void chooseErrorColor() {
        LoggerJavaFX.logColor = ERROR_COLOR;}

    /**
     * Clears all the visible logged information.
     */
    public static void clear() {
        logArea.getItems().clear();
        LoggerJavaFX.logColor = INFO_COLOR;
        LoggerJavaFX.log("> Cleared all logged information!");
    }

    /**
     * Logs the string and shows it in the application.
     * @param content The string you want to show in the interface.
     */
    public static void log(String content) {
        if (logArea == null) return;

        Label lbl = new Label(content);
        lbl.setTextFill(logColor);

        logArea.getItems().add(lbl);

        if (logArea.getItems().size() > LOG_JAVAFX_LIMIT) logArea.getItems().remove(0);
    }

}
