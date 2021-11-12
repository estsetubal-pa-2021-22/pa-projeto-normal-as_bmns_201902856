import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Rectangle2D rect = Screen.getPrimary().getBounds();
        double scale = 0.9;

        Scene scene = new Scene(new BorderPane(), rect.getWidth() * scale, rect.getHeight() * scale);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Projeto PA - Logistics Network");
        stage.setMinHeight(rect.getHeight() * scale);
        stage.setMinWidth(rect.getWidth() * scale);
        stage.setScene(scene);
        stage.show();


    }


}
