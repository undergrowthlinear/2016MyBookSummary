import java.io.*;
import java.math.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

public class Test {
   public static <T> void show(String title, Stream<T> stream) {
      final int SIZE = 10;
      List<T> firstElements = stream.limit(SIZE + 1).collect(Collectors.toList());
      System.out.print(title + ": ");
      if (firstElements.size() <= SIZE)
         System.out.println(firstElements);
      else {
         firstElements.remove(SIZE);
         String out = firstElements.toString();            
         System.out.println(out.substring(0, out.length() - 1) + ", ...]");
      }
   }

   public static void main(String[] args) throws IOException {
      Path path = Paths.get("../alice.txt");
      String contents = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        
      Stream<String> words = Stream.of(contents.split("[\\P{L}]+"));
      show("words", words);
      Stream<String> song = Stream.of("gently", "down", "the", "stream");
      show("song", song);
      Stream<String> silence = Stream.empty();
      silence = Stream.<String>empty(); // Explicit type specification
      show("silence", silence);
 

      Stream<String> echos = Stream.generate(() -> "Echo");
      show("echos", echos);

      Stream<Double> randoms = Stream.generate(Math::random);
      show("randoms", randoms);

      Stream<BigInteger> integers = Stream.iterate(BigInteger.ONE, n -> n.add(BigInteger.ONE));
      show("integers", integers);

      Stream<String> wordsAnotherWay = Pattern.compile("[\\P{L}]+").splitAsStream(contents);
      show("wordsAnotherWay", wordsAnotherWay);

      try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            show ("lines", lines);                
         }
   }
}
