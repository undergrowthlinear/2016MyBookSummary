import java.lang.annotation.Repeatable;

@Repeatable(TestCases.class)
public @interface TestCase {
    String params();

    String expected();
}
