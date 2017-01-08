import java.time.*;
import java.util.*;

public class CollectionMethods {
   public static void main(String[] args) {
      List<String> ids = new ArrayList<>(ZoneId.getAvailableZoneIds());
      ids.removeIf(s -> !s.startsWith("America"));
      ids.forEach(System.out::println);
      ids.replaceAll(s -> s.replace("America/", ""));
      System.out.println("---");
      ids.forEach(System.out::println);
      BitSet bits = new BitSet();
      ids.forEach(s -> bits.set(s.length()));
      System.out.println(Arrays.toString(bits.stream().toArray()));
   }
}
