import java.io.*;
import java.nio.file.*;
import java.util.*;

public class TryWithResources {
   public static void main(String[] args) {
      try {
         int count = 0;
         try (Scanner in = new Scanner(Paths.get("/usr/share/dict/words"))) {
               while (in.hasNext() && ++count < 100)
                  System.out.println(in.next().toLowerCase());
            }
         try (Scanner in = new Scanner(Paths.get("/usr/share/dict/words"));
               PrintWriter out = new PrintWriter("/tmp/out.txt")) {
            while (in.hasNext())
               out.println(in.next().toLowerCase());
         }
      } catch (IOException ex) { // Separate try-with-resources from try/catch
         ex.printStackTrace();
      }      

      
      // This works as it should
      try {
         try (InputStream in = new InputStream() {
                  public int read() throws IOException { 
                     throw new IOException("read failed");
                  }
                  public void close() throws IOException { 
                     throw new IOException("close failed");
                  }
            }) {
            System.out.println(in.read());
         } 
      } catch (Exception ex) {
         System.out.println(ex);
         Throwable[] secondaryExceptions = ex.getSuppressed();  
         System.out.println(Arrays.toString(secondaryExceptions));
      }
      

      // This doesn't since, depressingly, the Scanner, changes the
      // exception type without also carrying along the suppressed 
      // exceptions
      try {
         try (Scanner in = new Scanner(new InputStream() {
                  public int read() throws IOException { 
                     System.out.println("reading");
                     throw new IOException("read failed");
                  }
                  public void close() throws IOException { 
                     System.out.println("closing");
                     throw new IOException("close failed");
                  }
            })) {
            System.out.println(in.next());
         } 
      } catch (Exception ex) {
         System.out.println(ex);
         Throwable[] secondaryExceptions = ex.getSuppressed();  
         System.out.println(Arrays.toString(secondaryExceptions));
      }

      // As you can see, the read and close method got called, and both
      // threw exceptions, but both the read and the close exception
      // are still lost. The scanner takes it upon itself to catch 
      // exceptions from the underlying stream without linking them,
      // and then it throws a different exception
   }
}
