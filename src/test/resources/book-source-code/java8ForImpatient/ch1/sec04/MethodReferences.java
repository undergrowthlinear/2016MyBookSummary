import java.util.*;
import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;

public class MethodReferences extends Application {
   public void start(Stage stage) {
      String[] strings = "Mary had a little lamb".split(" ");
      Arrays.sort(strings, String::compareToIgnoreCase);
      System.out.println(Arrays.toString(strings));

      Button button = new Button("Click me!");
      button.setOnAction(System.out::println);
      stage.setScene(new Scene(button));
      stage.show();
   }
}
