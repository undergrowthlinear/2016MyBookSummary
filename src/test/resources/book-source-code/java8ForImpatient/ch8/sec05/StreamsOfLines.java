import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public class StreamsOfLines {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("StreamsOfLines.java");
        try (Stream<String> lines = Files.lines(path)) {
            Optional<String> passwordEntry = lines.filter(s -> s.contains("password")).findFirst();
            passwordEntry.ifPresent(System.out::println);
        }

        try (Stream<String> filteredLines
            = Files.lines(path).onClose(() -> System.out.println("Closing")).filter(s -> s.contains("password"))) {
            Optional<String> passwordEntry = filteredLines.findFirst();
            passwordEntry.ifPresent(System.out::println);
        }

        URL url = new URL("http://horstmann.com");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            Stream<String> lines = reader.lines();
            Optional<String> imgEntry = lines.filter(s -> s.contains("<img ")).findFirst();
            imgEntry.ifPresent(System.out::println);
        }

        try (BufferedReader reader = new BufferedReader(new Reader() {
            private int count;

            public void close() {
            }

            public int read(char[] cbuf, int off, int len) throws IOException {
                if (++count == 10) {
                    throw new IOException("Simulated exception");
                }
                return len;
            }
        })) {
            Stream<String> lines = reader.lines();
            Optional<String> imgEntry = lines.filter(s -> s.contains("<img ")).findFirst();
            imgEntry.ifPresent(System.out::println);
        }
    }
}
