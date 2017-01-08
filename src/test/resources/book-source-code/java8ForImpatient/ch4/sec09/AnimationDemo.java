import javafx.application.*;
import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.util.*;

public class AnimationDemo extends Application {
   @Override
   public void start(Stage stage) {
      
      Button yesButton = new Button("Yes");
      Button noButton = new Button("No");
      Button maybeButton = new Button("Maybe");

      ScaleTransition st = new ScaleTransition(Duration.millis(3000));
      st.setByX(1.5);
      st.setByY(1.5);
      st.setCycleCount(Animation.INDEFINITE);
      st.setAutoReverse(true);
      st.setNode(yesButton);
      st.play();
 
      FadeTransition ft = new FadeTransition(Duration.millis(3000));
      ft.setFromValue(1.0);
      ft.setToValue(0);
      ft.setCycleCount(Animation.INDEFINITE);
      ft.setAutoReverse(true);
      ft.setNode(noButton);
      ft.play();

      RotateTransition rt = new RotateTransition(Duration.millis(3000));
      rt.setByAngle(180);
      rt.setCycleCount(Animation.INDEFINITE);
      rt.setAutoReverse(true);
      rt.setNode(maybeButton);
      rt.play();
 
      HBox buttons = new HBox(10);
      buttons.getChildren().addAll(yesButton, noButton, maybeButton);
      VBox pane = new VBox(10);
      Label question = new Label("Will you attend?");
      pane.setPadding(new Insets(10));
      pane.getChildren().addAll(question, buttons);

      stage.setScene(new Scene(pane));
      stage.show();
   }
}



