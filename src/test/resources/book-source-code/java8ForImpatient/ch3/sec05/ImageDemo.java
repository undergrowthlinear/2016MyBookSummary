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

   public static <T> UnaryOperator<T> compose(UnaryOperator<T> op1,
      UnaryOperator<T> op2) {
      return t -> op2.apply(op1.apply(t));
   }

   public void start(Stage stage) {
      Image image = new Image("eiffel-tower.jpg");
      // Image image2 = transform(image, Color::brighter);
      // Image image3 = transform(image2, Color::grayscale);
      Image image3 = transform(image, compose(Color::brighter, Color::grayscale));
      stage.setScene(new Scene(new HBox(
         new ImageView(image),
         // new ImageView(image2),
         new ImageView(image3))));
      stage.show();
   }
}
