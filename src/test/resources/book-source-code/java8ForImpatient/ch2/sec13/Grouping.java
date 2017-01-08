import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Grouping
{
    public static void main(String[] args) throws IOException
    {
        String contents = new String(Files.readAllBytes(
                Paths.get("../alice.txt")), StandardCharsets.UTF_8);
        List<String> wordList = Arrays.asList(contents.split("[\\P{L}]+"));
        Map<Integer, List<String>> result = wordList.stream().collect(
           Collectors.groupingByConcurrent(String::length));

        System.out.println(result.get(14));

        result = wordList.stream().parallel().collect(
           Collectors.groupingByConcurrent(String::length));

        System.out.println(result.get(14));
    }
}
