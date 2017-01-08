import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;

public class TilePaneTest extends Application {
   public void start(Stage stage) {
      final double em = Font.getDefault().getSize();

      TilePane pane = new TilePane();
      // pane.setGridLinesVisible(true);

      pane.setPrefColumns(1);
      for (int i = 0; i < 10; i++) pane.getChildren().add(new Button("" + i * 3));
      stage.setScene(new Scene(pane));
      stage.show();
   }
}
