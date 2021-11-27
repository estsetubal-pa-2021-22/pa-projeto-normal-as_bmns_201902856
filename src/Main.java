import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pt.pa.graph.GraphAdjacencyList;
import pt.pa.javafxinterface.MainPane;
import pt.pa.model.Hub;
import pt.pa.model.Matrix;
import pt.pa.model.MatrixHashMap;
import pt.pa.model.Route;
import pt.pa.filemanaging.FileManager;

import java.io.BufferedReader;
import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        String prefix = "dataset/sgb128/";
        GraphAdjacencyList<Hub, Route> testGraph = FileManager.graphFromFiles(
                prefix + "name.txt",
                prefix + "weight.txt",
                prefix + "xy.txt",
                prefix + "routes_1.txt"
        );
        System.out.println(testGraph);

        Rectangle2D rect = Screen.getPrimary().getBounds();
        //Rectangle2D rect = new Rectangle2D(900, 900, 900, 900);
        double scale = 0.9;

        MainPane mainPane = new MainPane();

        Scene scene = new Scene(mainPane, 1680, 900);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Projeto PA - Logistics Network");
        stage.setMinHeight(rect.getHeight() * scale);
        stage.setMinWidth(rect.getWidth() * scale);
        stage.setScene(scene);
        stage.show();

        mainPane.graphView.init();

    }


}
