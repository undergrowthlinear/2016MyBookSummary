import java.util.function.UnaryOperator;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

@FunctionalInterface
interface ColorTransformer {
    Color apply(int x, int y, Color colorAtXY);
}

public class ImageDemo extends Application {
    public static Image transform(Image in, UnaryOperator<Color> f) {
        int width = (int) in.getWidth();
        int height = (int) in.getHeight();
        WritableImage out = new WritableImage(
            width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                out.getPixelWriter().setColor(x, y,
                    f.apply(in.getPixelReader().getColor(x, y)));
            }
        }
        return out;
    }

    public static Image transform(Image in, ColorTransformer f) {
        int width = (int) in.getWidth();
        int height = (int) in.getHeight();
        WritableImage out = new WritableImage(
            width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                out.getPixelWriter().setColor(x, y,
                    f.apply(x, y, in.getPixelReader().getColor(x, y)));
            }
        }
        return out;
    }

    public void start(Stage stage) {
        Image image = new Image("queen-mary.png");
        Image brightenedImage = transform(image, Color::brighter);
        Image image2 = transform(image,
            (x, y, c) -> (x / 10) % 2 == (y / 10) % 2 ? Color.GRAY : c);

        stage.setScene(new Scene(new HBox(new ImageView(image), new ImageView(brightenedImage), new ImageView(image2))));
        stage.show();
    }
}
