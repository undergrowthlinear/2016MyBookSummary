import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Test {
   public static <T> void show(String title, Stream<T> stream) {
      final int SIZE = 10;
      List<T> firstElements = stream.limit(SIZE + 1).collect(Collectors.toList());
      System.out.print(title + ": ");
      if (firstElements.size() <= SIZE)
         System.out.println(firstElements);
      else {
         firstElements.remove(SIZE);
         String out = firstElements.toString();            
         System.out.println(out.substring(0, out.length() - 1) + ", ...]");
      }
   }

   public static Stream<Character> characterStream(String s) {
      List<Character> result = new ArrayList<>();
      for (char c : s.toCharArray()) result.add(c);
      return result.stream();
   }

   public static void main(String[] args) throws IOException {
      Stream<Double> randoms = Stream.generate(Math::random).limit(100);
      show("randoms", randoms);

      Stream<Integer> integers = Stream.iterate(0, n -> n + 1);
      Stream<Integer> firstFive = integers.limit(5);
      show("firstFive", firstFive);

      integers = Stream.iterate(0, n -> n + 1);
      Stream<Integer> notTheFirst = integers.skip(1);
      show("notTheFirst", notTheFirst);        

      Stream<Character> combined = Stream.concat(
         characterStream("Hello"), 
         characterStream("World"));
      show("combined", combined);

      Object[] powers = Stream.iterate(1.0, p -> p * 2)
         .peek(e -> System.out.println("Fetching " + e))
         .limit(20).toArray();
      System.out.println(Arrays.asList(powers));
   }
}


