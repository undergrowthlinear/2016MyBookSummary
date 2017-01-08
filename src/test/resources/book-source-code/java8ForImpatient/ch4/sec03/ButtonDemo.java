import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.stage.*;

public class ButtonDemo extends Application {
   public void start(Stage stage) {
      Label message = new Label("Hello, JavaFX!");
      message.setFont(new Font(100));
      Button red = new Button("Red");
      red.setOnAction(event -> message.setTextFill(Color.RED));
      VBox root = new VBox();
      root.getChildren().addAll(red, message);
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
   }
}
