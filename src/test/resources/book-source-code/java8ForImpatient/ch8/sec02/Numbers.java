import java.math.*;
import java.util.*;

public class Numbers {
   public static void main(String[] args) {
      System.out.printf("Integer SIZE: %d BYTES: %d\n",
         Integer.SIZE, Integer.BYTES);
      System.out.printf("Long SIZE: %d BYTES: %d\n",
         Long.SIZE, Long.BYTES);
      System.out.printf("Double SIZE: %d BYTES: %d\n",
         Double.SIZE, Double.BYTES);
      System.out.printf("Float SIZE: %d BYTES: %d\n",
         Float.SIZE, Float.BYTES);
      System.out.printf("Short SIZE: %d BYTES: %d\n",
         Short.SIZE, Short.BYTES);
      System.out.printf("Byte SIZE: %d BYTES: %d\n",
         Byte.SIZE, Byte.BYTES);
      System.out.printf("Character SIZE: %d BYTES: %d\n",
         Character.SIZE, Character.BYTES);

      System.out.println(Double.hashCode(0.25));
      Random generator = new Random();

      System.out.println(generator.ints().limit(100).reduce(Integer::sum));
      System.out.println(generator.ints().limit(100).reduce(Integer::min));
      
      System.out.println(generator.ints().limit(100).mapToObj(i -> i % 2 == 0).reduce(Boolean::logicalXor));   

      int maxValue = Integer.MAX_VALUE;
      int nextValue = maxValue + 1;
      System.out.printf("maxValue: %d, nextValue: %d, compared: %d\n",
         maxValue, nextValue, Integer.compareUnsigned(maxValue, nextValue));
      System.out.printf("nextValue / 65536 as unsigned: %d\n", Integer.divideUnsigned(nextValue, 65536));
      byte b = -127; 
      System.out.println(Byte.toUnsignedInt(b));

      System.out.println(Double.isFinite(1.0 / 0.0));
      System.out.println(Double.isFinite(Math.sqrt(-1.0)));

      b = new BigInteger("129").byteValueExact();
   }
}
