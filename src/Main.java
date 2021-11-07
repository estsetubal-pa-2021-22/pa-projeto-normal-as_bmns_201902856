import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Scene scene = new Scene(new BorderPane(), 1200, 768);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Projeto PA - Logistics Network");
        stage.setMinHeight(768);
        stage.setMinWidth(1200);
        stage.setScene(scene);
        stage.show();


    }


}
