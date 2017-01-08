import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Test
{
    public static void main(String[] args) throws IOException
    {
        String contents = new String(Files.readAllBytes(
                Paths.get("../alice.txt")), StandardCharsets.UTF_8);
        List<String> words = Arrays.asList(contents.split("[\\P{L}]+"));

        long count = 0;
        for (String w : words) {
           if (w.length() > 12) count++;  
        }
        System.out.println(count);

        count = words.stream().filter(w -> w.length() > 12).count();
        System.out.println(count);


        count = words.parallelStream().filter(w -> w.length() > 12).count();
        System.out.println(count);
    }
}
