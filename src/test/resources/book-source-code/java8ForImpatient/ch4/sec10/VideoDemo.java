import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;


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
