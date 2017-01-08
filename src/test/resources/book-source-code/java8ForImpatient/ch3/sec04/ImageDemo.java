import java.util.function.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.*;

public class ImageDemo extends Application {
   public static Image transform(Image in, UnaryOperator<Color> f) {
      int width = (int) in.getWidth();
      int height = (int) in.getHeight();
      WritableImage out = new WritableImage(
         width, height);
      for (int x = 0; x < width; x++)
         for (int y = 0; y < height; y++)
            out.getPixelWriter().setColor(x, y, 
               f.apply(in.getPixelReader().getColor(x, y)));
      return out;
   }

   public static UnaryOperator<Color> brighten(double factor) {
      return c -> c.deriveColor(0, 1, factor, 1); 
   }

   public void start(Stage stage) {
      Image image = new Image("queen-mary.png");
      Image brightenedImage = transform(image, brighten(1.2));
      stage.setScene(new Scene(new HBox(new ImageView(image), new ImageView(brightenedImage))));
      stage.show();
   }
}
