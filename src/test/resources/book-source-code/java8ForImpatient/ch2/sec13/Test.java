import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import static java.util.stream.Collectors.*;

public class Test {
    public static void main(String[] args) throws IOException {
        String contents = new String(Files.readAllBytes(
                Paths.get("../alice.txt")), StandardCharsets.UTF_8);
        List<String> wordList = Arrays.asList(contents.split("[\\P{L}]+"));

        Stream<String> words = wordList.stream();
        
        // Very bad code ahead
        int[] shortWords = new int[10];
        words.parallel().forEach(
            s -> { if (s.length() < 10) shortWords[s.length()]++; });
        System.out.println(Arrays.toString(shortWords));

        // Try again--the result will likely be different (and also wrong)
        Arrays.fill(shortWords, 0);
        words = wordList.stream();
        words.parallel().forEach(
            s -> { if (s.length() < 10) shortWords[s.length()]++; });
        System.out.println(Arrays.toString(shortWords));

        // Sequential stream works ok
        Arrays.fill(shortWords, 0);
        words = wordList.stream();
        words.forEach(
            s -> { if (s.length() < 10) shortWords[s.length()]++; });
        System.out.println(Arrays.toString(shortWords));

        // Atomic integers 
        AtomicInteger[] shortWordCounters = new AtomicInteger[10];
        for (int i = 0; i < shortWordCounters.length; i++)
            shortWordCounters[i] = new AtomicInteger();
        words = wordList.stream();
        words.forEach(
            s -> { if (s.length() < 10) shortWordCounters[s.length()].getAndIncrement(); });
        System.out.println(Arrays.toString(shortWordCounters));

        // Grouping works in parallel
        words = wordList.stream();
        System.out.println(
            words.parallel().filter(s -> s.length() < 10).collect(
               groupingBy(
                  String::length,
                  counting())));                
   }
}


