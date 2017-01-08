public class TestCaseDemo {
   @TestCase(params="4", expected="24")
   @TestCase(params="0", expected="1")
   /**
      @long the returned value
    */
   public static long factorial(int n) {
      return n <= 1 ? 1 : n * factorial(n - 1);
   }
}
