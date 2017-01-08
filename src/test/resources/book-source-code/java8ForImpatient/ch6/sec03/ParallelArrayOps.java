import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;

public class ParallelArrayOps {
   public static void main(String[] args) throws IOException {
      String contents = new String(Files.readAllBytes(
            Paths.get("../../ch2/alice.txt")), StandardCharsets.UTF_8); // Read file into string
      String[] words = contents.split("[\\P{L}]"); // Split along nonletters
      Arrays.parallelSort(words);
      // System.out.println(Arrays.toString(words));

      int[] values = new int[20];
      Arrays.parallelSetAll(values, i -> i % 10);
      System.out.println(Arrays.toString(values));

      Arrays.parallelSetAll(values, i -> i + 1);
      Arrays.parallelPrefix(values, (x, y) -> x * y);
      System.out.println(Arrays.toString(values));
   }
}
