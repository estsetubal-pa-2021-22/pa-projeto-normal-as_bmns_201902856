package pt.pa.javafxinterface;

import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainer;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartRandomPlacementStrategy;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pt.pa.commands.Command;
import pt.pa.commands.CommandAdd;
import pt.pa.commands.CommandHistory;
import pt.pa.commands.CommandRemove;
import pt.pa.filemanaging.FileManager;
import pt.pa.graph.*;
import pt.pa.model.Hub;
import pt.pa.model.Route;

import java.awt.event.ActionListener;

public class MainPane extends BorderPane{
    private CommandHistory history = new CommandHistory();
    private final int GRAPH_WIDTH = 1024 - 300;
    private final int GRAPH_HEIGHT = 768 - 150;
    public Graph<Hub, Route> g; //For Command
    private Stage stage = new Stage(StageStyle.DECORATED);  //For add and Remove
    public TextField nameHub1;//For add and Remove
    public TextField nameHub2;//For add and Remove
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
        addRouteItem.setOnAction(new EventHandler() {

            /**
             * Invoked when a specific event to add routes
             *
             * @param event the event which occurred
             */
            @Override
            public void handle(Event event) {
                //open new window
                initWindowAdd();
            }
        });
        MenuItem removeRouteItem = new MenuItem("Remove Route");
        removeRouteItem.setOnAction(new EventHandler() {

            /**
             * Invoked when a specific event to remove routes
             *
             * @param event the event which occurred
             */
            @Override
            public void handle(Event event) {
                //open new window
                initWindowRemove();
            }
        });
        MenuItem undoItem = new MenuItem("Undo");
        undoItem.setOnAction(new EventHandler() {

            /**
             * Event to undo changes to the routes
             *
             * @param event the event which occurred
             */
            @Override
            public void handle(Event event) {
                //open new window
                undo();
            }
        });
        editMenu.getItems().addAll(addRouteItem, removeRouteItem, undoItem);

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
        g = FileManager.graphFromFiles( //Change here test purposes
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
        Label Hub1 = new Label("Nome Hub1:");
        Hub1.setFont(new Font(15));
        nameHub1 = new TextField("nome hub");
        HBox hub1 = new HBox();
        hub1.getChildren().addAll(Hub1, nameHub1);
        Hub1.setAlignment(Pos.CENTER_LEFT);
        Label Hub2 = new Label("Nome Hub2:");
        Hub2.setFont(new Font(15));
        nameHub2 = new TextField("nome hub");
        HBox hub2 = new HBox();
        hub2.getChildren().addAll(Hub2, nameHub2);
        Hub2.setAlignment(Pos.CENTER_LEFT);
        Button addBtn = new Button("Add");
        StackPane root=new StackPane();
        VBox hub = new VBox();
        hub.getChildren().addAll(hub1,hub2);
        root.getChildren().addAll(hub,addBtn);
        addBtn.setAlignment(Pos.BOTTOM_CENTER);
        Scene scene = new Scene(root, 300, 150);
        stage.setScene(scene);
        stage.show();
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //execute add Command
                executeCommand(new CommandAdd(pane));
                graphView.updateAndWait();
                stage.close();
            }
        });
    }

    private void initWindowRemove(){
        MainPane pane = this;
        Label Hub1 = new Label("Nome Hub1:");
        Hub1.setFont(new Font(15));
        nameHub1 = new TextField("nome hub");
        HBox hub1 = new HBox();
        hub1.getChildren().addAll(Hub1, nameHub1);
        Hub1.setAlignment(Pos.CENTER_LEFT);
        Label Hub2 = new Label("Nome Hub2:");
        Hub2.setFont(new Font(15));
        nameHub2 = new TextField("nome hub");
        HBox hub2 = new HBox();
        hub2.getChildren().addAll(Hub2, nameHub2);
        Hub2.setAlignment(Pos.CENTER_LEFT);
        Button remBtn = new Button("Remove");
        StackPane root=new StackPane();
        VBox hub = new VBox();
        hub.getChildren().addAll(hub1,hub2);
        root.getChildren().addAll(hub,remBtn);
        remBtn.setAlignment(Pos.BOTTOM_CENTER);
        Scene scene = new Scene(root, 300, 150);
        stage.setScene(scene);
        stage.show();
        remBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //execute remove Command
                executeCommand(new CommandRemove(pane));
                graphView.updateAndWait();
                stage.close();
            }
        });
    }
}
