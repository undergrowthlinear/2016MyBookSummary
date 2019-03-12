import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

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
