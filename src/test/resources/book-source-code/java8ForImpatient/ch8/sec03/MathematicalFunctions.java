import java.util.*;

public class MathematicalFunctions {
   public static void main(String[] args) {
      System.out.println(100000 * 100000);
      try {
         System.out.println(Math.multiplyExact(100000, 100000));
      } catch (ArithmeticException ex) {
         ex.printStackTrace();
      }

      long product = 100000L * 100000L;
      System.out.println(product);
      try {
         int n = Math.toIntExact(product);
      } catch (ArithmeticException ex) {
         ex.printStackTrace();
      }

      System.out.println(Math.floorMod(6 + 10, 12)); // Ten hours later
      System.out.println(Math.floorMod(6 - 10, 12)); // Ten hours earlier
      System.out.println(Math.floorMod(6 + 10, -12));
      System.out.println(Math.floorMod(6 + 10, -12));

      Random generator = new Random(164311266871034L); 
         // Also try new Random(881498)
      for (int tries = 1; tries < 1000000000; tries++) {
         double r = 1.0 - generator.nextDouble();
         if (r == 1.0) {
            System.out.println("It happened at try " + tries);
            r = Math.nextDown(r);
            System.out.println(r);                       
         }
      }

      /*
        Isn't that awesome? After just two tries, we happened to get 
        a floating-point value of 0. 

        Of course, it didn't just happen. Here is how to get the seed.
        The random number generator computes 
           next(s) = s * m + a % N
        where m = 25214903917, a = 11, and N = 2^48. 
        The inverse of m mod N is v = 246154705703781, and therefore 
           prev(s) = (s - a) * v mod N
        To make a double, next is called twice, and the top 26 and 27 
        bit are taken each time. When s is 0, next(s) is 11, so that's 
        what we want to hit: two consecutive numbers whose top bits are
        zero. Now, working backwards, we start with
           s = prev(prev(prev(0))
        Almost there--the Random constructor sets 
           s = (initialSeed ^ m) % N,
        so we need to offer it
           s = prev(prev(prev(0)) ^ m = 164311266871034
      */
   }
}
