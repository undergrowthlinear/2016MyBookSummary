import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.concurrent.*;

class Util {
   public static String getPage(String urlString)  {
      try {
         Scanner in = new Scanner(new URL(urlString).openStream());
         StringBuilder builder = new StringBuilder();
         while (in.hasNextLine()) { 
            builder.append(in.nextLine()); 
            builder.append("\n");
         }
         return builder.toString();
      } catch (IOException ex) {
         RuntimeException rex = new RuntimeException();
         rex.initCause(ex);
         throw rex;
      }
   }

   public static List<String> matches(String input, String patternString) {
      Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(input);
      List<String> result = new ArrayList<>();
      
      while (matcher.find()) 
         result.add(matcher.group(1));
      return result;
   }

   public static Scanner in = new Scanner(System.in);

   public static String getInput(String prompt) {
      System.out.print(prompt + ": ");
      return in.nextLine();      
   }

   public static <T> CompletableFuture<T> repeat(Supplier<T> action,
      Predicate<T> until) {
      return CompletableFuture.supplyAsync(action).thenComposeAsync((T t) -> 
         { 
         return until.test(t) ? CompletableFuture.completedFuture(t) : repeat(action, until);
         });
   }

}




public class FutureDemo {
   public static void main(String[] args) throws ExecutionException, InterruptedException {
      String hrefPattern = "<a\\s+href\\s*=\\s*(\"[^\"]*\"|[^\\s>]*)\\s*>";
      // CompletableFuture<String> getURL = CompletableFuture.supplyAsync(() -> Util.getInput("URL"));
      
      // Make a function mapping URL to CompletableFuture
      
      CompletableFuture<String> f = Util.repeat(() -> Util.getInput("URL"), s -> s.startsWith("http://")).thenApplyAsync((String url) -> Util.getPage(url));
      CompletableFuture<List<String>> links = f.thenApply(c -> Util.matches(c, hrefPattern));
      links.thenAccept(System.out::println);
      ForkJoinPool.commonPool().awaitQuiescence(10,  TimeUnit.SECONDS);
      System.out.println("exiting");
   }
}
