import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.stage.*;

public class BindingDemo extends Application {
   public void start(Stage stage) {
      TextArea shipping = new TextArea();
      TextArea billing = new TextArea();
      billing.textProperty().bindBidirectional(shipping.textProperty());
      VBox root = new VBox();
      root.getChildren().addAll(new Label("Shipping"), shipping, new Label("Billing"), billing);
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
   }
}
