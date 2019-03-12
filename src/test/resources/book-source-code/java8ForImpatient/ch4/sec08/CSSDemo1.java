import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class CSSDemo1 extends Application {
    public void start(Stage stage) {
        GridPane pane = new GridPane();
        pane.setId("pane");

        Label usernameLabel = new Label("User name:");
        Label passwordLabel = new Label("Password:");
        TextField username = new TextField();
        PasswordField password = new PasswordField();

        Button okButton = new Button("Ok");
        Button cancelButton = new Button("Cancel");

        HBox buttons = new HBox();
        buttons.getStyleClass().add("buttonrow");

        buttons.getChildren().addAll(okButton, cancelButton);
        // buttons.setStyle("-fx-border-color: red;");

        pane.add(usernameLabel, 0, 0);
        pane.add(username, 1, 0);
        pane.add(passwordLabel, 0, 1);
        pane.add(password, 1, 1);
        pane.add(buttons, 0, 2, 2, 1);

        GridPane.setHalignment(usernameLabel, HPos.RIGHT);
        GridPane.setHalignment(passwordLabel, HPos.RIGHT);

        Scene scene = new Scene(pane);
        scene.getStylesheets().add("scene.css");
        stage.setScene(scene);

        stage.show();
    }
}
