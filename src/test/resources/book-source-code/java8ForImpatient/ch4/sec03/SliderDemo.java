import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SliderDemo extends Application {
    public void start(Stage stage) {
        Label message = new Label("Hello, JavaFX!");
        message.setFont(new Font(100));
        Slider slider = new Slider();
        slider.setValue(100);
        slider.valueProperty().addListener(property
            -> message.setFont(new Font(slider.getValue())));
        VBox root = new VBox();
        root.getChildren().addAll(slider, message);
        Scene scene = new Scene(root);
        stage.setTitle("Hello");
        stage.setScene(scene);
        stage.show();
    }
}
