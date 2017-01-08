import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class FilesDemo {
   public static void main(String[] args) throws IOException {
      Path path = Paths.get("FilesDemo.java");
      byte[] bytes = Files.readAllBytes(path);
      String content = new String(bytes, StandardCharsets.UTF_8);
      System.out.println(content.substring(0, 100) + "...");
      List<String> lines = Files.readAllLines(path);
      System.out.println("Last line: " + lines.get(lines.size() - 1));
      path = Paths.get("FilesDemo1.out");
      content = content.replaceAll("e", "x");
      Files.write(path, content.getBytes(StandardCharsets.UTF_8));
      path = Paths.get("FilesDemo2.out");
      Files.write(path, lines);
      Collections.reverse(lines);
      Files.write(path, lines, StandardOpenOption.APPEND);
      URL url = new URL("http://horstmann.com");

      boolean deleted = Files.deleteIfExists(Paths.get("FilesDemo3.out"));
      System.out.println(deleted);

      Files.copy(url.openStream(), Paths.get("FilesDemo3.out"));
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      Files.copy(Paths.get("FilesDemo3.out"), out);
      System.out.println(out.toString("UTF-8").substring(0, 100) + "...");

      Files.copy(Paths.get("FilesDemo3.out"), Paths.get("FilesDemo4.out"));
      Files.move(Paths.get("FilesDemo4.out"), Paths.get("FilesDemo5.out"));
      Files.copy(Paths.get("FilesDemo3.out"), Paths.get("FilesDemo5.out"), 
         StandardCopyOption.REPLACE_EXISTING,
         StandardCopyOption.COPY_ATTRIBUTES);      
      Files.move(Paths.get("FilesDemo5.out"), Paths.get("FilesDemo6.out"), 
         StandardCopyOption.ATOMIC_MOVE);
      Files.delete(Paths.get("FilesDemo6.out"));

      Path newPath = Files.createTempFile(null, ".txt");
      System.out.println(newPath);
      newPath = Files.createTempDirectory("fred");
      System.out.println(newPath);
   }
}
