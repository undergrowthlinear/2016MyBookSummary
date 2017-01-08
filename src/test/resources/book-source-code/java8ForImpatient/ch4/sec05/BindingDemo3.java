import javafx.application.*;
import javafx.beans.binding.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;

import static javafx.beans.binding.Bindings.*;

public class BindingDemo3 extends Application {
   public void start(Stage stage) {      
      Button smaller = new Button("Smaller");
      Button larger = new Button("Larger");
      Rectangle gauge = new Rectangle(0, 5, 50, 15);
      Rectangle outline = new Rectangle(0, 5, 100, 15);
      outline.setFill(null);
      outline.setStroke(Color.BLACK);
      Pane pane = new Pane();
      pane.getChildren().addAll(gauge, outline);     
      smaller.setOnAction(event -> gauge.setWidth(gauge.getWidth() - 10));
      larger.setOnAction(event -> gauge.setWidth(gauge.getWidth() + 10));
      smaller.disableProperty().bind(lessThanOrEqual(gauge.widthProperty(), 0));
      larger.disableProperty().bind(greaterThanOrEqual(gauge.widthProperty(), 100));

      HBox box = new HBox(10);
      box.getChildren().addAll(smaller, pane, larger);
      Scene scene = new Scene(box);
      stage.setScene(scene);
      stage.show();
   }
}
