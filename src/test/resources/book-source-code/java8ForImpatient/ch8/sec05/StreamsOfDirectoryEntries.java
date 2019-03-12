import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

public class StreamsOfDirectoryEntries {
    public static void main(String[] args) throws IOException {
        Path pathToDirectory = Paths.get("/home/cay");
        try (Stream<Path> entries = Files.list(pathToDirectory)) {
            entries.filter(p -> !p.getFileName().toString().startsWith(".")).forEach(System.out::println);
        }

        System.out.println("Hidden directories");
        try (Stream<Path> entries = Files.walk(pathToDirectory)) {
            entries.filter(p -> p.getFileName().toString().startsWith(".")).forEach(System.out::println);
        }

        System.out.println("Recent files");
        int depth = 5;
        Instant oneMonthAgo = Instant.now().minus(30, ChronoUnit.DAYS);
        try (Stream<Path> entries = Files.find(pathToDirectory, depth,
            (path, attrs) -> attrs.creationTime().toInstant().compareTo(oneMonthAgo) >= 0)) {
            entries.forEach(System.out::println);
        }
    }
}

