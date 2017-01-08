import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;

public class ProcessDemo {
   public static void main(String[] args) throws IOException, InterruptedException {
      ProcessBuilder builder = new ProcessBuilder(
         "grep", "-o", "[A-Za-z_][A-Za-z_0-9]*");
      builder.redirectInput(Paths.get("ProcessDemo.java").toFile());
      builder.redirectOutput(Paths.get("identifiers.txt").toFile());
      Process process = builder.start();
      process.waitFor(1, TimeUnit.MINUTES);
   }
}
