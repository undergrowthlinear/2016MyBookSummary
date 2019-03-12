import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
