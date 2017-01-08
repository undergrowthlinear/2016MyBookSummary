import java.util.function.*;
import java.util.logging.*;

public class Logging {
   public static void info(Logger logger, Supplier<String> message) {
      if (logger.isLoggable(Level.INFO))
         logger.info(message.get());
   }
   public static void main(String[] args) {
      double x = 3;
      double y = 4;
      info(Logger.getGlobal(), () -> "x: " + x + ", y: " + y);
   }
}
