import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class BulkOperations {
   public static void main(String[] args) throws IOException {
      ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();
      Path path = Paths.get("../../ch2/alice.txt");
      String contents = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
      Stream<String> words = Stream.of(contents.split("[\\P{L}]+"));
      words.parallel().forEach(w -> map.merge(w, 1L, Long::sum));

      long threshold = 1;
      String result = map.search(threshold, (k, v) -> v > 1000 ? k : null);
      System.out.println("result: " + result);
      System.out.println("\n---\n");
      map.forEach(threshold,
         (k, v) -> System.out.print(k + " -> " + v + ", "));

      System.out.println("\n---\n");
      map.forEach(threshold,
         (k, v) -> k + " -> " + v + ", ", // Transformer
         System.out::print); // Consumer
      System.out.println("\n---\n");

      map.forEach(threshold,
         (k, v) -> v > 300 ? k + " -> " + v : null, // Filter and transformer
         System.out::println); // The nulls are not passed to the consumer

      Long sum = map.reduceValues(threshold, Long::sum);
      System.out.println("\nsum: " + sum);

      Integer maxlength = map.reduceKeys(threshold,
         String::length, // Transformer
         Integer::max); // Accumulator
      System.out.println("maxlength: " + maxlength);
      Long count = map.reduceValues(threshold,
         v -> v > 300 ? 1L : null,
         Long::sum);
      System.out.println("count: " + count);
      long sum2 = map.reduceValuesToLong(threshold,
         Long::longValue, // Transformer to primitive type
         0, // Default value for empty map
         Long::sum); // Primitive type accumulator
      System.out.println("sum2: " + sum2);
   }
}
