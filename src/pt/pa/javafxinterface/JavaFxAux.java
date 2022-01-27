package pt.pa.javafxinterface;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;

import java.time.LocalDateTime;

public class JavaFxAux {
    private static ListView<Label> logArea;

    private static final int LOG_JAVAFX_LIMIT = 50;

    private static Color logColor;

    private static final Color INFO_COLOR = Color.DARKGREEN;
    private static final Color WARNING_COLOR = Color.ORANGERED;
    private static final Color ERROR_COLOR = Color.RED;

    public static void initLog(ListView<Label> logArea) {
        JavaFxAux.logArea = logArea;
        JavaFxAux.logColor = INFO_COLOR;
    }

    public static void chooseInfoColor() {JavaFxAux.logColor = INFO_COLOR;}
    public static void chooseWarningColor() {JavaFxAux.logColor = WARNING_COLOR;}
    public static void chooseErrorColor() {JavaFxAux.logColor = ERROR_COLOR;}

    public static void clear() {
        logArea.getItems().clear();
        JavaFxAux.logColor = INFO_COLOR;
        JavaFxAux.log("> Cleared all logged information!");
    }

    public static void log(String content) {
        if (logArea == null) return;

        Label lbl = new Label(content);
        lbl.setTextFill(logColor);

        logArea.getItems().add(lbl);

        if (logArea.getItems().size() > LOG_JAVAFX_LIMIT) logArea.getItems().remove(0);
    }

}
