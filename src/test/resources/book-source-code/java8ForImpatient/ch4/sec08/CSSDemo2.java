import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class CSSDemo2 extends Application implements Initializable {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button okButton;

    public void initialize(URL url, ResourceBundle rb) {
        okButton.disableProperty().bind(
            Bindings.createBooleanBinding(
                () -> username.getText().length() == 0
                    || password.getText().length() == 0,
                username.textProperty(),
                password.textProperty()));
        okButton.setOnAction(event ->
            System.out.println("Verifying " + username.getText()
                + ":" + password.getText()));
    }

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("dialog.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
}

