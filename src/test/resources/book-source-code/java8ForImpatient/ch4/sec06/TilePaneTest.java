import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TilePaneTest extends Application {
    public void start(Stage stage) {
        final double em = Font.getDefault().getSize();

        TilePane pane = new TilePane();
        // pane.setGridLinesVisible(true);

        pane.setPrefColumns(1);
        for (int i = 0; i < 10; i++) {
            pane.getChildren().add(new Button("" + i * 3));
        }
        stage.setScene(new Scene(pane));
        stage.show();
    }
}
