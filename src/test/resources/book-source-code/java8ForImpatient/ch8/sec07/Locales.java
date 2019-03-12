import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Locales {
    public static void main(String[] args) {
        List<Locale.LanguageRange> ranges =
            Stream.of("de", "*-CH").map(Locale.LanguageRange::new).collect(Collectors.toList());
        List<Locale> matches = Locale.filter(ranges,
            Arrays.asList(Locale.getAvailableLocales()));
        System.out.println(matches);
        Locale bestMatch = Locale.lookup(ranges,
            Arrays.asList(Locale.getAvailableLocales()));
        System.out.println(bestMatch);
    }
}
