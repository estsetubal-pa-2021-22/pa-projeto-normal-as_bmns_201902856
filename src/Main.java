import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pt.pa.javafxinterface.MainPane;
import pt.pa.model.Hub;
import pt.pa.model.Route;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Hub hub1 = new Hub("Generic City A", 100000, 123, 235);
        Hub hub2 = new Hub("Generic City B", 123456, 234, 346);

        Route route1 = new Route(hub1, hub2);

        System.out.println(route1);

        Rectangle2D rect = Screen.getPrimary().getBounds();
        double scale = 0.9;

        Scene scene = new Scene(new MainPane(), rect.getWidth() * scale, rect.getHeight() * scale);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Projeto PA - Logistics Network");
        stage.setMinHeight(rect.getHeight() * scale);
        stage.setMinWidth(rect.getWidth() * scale);
        stage.setScene(scene);
        stage.show();


    }


}
