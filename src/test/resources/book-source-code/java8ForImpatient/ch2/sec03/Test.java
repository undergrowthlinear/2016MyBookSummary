import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
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

   public static Stream<Character> characterStream(String s) {
      List<Character> result = new ArrayList<>();
      for (char c : s.toCharArray()) result.add(c);
      return result.stream();
   }

   public static void main(String[] args) throws IOException {
      String contents = new String(Files.readAllBytes(
            Paths.get("../alice.txt")), StandardCharsets.UTF_8);
      List<String> wordList = Arrays.asList(contents.split("[\\P{L}]+"));
      Stream<String> words = wordList.stream();
      Stream<String> longWords = words.filter(w -> w.length() > 12);
      show("longWords", longWords);

      words = wordList.stream();
      Stream<String> lowercaseWords = words.map(String::toLowerCase);
      show("lowercaseWords", lowercaseWords);

      Stream<String> song = Stream.of("row", "row", "row", "your", "boat", "gently", "down", "the", "stream");
      Stream<Character> firstChars = song.filter(w -> w.length() > 0).map(s -> s.charAt(0));
      show("firstChars", firstChars);

      song = Stream.of("row", "row", "row", "your", "boat", "gently", "down", "the", "stream");
      Stream<Character> letters = song.flatMap(w -> characterStream(w));
      show("letters", letters);
   }
}
