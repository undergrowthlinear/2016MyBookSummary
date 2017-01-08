import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class Base64Demo {
   public static void main(String[] args) throws IOException {
      String username = "fred";
      String password = "secret";
      Base64.Encoder encoder = Base64.getEncoder();
      String original = username + ":" + password;
      String encoded = encoder.encodeToString(original.getBytes(StandardCharsets.UTF_8));
      System.out.println(encoded);
      String decoded = new String(Base64.getDecoder().decode(encoded.getBytes()));
      System.out.println(decoded);

      Path originalPath = Paths.get("Base64Demo.java"), 
         encodedPath = Paths.get("Base64Demo.java.base64");
      encoder = Base64.getMimeEncoder();
      try (OutputStream output = Files.newOutputStream(encodedPath)) {
         Files.copy(originalPath, encoder.wrap(output));
      }

      Path decodedPath = Paths.get("Base64Demo.java.decoded");      
      Base64.Decoder decoder = Base64.getMimeDecoder();
      try (InputStream input = Files.newInputStream(encodedPath)) {
         Files.copy(decoder.wrap(input), decodedPath);
      }
   }
}
