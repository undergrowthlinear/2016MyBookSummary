import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class UpdatingValues {
   public static void main(String[] args) {
      String word = "";
      ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();
      Long oldValue = map.get(word);
      Long newValue = oldValue == null ? 1 : oldValue + 1;    
      map.put(word, newValue); // Error—might not replace oldValue

      do {
         oldValue = map.get(word);
         newValue = oldValue == null ? 1 : oldValue + 1;    
      } while (!map.replace(word, oldValue, newValue));

      ConcurrentHashMap<String, LongAdder> map2 = new ConcurrentHashMap<>();
      map2.putIfAbsent(word, new LongAdder());
      map2.get(word).increment();

      map.compute(word, (k, v) -> v == null ? 1 : v + 1);

      map2.computeIfAbsent(word, k -> new LongAdder()).increment();

      map.merge(word, 1L, (existingValue, newValue2) -> existingValue + newValue2);

      map.merge(word, 1L, Long::sum);
   }
}
