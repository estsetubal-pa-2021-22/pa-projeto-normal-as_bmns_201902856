package pt.pa.javafxinterface;

import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainer;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartRandomPlacementStrategy;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;
import pt.pa.Statistics;
import pt.pa.filemanaging.FileManager;
import pt.pa.graph.*;
import pt.pa.model.Hub;
import pt.pa.model.Route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainPane extends BorderPane {
    private final int GRAPH_WIDTH = 1024 - 300;
    private final int GRAPH_HEIGHT = 768 - 150;

    public SmartGraphPanel<Hub, Route> graphView;
    private SmartGraphDemoContainer whereGraphWillBe;
    private MenuBar menuBar;
    private Menu fileMenu,
                 editMenu,
                 calculateMenu;

    private VBox logBox;
    private Label logTitle;
    private TextArea logArea;

    private VBox centerBox;

    private Graph<Hub, Route> g;

    public MainPane() {
        initGraph();
        initMenu();
        initLogBox();
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

        Statistics stats = new Statistics(g);
        this.calculateMenu = new Menu("Calculate");
        MenuItem amountOfHubsItem = new MenuItem("Amount of Hubs");
        amountOfHubsItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Label amountOfHubsLabel = new Label("Amount of Hubs: " + stats.getAmountOfHubs());
                showInformation(amountOfHubsLabel);
            }
        });
        MenuItem amountOfRoutesItem = new MenuItem("Amount of Routes");
        amountOfRoutesItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Label amountOfRoutesLabel = new Label("Amount of Routes: " + stats.getAmountOfRoutes());
                showInformation(amountOfRoutesLabel);
            }
        });
        MenuItem hubCentralityItem = new MenuItem("Hub Centrality");
        hubCentralityItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ObservableList<Statistics.HubWithAdjacents> obsAllHubWithAdjacents = FXCollections.observableArrayList(stats.getHubCentrality());
                ListView<Statistics.HubWithAdjacents> listView = new ListView<>();
                listView.setItems(obsAllHubWithAdjacents);
                listView.setMaxWidth(380);
                listView.setMaxHeight(450);

                Label labelTitle = new Label("Hub Centrality");
                labelTitle.setStyle("-fx-font-size: 20pt");

                VBox vBox = new VBox();
                vBox.getChildren().addAll(labelTitle, listView);
                vBox.setAlignment(Pos.CENTER);

                Scene newScene = new Scene(vBox, 430, 500);
                Stage newWindow = new Stage();
                newWindow.setTitle("Statistics");
                newWindow.setScene(newScene);
                newWindow.show();
            }
        });
        MenuItem top5HubsItem = new MenuItem("Top 5 Hubs");
        top5HubsItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CategoryAxis yAxis = new CategoryAxis();
                NumberAxis xAxis = new NumberAxis();
                BarChart<Number, String> chart = new BarChart<>(xAxis, yAxis);
                chart.setTitle("Top 5 Hubs");
                xAxis.setLabel("Amount of adjacent hubs");
                xAxis.setTickLabelRotation(90);
                yAxis.setLabel("Hubs");

                XYChart.Series series = new XYChart.Series();
                series.setName("Top 5");
                List<Statistics.HubWithAdjacents> top5 = stats.getTop5Hubs();
                Collections.reverse(top5);
                for(Statistics.HubWithAdjacents hubWithAdjacents: top5) {
                    series.getData().add(new XYChart.Data<>(hubWithAdjacents.getAmountOfAdjacentHubs(), hubWithAdjacents.getHub().getName()));
                }
                chart.getData().add(series);

                VBox vBox = new VBox();
                vBox.getChildren().addAll(chart);
                vBox.setAlignment(Pos.CENTER);
                Scene newScene = new Scene(vBox, 600, 500);
                Stage newWindow = new Stage();
                newWindow.setTitle("Statistics");
                newWindow.setScene(newScene);
                newWindow.show();
            }
        });
        MenuItem subNetworksItem = new MenuItem("Number of Logistic Sub-Networks");
        subNetworksItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Label amountOfSubnetworksLabel = new Label("Amount of Logistical Subnetworks: " + stats.amountOfLogisticalSubnetworks());
                showInformation(amountOfSubnetworksLabel);
            }
        });
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
        this.logBox.setMaxWidth(300);

        this.setRight(logBox);
        BorderPane.setMargin(logBox, new Insets(0, 20, 0, 0));

        JavaFxAux.initLog(logArea, true);

    }

    private void initGraph() {
        String prefix = "dataset/sgb32/";
        g = FileManager.graphFromFiles(
                prefix + "name.txt",
                prefix + "weight.txt",
                prefix + "xy.txt",
                prefix + "routes_1.txt"
        );
        //Graph<String, String> g = build_flower_graph();
        System.out.println(g);

        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        //SmartPlacementStrategy strategy = new SmartRandomPlacementStrategy();
        graphView = new SmartGraphPanel<>(g);


        /*
        After creating, you can change the styling of some element.
        This can be done at any time afterwards.
        */
        if (g.numVertices() > 0) {
            //graphView.getStylableVertex("A").setStyle("-fx-fill: gold; -fx-stroke: brown;");
        }

        Label graphNameLbl = new Label("Visual Graph Implementation");

        whereGraphWillBe = new SmartGraphDemoContainer(graphView);
        whereGraphWillBe.setMinSize(GRAPH_WIDTH, GRAPH_HEIGHT);
        whereGraphWillBe.setMaxSize(GRAPH_WIDTH, GRAPH_HEIGHT);

        this.centerBox = new VBox(5);

        this.centerBox.getChildren().addAll(whereGraphWillBe, graphNameLbl);
        this.centerBox.setAlignment(Pos.CENTER);

        this.setCenter(centerBox);

        for(Vertex<Hub> v: g.vertices()) {
            graphView.setVertexPosition(v, v.element().getGuiX(), v.element().getGuiY());
        }
    }

    private Graph<String, String> build_sample_digraph() {

        Graph<String, String> g = new GraphAdjacencyList<>();
        //Digraph<String, String> g = new DigraphEdgeList<>();

        g.insertVertex("A");
        g.insertVertex("B");
        g.insertVertex("C");
        g.insertVertex("D");
        g.insertVertex("E");
        g.insertVertex("F");

        g.insertEdge("A", "B", "AB");
        g.insertEdge("B", "A", "AB2");
        g.insertEdge("A", "C", "AC");
        g.insertEdge("A", "D", "AD");
        g.insertEdge("B", "C", "BC");
        g.insertEdge("C", "D", "CD");
        g.insertEdge("B", "E", "BE");
        g.insertEdge("F", "D", "DF");
        g.insertEdge("F", "D", "DF2");

        //yep, its a loop!
        g.insertEdge("A", "A", "Loop");

        return g;
    }

    public void showInformation(Label label) {
        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(label);

        Scene newScene = new Scene(secondaryLayout, 330, 100);

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Statistics");
        newWindow.setScene(newScene);
        newWindow.show();
    }
}
