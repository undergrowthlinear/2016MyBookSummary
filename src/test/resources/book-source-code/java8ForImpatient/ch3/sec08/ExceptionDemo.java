import java.util.function.*;

public class ExceptionDemo {
   public static void doInOrder(Runnable first, Runnable second, Consumer<Throwable> handler) {
      Thread t = new Thread() {
            public void run() {
               try {
                  first.run();
                  second.run();
               } catch (Throwable t) {
                  handler.accept(t);
               }
            }
         };
      t.start();  
   }

   public static void main(String[] args) {
      doInOrder(
         () -> System.out.println(args[0]),
         () -> System.out.println("Goodbye"),
         (e) -> System.out.println("Yikes: " + e));      
      
   }
}
