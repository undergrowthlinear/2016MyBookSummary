import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Test {
   public static void main(String[] args) throws IOException {
      Integer[] digits = {
         3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9, 3, 2, 3, 8, 4, 6, 2, 6, 4, 3, 3, 8, 3, 2, 7, 9, 5, 0, 2, 8, 8, 4, 1, 9, 7, 1, 6, 9, 3, 9, 9, 3, 7, 5, 1, 0, 5, 8, 2, 0, 9, 7, 4, 9, 4, 4, 5, 9, 2, 3, 0, 7, 8, 1, 6, 4, 0, 6, 2, 8, 6 };
      Stream<Integer> values = Stream.of(digits);
      Optional<Integer> sum = values.reduce((x, y) -> x + y);
      System.out.println("sum: " + sum);
        
      values = Stream.empty();
      sum = values.reduce((x, y) -> x + y); // Or values.reduce(Integer::sum);
      System.out.println("sum: " + sum);

      values = Stream.of(digits);
      Integer sum2 = values.reduce(0, (x, y) -> x + y);
      System.out.println("sum2: " + sum2);        

      values = Stream.empty();
      Integer sum3 = values.reduce(0, (x, y) -> x + y);
      System.out.println("sum3: " + sum3);

      String contents = new String(Files.readAllBytes(
            Paths.get("../alice.txt")), StandardCharsets.UTF_8);
      List<String> wordList = Arrays.asList(contents.split("[\\P{L}]+"));
      Stream<String> words = wordList.stream();
        
      int result = words.reduce(0, 
         (s, w) -> s + w.length(), 
         (s1, s2) -> s1 + s2);

      System.out.println("result: " + result);
   }
}


