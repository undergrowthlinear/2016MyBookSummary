import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Test {
   public static void main(String[] args) throws IOException {
      String contents = new String(Files.readAllBytes(
            Paths.get("../alice.txt")), StandardCharsets.UTF_8);
      List<String> wordList = Arrays.asList(contents.split("[\\P{L}]+"));

      Stream<String> words = wordList.stream();

      Optional<String> largest = words.max(String::compareToIgnoreCase);
      if (largest.isPresent())
         System.out.println("largest: " + largest.get());

      words = wordList.stream();
      boolean aWordStartsWithQ
         = words.anyMatch(s -> s.startsWith("Q"));
      System.out.println("aWordStartsWithQ: " + aWordStartsWithQ);

      words = wordList.stream();
      Optional<String> startsWithQ = words.parallel().filter(s -> s.startsWith("Q")).findAny();
      if (startsWithQ.isPresent())
         System.out.println("startsWithQ: " + startsWithQ.get());
      else
         System.out.println("No word starts with Q");
   }
}


