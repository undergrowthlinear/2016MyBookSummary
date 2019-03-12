import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class BorderPaneDemo extends Application {
    public void start(Stage stage) {
        BorderPane pane = new BorderPane();
        pane.setTop(new Button("Top"));
        pane.setLeft(new Button("Left"));
        pane.setCenter(new Button("Center"));
        pane.setRight(new Button("Right"));
        pane.setBottom(new Button("Bottom"));
        stage.setScene(new Scene(pane));
        stage.show();
    }
}
