import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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
