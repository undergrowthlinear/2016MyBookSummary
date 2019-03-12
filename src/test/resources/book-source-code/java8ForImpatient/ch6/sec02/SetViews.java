import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SetViews {
    public static void main(String[] args) {
        Set<String> words = ConcurrentHashMap.<String>newKeySet();

        ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();
        words = map.keySet(1L);
        words.add("Java");
        System.out.println(map.get("Java"));
    }
}
