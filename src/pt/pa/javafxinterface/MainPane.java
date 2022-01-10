package pt.pa.javafxinterface;

import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainer;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;
import pt.pa.Logger;
import pt.pa.Statistics;
import pt.pa.commands.Command;
import pt.pa.commands.CommandAdd;
import pt.pa.commands.CommandHistory;
import pt.pa.commands.CommandRemove;
import pt.pa.dijkstra.HubRouteDijkstra;
import pt.pa.filemanaging.FileManager;
import pt.pa.graph.*;
import pt.pa.model.Hub;
import pt.pa.model.Route;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class MainPane extends BorderPane {
    private final int GRAPH_WIDTH = 1024 - 300;
    private final int GRAPH_HEIGHT = 768 - 150;

    private final int LOG_HEIGHT = GRAPH_HEIGHT;
    private final int LOG_WIDTH = (int)(0.5 * GRAPH_WIDTH);

    private CommandHistory history = new CommandHistory();
    private Stage stage = new Stage(StageStyle.DECORATED);  //For add and Remove
    private ChoiceBox nameHub1;//For add and Remove
    private ChoiceBox nameHub2;//For add and Remove
    public SmartGraphPanel<Hub, Route> graphView;
    private SmartGraphDemoContainer whereGraphWillBe;
    private MenuBar menuBar;
    private Menu fileMenu,
                 editMenu,
                 calculateMenu,
                 showMenu;

    private VBox logBox;
    private Label logTitle;
    private ListView<Label> logArea;

    private VBox centerBox;

    public Graph<Hub, Route> g;

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
        MenuItem clearLogItem = new MenuItem("Clear Log File");
        MenuItem quitItem = new MenuItem("Quit");

        clearLogItem.setOnAction(e -> Logger.getInstance().clear());
        quitItem.setOnAction(e -> System.exit(0));
        fileMenu.getItems().addAll(importFileItem, exportFileItem, clearLogItem, quitItem);

        this.editMenu = new Menu("Edit");
        MenuItem addRouteItem = new MenuItem("Add Route");
        /*addRouteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });*/
        addRouteItem.setOnAction(new EventHandler<ActionEvent>() {

            /**
             * Invoked when a specific event to add routes
             *
             * @param event the event which occurred
             */
            @Override
            public void handle(ActionEvent event) {
                //open new window
                initWindowAdd();
            }
        });
        MenuItem removeRouteItem = new MenuItem("Remove Route");
        removeRouteItem.setOnAction(new EventHandler<ActionEvent>() {

            /**
             * Invoked when a specific event to remove routes
             *
             * @param event the event which occurred
             */
            @Override
            public void handle(ActionEvent event) {
                //open new window
                initWindowRemove();
            }
        });
        MenuItem undoItem = new MenuItem("Undo");
        undoItem.setOnAction(new EventHandler<ActionEvent>() {

            /**
             * Event to undo changes to the routes
             *
             * @param event the event which occurred
             */
            @Override
            public void handle(ActionEvent event) {
                //open new window
                undo();
            }
        });
        editMenu.getItems().addAll(addRouteItem, removeRouteItem, undoItem);

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

        this.showMenu = new Menu("Show");
        HubRouteDijkstra hubRouteDijkstra = HubRouteDijkstra.getInstance();
        hubRouteDijkstra.setGraph((GraphAdjacencyList<Hub, Route>) g);
        MenuItem shortestPathItem = new MenuItem("Shortest path between 2 hubs");
        shortestPathItem.setOnAction((event -> {
            VBox doubleChoiceVBox = createVBoxWithDoubleChoiceBox();
            Button findShortestBtn = new Button("Find shortest path: ");
            StackPane root=new StackPane();
            root.getChildren().addAll(doubleChoiceVBox,findShortestBtn);
            findShortestBtn.setAlignment(Pos.BOTTOM_CENTER);
            Scene scene = new Scene(root, 400, 200);
            stage.setScene(scene);
            stage.show();
            findShortestBtn.setOnAction((event1 -> {
                if (nameHub1.getValue() == null || nameHub2.getValue() == null) {
                    Logger.getInstance().log(Logger.Type.ERROR, this, "Please put a value in the choice boxes!");
                    return;
                }
                Vertex<Hub> v1 = null;
                Vertex<Hub> v2 = null;
                String hub1 = ((String) nameHub1.getValue()).toLowerCase();
                String hub2 = ((String) nameHub2.getValue()).toLowerCase();
                for(Vertex<Hub> v: g.vertices()) {
                    if(v.element().getName().toLowerCase().equals(hub1)) {
                        v1 = v;
                    }
                    if(v.element().getName().toLowerCase().equals(hub2)) {
                        v2 = v;
                    }
                }
                if(!g.depthFirstSearch(v1).contains(v2)) {
                    Logger.getInstance().log(Logger.Type.ERROR, this, "The two hubs aren't connected!");
                    return;
                }
                stage.close();
                List<Vertex<Hub>> shortestPathVertices = hubRouteDijkstra.shortestPath(v1, v2);
                for(Vertex<Hub> v: shortestPathVertices) {
                    graphView.getStylableVertex(v).setStyleClass("selectedVertex");
                }
                for(Edge<Route, Hub> e: hubRouteDijkstra.edgesUpTo(shortestPathVertices)) {
                    graphView.getStylableEdge(e).setStyleClass("selectedEdge");
                }

            }));
        }));
        MenuItem furthestHubsItem = new MenuItem("Hubs furthest from each other");
        furthestHubsItem.setOnAction(event -> {
            Pair<Vertex<Hub>, Vertex<Hub>> hubPair = hubRouteDijkstra.farthestHubPair();
            List<Vertex<Hub>> shortestPathVertices = hubRouteDijkstra.shortestPath(hubPair.getKey(), hubPair.getValue());
            for(Vertex<Hub> v: shortestPathVertices) {
                graphView.getStylableVertex(v).setStyleClass("selectedVertex");
            }
            for(Edge<Route, Hub> e: hubRouteDijkstra.edgesUpTo(shortestPathVertices)) {
                graphView.getStylableEdge(e).setStyleClass("selectedEdge");
            }
        });
        MenuItem hubsDistFromHubItem = new MenuItem("All hubs that are N routes from hub H");
        hubsDistFromHubItem.setOnAction(event -> {
            VBox vbox = new VBox();
            Label hubLabel = new Label("Hubs: ");
            ChoiceBox rootChoiceBox = new ChoiceBox();
            HBox hbox1 = new HBox();
            hbox1.getChildren().addAll(hubLabel, rootChoiceBox);
            for (Vertex<Hub> v: g.vertices()) {
                rootChoiceBox.getItems().add(v.element().getName());
            }
            Label distanceLbl = new Label("Distance: ");
            Spinner<Integer> intSpinner = new Spinner<>(0, 1000, 0, 1);
            HBox hbox2 = new HBox();
            hbox2.getChildren().addAll(distanceLbl, intSpinner);
            Button findHubsButton = new Button("Find hubs based on distance");
            findHubsButton.setAlignment(Pos.BOTTOM_CENTER);
            vbox.getChildren().addAll(hbox1, hbox2, findHubsButton);
            StackPane root=new StackPane();
            root.getChildren().addAll(vbox, findHubsButton);
            Scene scene = new Scene(root, 400, 200);
            stage.setScene(scene);
            stage.show();

            findHubsButton.setOnAction(event1 -> {
                if (rootChoiceBox.getValue() == null) {
                    Logger.getInstance().log(Logger.Type.ERROR, this, "Please put a value in the choice box!");
                }
                Vertex<Hub> rootVertex = null;
                for (Vertex<Hub> v: g.vertices()) {
                    if (v.element().getName().equalsIgnoreCase(((String) rootChoiceBox.getValue()))) {
                        rootVertex = v;
                    }
                }
                //System.out.println("Root Vertex: " + rootVertex);
                HubRouteGraphSearch graphSearch = new HubRouteGraphSearch((GraphAdjacencyList<Hub, Route>) g);
                List<Vertex<Hub>> verticesInRange = graphSearch.bfsLimited(rootVertex, intSpinner.getValue() + 1);
                System.out.println(verticesInRange == null);
                verticesInRange.stream().map(value -> value.element().getName()).forEach(System.out::println);
                for(Vertex<Hub> v: verticesInRange) {
                    graphView.getStylableVertex(v).setStyleClass("selectedVertex");
                    List<Vertex<Hub>> shortestPath = hubRouteDijkstra.shortestPath(rootVertex, v);
                    //shortestPath.forEach(e -> System.out.println("Path: " + e));

                    for(Edge<Route, Hub> e: hubRouteDijkstra.edgesUpTo(shortestPath)) {
                        graphView.getStylableEdge(e).setStyleClass("selectedEdge");
                    }
                }

                graphView.getStylableVertex(rootVertex).setStyle("-fx-stroke: blue;");
            });

        });
        MenuItem resetOutlinesItem = new MenuItem("Reset outlines");
        resetOutlinesItem.setOnAction((event -> {
            resetStyling();
        }));
        showMenu.getItems().addAll(shortestPathItem, furthestHubsItem, hubsDistFromHubItem, resetOutlinesItem);

        menuBar.getMenus().addAll(
                fileMenu, editMenu, calculateMenu, showMenu
        );

        this.setTop(menuBar);
    }

    private void initLogBox() {
        this.logTitle = new Label("Logs");
        logTitle.setAlignment(Pos.CENTER);
        logTitle.setTextAlignment(TextAlignment.CENTER);

        this.logArea = new ListView<>();
        logArea.setMinHeight(LOG_HEIGHT);
        logArea.setMaxHeight(LOG_HEIGHT);
        logArea.setMinWidth(LOG_WIDTH);
        logArea.setMinWidth(LOG_WIDTH);
        logArea.setEditable(false);

        this.logBox = new VBox(5, logTitle, logArea);
        this.logBox.setMaxWidth(LOG_WIDTH);

        this.setRight(logBox);
        BorderPane.setMargin(logBox, new Insets(0, 20, 0, 0));

        Logger.getInstance().setLoggerView(logArea);
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
        //System.out.println(g);

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

    private void executeCommand(Command command) {
        if (command.execute()) {
            history.push(command);
        }
    }

    private void undo() {
        if (history.isEmpty()) return;

        Command command = history.pop();
        if (command != null) {
            command.undo();
        }
    }

    private void initWindowAdd(){
        MainPane pane = this;
        VBox hub = createVBoxWithDoubleChoiceBox();
        Button addBtn = new Button("Add");
        StackPane root=new StackPane();
        root.getChildren().addAll(hub,addBtn);
        addBtn.setAlignment(Pos.BOTTOM_CENTER);
        Scene scene = new Scene(root, 300, 150);
        stage.setScene(scene);
        stage.show();
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //execute add Command
                if (nameHub1.getValue() == null || nameHub2.getValue() == null) {
                    Logger.getInstance().log(Logger.Type.ERROR, this, "Please put a value in the choice boxes!");
                    return;
                }
                executeCommand(new CommandAdd(pane));
                graphView.updateAndWait();
                stage.close();
            }
        });
    }

    private void initWindowRemove(){
        MainPane pane = this;
        VBox hub = createVBoxWithDoubleChoiceBox();
        Button remBtn = new Button("Remove");
        StackPane root=new StackPane();
        root.getChildren().addAll(hub,remBtn);
        remBtn.setAlignment(Pos.BOTTOM_CENTER);
        Scene scene = new Scene(root, 300, 150);
        stage.setScene(scene);
        stage.show();
        remBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //execute remove Command
                if (nameHub1.getValue() == null || nameHub2.getValue() == null) {
                    Logger.getInstance().log(Logger.Type.ERROR, this, "Please put a value in the choice boxes!");
                    return;
                }
                executeCommand(new CommandRemove(pane));
                graphView.updateAndWait();
                stage.close();
            }
        });
    }

    private VBox createVBoxWithDoubleChoiceBox() {
        Label Hub1 = new Label("Nome Hub1:");
        Hub1.setFont(new Font(15));
        nameHub1 = new ChoiceBox();
        nameHub2 = new ChoiceBox();
        for (Vertex<Hub> v: g.vertices()) {
            nameHub1.getItems().add(v.element().getName());
            nameHub2.getItems().add(v.element().getName());
        }
        HBox hub1 = new HBox();
        hub1.getChildren().addAll(Hub1, nameHub1);
        Hub1.setAlignment(Pos.CENTER_LEFT);
        Label Hub2 = new Label("Nome Hub2:");
        Hub2.setFont(new Font(15));
        HBox hub2 = new HBox();
        hub2.getChildren().addAll(Hub2, nameHub2);
        Hub2.setAlignment(Pos.CENTER_LEFT);
        VBox hub = new VBox();
        hub.getChildren().addAll(hub1,hub2);

        return hub;
    }

    private void resetStyling() {
        for(Vertex<Hub> v: g.vertices()) {
            graphView.getStylableVertex(v).setStyleClass("vertex");
        }
        for(Edge<Route, Hub> e: g.edges()) {
            graphView.getStylableEdge(e).setStyleClass("edge");
        }
    }

    public ChoiceBox getNameHub1() {
        return nameHub1;
    }
    public ChoiceBox getNameHub2() {
        return nameHub2;
    }
}

