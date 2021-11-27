package pt.pa.javafxinterface;

import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainer;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartRandomPlacementStrategy;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import pt.pa.filemanaging.FileManager;
import pt.pa.graph.*;
import pt.pa.model.Hub;
import pt.pa.model.Route;

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

    public MainPane() {




        initMenu();
        initLogBox();
        initGraph();



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
        this.logBox.setMaxWidth(300);

        this.setRight(logBox);
        BorderPane.setMargin(logBox, new Insets(0, 20, 0, 0));

        JavaFxAux.initLog(logArea, true);

    }

    private void initGraph() {
        String prefix = "dataset/sgb32/";
        Graph<Hub, Route> g = FileManager.graphFromFiles(
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
        for(Vertex<Hub> v: g.vertices()) {
            graphView.setVertexPosition(v, v.element().getGuiX(), v.element().getGuiY());
        }

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
}
