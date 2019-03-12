import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EffectsDemo extends Application {
    @Override
    public void start(Stage stage) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.GRAY);

        Text text = new Text();
        text.setY(50.0);
        text.setFill(Color.RED);
        text.setText("Drop shadow");
        text.setFont(Font.font("sans", FontWeight.BOLD, 40));
        text.setEffect(dropShadow);

        Text text2 = new Text();
        text2.setY(150.0);
        text2.setFill(Color.BLUE);
        text2.setText("Glow");
        text2.setFont(Font.font("sans", FontWeight.BOLD, 40));
        text2.setEffect(new Glow(0.8));

        Text text3 = new Text();
        text3.setY(250.0);
        text3.setFill(Color.GREEN);
        text3.setText("GaussianBlur");
        text3.setFont(Font.font("sans", FontWeight.BOLD, 40));
        text3.setEffect(new GaussianBlur());

        Group group = new Group(text, text2, text3);

        stage.setScene(new Scene(group));
        stage.show();
    }
}



