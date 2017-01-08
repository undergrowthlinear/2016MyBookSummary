import javafx.application.*;
import javafx.beans.binding.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;

public class BindingDemo2 extends Application {
   public void start(Stage stage) {      
      Circle circle = new Circle(100, 100, 100);
      circle.setFill(Color.RED);
      Pane pane = new Pane();
      pane.getChildren().add(circle);
      Scene scene = new Scene(pane);
      circle.centerXProperty().bind(Bindings.divide(scene.widthProperty(), 2));
      circle.centerYProperty().bind(Bindings.divide(scene.heightProperty(), 2));
      stage.setScene(scene);
      stage.show();
   }
}
