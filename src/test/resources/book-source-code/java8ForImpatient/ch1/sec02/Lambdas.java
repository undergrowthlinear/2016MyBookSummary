import java.util.*;
import javafx.event.*;

public class Lambdas {
   public static void main(String[] args) {
      Comparator<String> comp = 
         (String first, String second)
            -> Integer.compare(first.length(), second.length());

      comp = 
         (String first, String second) -> {
            if (first.length() < second.length()) return -1;
            else if (first.length() > second.length()) return 1;
            else return 0;
         };

      Runnable runner =
         () -> { for (int i = 0; i < 1000; i++) doWork(); };

      comp =
        (first, second) // Same as (String first, String second)
          -> Integer.compare(first.length(), second.length());

      EventHandler<ActionEvent> listener = e -> System.out.println(e.getTarget());
         // Instead of (e) -> or (ActionEvent e) ->
   }

   public static void doWork() {
   }
}
