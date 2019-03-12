import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Grouping {
    public static void main(String[] args) throws IOException {
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
