import java.util.*;
import java.util.logging.*;

public class Misc {
   public static void main(String[] args) {
      double x = Double.parseDouble("+1.0");
      int n = Integer.parseInt("+1");
      System.out.printf("%f %d\n", x, n);

      Logger.getGlobal().info("x=" + x);

      try {
         new Misc().process(null);
      } catch (Exception ex) {
         ex.printStackTrace();
      }

      byte[] bytes = { (byte) 0b10101100, (byte) 0b00101000 };
      BitSet primes = BitSet.valueOf(bytes);
      System.out.println(primes);
      long[] longs = { 0x100010116L, 0x1L, 0x1L, 0L, 0x1L };
      BitSet powersOfTwo = BitSet.valueOf(longs);
      System.out.println(powersOfTwo);
      for (byte b : powersOfTwo.toByteArray())
         System.out.print(Integer.toBinaryString(Byte.toUnsignedInt(b)));
      System.out.println();
      System.out.println(powersOfTwo.stream().sum());
   }

   private String directions;

   public void process(String directions) {
      this.directions = Objects.requireNonNull(directions,
         "directions must not be null");           
   }
}
