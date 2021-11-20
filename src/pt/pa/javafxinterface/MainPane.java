package pt.pa.javafxinterface;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

public class MainPane extends BorderPane {
    private final int GRAPH_WIDTH = 1024 - 300;
    private final int GRAPH_HEIGHT = 768 - 150;

    private Label whereGraphWillBe;
    private MenuBar menuBar;
    private Menu fileMenu,
                 editMenu,
                 calculateMenu;

    private VBox logBox;
    private Label logTitle;
    private TextArea logArea;

    private HBox centerBox;

    public MainPane() {
        this.whereGraphWillBe = new Label("Cenas Fixes e\nGrafos e Cenas");
        whereGraphWillBe.setBackground(new Background(
                new BackgroundFill(Color.BURLYWOOD, CornerRadii.EMPTY, Insets.EMPTY)
        ));

        whereGraphWillBe.setMinSize(GRAPH_WIDTH, GRAPH_HEIGHT);
        whereGraphWillBe.setMaxSize(GRAPH_WIDTH, GRAPH_HEIGHT);

        this.centerBox = new HBox(5);

        this.centerBox.getChildren().add(whereGraphWillBe);

        initMenu();
        initLogBox();

        this.setCenter(centerBox);


    }

    private void initMenu() {
        this.menuBar = new MenuBar();

        this.fileMenu = new Menu("File");
        MenuItem quitItem = new MenuItem("Quit");
        quitItem.setOnAction(e -> System.exit(0));
        fileMenu.getItems().addAll(quitItem);

        this.editMenu = new Menu("Edit");


        this.calculateMenu = new Menu("Calculate");
        MenuItem testCalculate = new MenuItem("Test Calculate");
        testCalculate.setOnAction(e -> {
            for (int i = 0; i < 1000; i++) JavaFxAux.appendLog("" + i);
        });
        calculateMenu.getItems().addAll(testCalculate);

        menuBar.getMenus().addAll(
                fileMenu, editMenu, calculateMenu
        );

        this.setTop(menuBar);
    }

    private void initLogBox() {
        this.logTitle = new Label("Logs");
        logTitle.setAlignment(Pos.CENTER);
        logTitle.setTextAlignment(TextAlignment.CENTER);

        this.logArea = new TextArea();
        logArea.setPrefRowCount(36);
        logArea.setEditable(false);

        this.logBox = new VBox(5, logTitle, logArea);

        this.centerBox.getChildren().add(logBox);

        JavaFxAux.initLog(logArea, true);

    }
}
