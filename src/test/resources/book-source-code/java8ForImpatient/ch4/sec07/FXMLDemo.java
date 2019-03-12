import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXMLDemo extends Application {
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

