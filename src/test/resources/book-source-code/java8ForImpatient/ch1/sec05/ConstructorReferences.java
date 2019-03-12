import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ConstructorReferences extends Application {
    public void start(Stage stage) {
        List<String> labels = Arrays.asList("Ok", "Cancel", "Yes", "No", "Maybe");
        Stream<Button> stream = labels.stream().map(Button::new);
        List<Button> buttons = stream.collect(Collectors.toList());

        System.out.println(buttons);

        stream = labels.stream().map(Button::new);
        Object[] buttons2 = stream.toArray();
        System.out.println(buttons2.getClass());

        // The following generates a ClassCastException
        // stream = labels.stream().map(Button::new);
        // Button[] buttons3 = (Button[]) stream.toArray();

        stream = labels.stream().map(Button::new);
        Button[] buttons4 = stream.toArray(Button[]::new);

        final double rem = Font.getDefault().getSize();
        HBox box = new HBox(0.8 * rem);
        box.getChildren().addAll(buttons4);
        stage.setScene(new Scene(box));
        stage.show();
    }
}
