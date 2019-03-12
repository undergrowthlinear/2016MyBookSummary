import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

class LatentImage {
    private Image in;
    private List<UnaryOperator<Color>> pendingOperations;

    public static LatentImage from(Image in) {
        LatentImage result = new LatentImage();
        result.in = in;
        result.pendingOperations = new ArrayList<>();
        return result;
    }

    LatentImage transform(UnaryOperator<Color> f) {
        pendingOperations.add(f);
        return this;
    }

    public Image toImage() {
        int width = (int) in.getWidth();
        int height = (int) in.getHeight();
        WritableImage out = new WritableImage(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color c = in.getPixelReader().getColor(x, y);
                for (UnaryOperator<Color> f : pendingOperations) {
                    c = f.apply(c);
                }
                out.getPixelWriter().setColor(x, y, c);
            }
        }
        return out;
    }
}

public class ImageDemo extends Application {
    public void start(Stage stage) {
        Image image = new Image("eiffel-tower.jpg");
        Image finalImage = LatentImage.from(image)
            .transform(Color::brighter).transform(Color::grayscale)
            .toImage();
        stage.setScene(new Scene(new HBox(
            new ImageView(image),
            new ImageView(finalImage))));
        stage.show();
    }
}
