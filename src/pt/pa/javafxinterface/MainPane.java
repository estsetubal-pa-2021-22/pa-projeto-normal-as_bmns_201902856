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

        centerBox.setAlignment(Pos.CENTER);

        this.setCenter(centerBox);


    }

    private void initMenu() {
        this.menuBar = new MenuBar();

        this.fileMenu = new Menu("File");
        MenuItem importFileItem = new MenuItem("Import File");
        MenuItem exportFileItem = new MenuItem("Export File");
        MenuItem quitItem = new MenuItem("Quit");
        quitItem.setOnAction(e -> System.exit(0));
        fileMenu.getItems().addAll(importFileItem, exportFileItem, quitItem);

        this.editMenu = new Menu("Edit");
        MenuItem addRouteItem = new MenuItem("Add Route");
        MenuItem removeRouteItem = new MenuItem("Remove Route");
        editMenu.getItems().addAll(addRouteItem, removeRouteItem);

        this.calculateMenu = new Menu("Calculate");
        MenuItem amountOfHubsItem = new MenuItem("Amount of Hubs");
        MenuItem amountOfRoutesItem = new MenuItem("Amount of Routes");
        MenuItem hubCentralityItem = new MenuItem("Hub Centrality");
        MenuItem top5HubsItem = new MenuItem("Top 5 Hubs");
        MenuItem subNetworksItem = new MenuItem("Number of Logistic Sub-Networks");
        calculateMenu.getItems().addAll(amountOfHubsItem, amountOfRoutesItem, hubCentralityItem, top5HubsItem, subNetworksItem);

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
