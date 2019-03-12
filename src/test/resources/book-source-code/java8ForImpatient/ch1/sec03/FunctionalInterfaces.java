import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class FunctionalInterfaces extends Application {
    public void start(Stage stage) {
        String[] strings = "Mary had a little lamb".split(" ");

        Arrays.sort(strings,
            (first, second) -> Integer.compare(first.length(), second.length()));

        System.out.println(Arrays.toString(strings));

        Button button = new Button("Click me!");
        button.setOnAction(
            event -> System.out.println("Thanks for clicking!"));

        stage.setScene(new Scene(button));
        stage.show();

        BiFunction<String, String, Integer> comp
            = (first, second) -> Integer.compare(first.length(), second.length());
        // Arrays.sort(strings, comp);
        // Error: Arrays.sort doesn't want a BiFunction

        // Runnable sleeper = () -> { System.out.println("Zzz"); Thread.sleep(1000); };
        // Error: Thread.sleep can throw a checked InterruptedException

        Runnable sleeper2 = () -> {
            System.out.println("Zzz");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        };

        Callable<Void> sleeper3 = () -> {
            System.out.println("Zzz");
            Thread.sleep(1000);
            return null;
        };
    }
}
