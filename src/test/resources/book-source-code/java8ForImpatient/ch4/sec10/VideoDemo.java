import java.nio.file.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.stage.*;

 
public class VideoDemo extends Application {
   public void start(Stage stage) {
      Path path = Paths.get("moonlanding.mp4");
      String location = path.toUri().toString();
      Media media = new Media(location);
      MediaPlayer player = new MediaPlayer(media);
      player.setAutoPlay(true);

      MediaView view = new MediaView(player);
      view.setOnError(e -> System.out.println(e));
      HBox box = new HBox(view);
      box.setAlignment(Pos.CENTER);
      Scene scene = new Scene(box);
      stage.setScene(scene);
      stage.setWidth(500);
      stage.setHeight(500);
      stage.show();
   }
 
}
