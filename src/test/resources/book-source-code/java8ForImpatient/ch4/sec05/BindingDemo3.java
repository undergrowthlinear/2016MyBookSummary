import static javafx.beans.binding.Bindings.greaterThanOrEqual;
import static javafx.beans.binding.Bindings.lessThanOrEqual;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

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
