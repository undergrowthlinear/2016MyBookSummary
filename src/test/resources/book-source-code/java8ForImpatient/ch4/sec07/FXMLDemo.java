import java.io.*;
import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

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

