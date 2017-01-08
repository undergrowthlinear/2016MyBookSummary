import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class LongAdderDemo {
   private static void await(CyclicBarrier barrier) {
      try {
         barrier.await();
      } catch (InterruptedException | BrokenBarrierException ex) {
         ex.printStackTrace();
         // Won't happen in this application
      }
   }

   public static double run(int nthreads, int iterations, Runnable action) {      
      Thread[] threads = new Thread[nthreads];      
      CyclicBarrier barrier = new CyclicBarrier(nthreads + 1);

      for (int t = 0; t < nthreads; t++) {
         threads[t] = new Thread(() -> {
               await(barrier);
               for (int i = 0;  i < iterations; i++) {
                  action.run();
               }
               await(barrier);
            });
         threads[t].start();
      }
      await(barrier);
      long start = System.nanoTime();
      await(barrier);
      long end = System.nanoTime();
      return (end - start) * 1E-9;
   }

   public static void main(String[] args) {
      final int THREADS = 100;
      final int ITERATIONS = 1000000;      

      System.out.println("Synchronized");
      
      class Counter {
         private long count;
         synchronized void increment() { count++; }
         synchronized long get() { return count; }
      };

      Counter counter = new Counter();

      double elapsedTime = run(THREADS, ITERATIONS, () -> {
            counter.increment();
         });
         
      System.out.println(counter.get());
      System.out.println(elapsedTime + " seconds");

      System.out.println("AtomicLong");

      AtomicLong atomic = new AtomicLong();
      elapsedTime = run(THREADS, ITERATIONS, () -> {
            atomic.incrementAndGet();
         });
         
      System.out.println(atomic.get());
      System.out.println(elapsedTime + " seconds");

      System.out.println("LongAdder");

      LongAdder adder = new LongAdder();
      elapsedTime = run(THREADS, ITERATIONS, () -> {
            adder.increment();
         });
         
      System.out.println(adder.sum());
      System.out.println(elapsedTime + " seconds");     

      System.out.println("Unsynchronized");

      long[] badCounter = new long[1];

      elapsedTime = run(THREADS, ITERATIONS, () -> {
            badCounter[0]++;
         });
         
      System.out.println(badCounter[0]);
      System.out.println(elapsedTime + " seconds");
   }
}
