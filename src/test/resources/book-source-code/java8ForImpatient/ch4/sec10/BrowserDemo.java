import java.nio.file.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.scene.*;
import javafx.scene.web.*;
import javafx.stage.*;

 
public class BrowserDemo extends Application {
   @Override public void start(Stage stage) {
      String location = "http://horstmann.com";
      WebView browser = new WebView();
      WebEngine engine = browser.getEngine();
      engine.load(location);
      Scene scene = new Scene(browser);
      stage.setScene(scene);
      stage.setWidth(500);
      stage.setHeight(500);
      stage.show();
   }
 
}
