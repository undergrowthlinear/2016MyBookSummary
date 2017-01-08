import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;

public class HBoxDemo extends Application {
   public void start(Stage stage) {
      Button yesButton = new Button("Yes");
      Button noButton = new Button("No");
      Button maybeButton = new Button("Maybe");

      final double rem = Font.getDefault().getSize();

      HBox buttons = new HBox(0.8 * rem); 
      buttons.getChildren().addAll(yesButton, noButton, maybeButton);
 
      VBox pane = new VBox(0.8 * rem);
      Label question = new Label("Will you attend?");
      pane.setPadding(new Insets(0.8 * rem));
      pane.getChildren().addAll(question, buttons);
      stage.setScene(new Scene(pane));
      stage.show();
   }
}
