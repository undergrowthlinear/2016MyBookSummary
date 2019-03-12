import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class AtomicUpdates2 {
    public static int ntasks = 1000;
    public static int iterations = 1000;

    public static AtomicLong nextNumber = new AtomicLong(ntasks * iterations / 2);
    public static AtomicLong largest = new AtomicLong();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();

        for (int t = 0; t < ntasks; t++) {
            pool.submit(() -> {
                long start = nextNumber.incrementAndGet();
                for (int i = 0; i < iterations; i++) {
                    long observed = (start + ntasks * i) % (ntasks * iterations);
                    long oldValue, newValue;
                    do {
                        oldValue = largest.get();
                        newValue = Math.max(oldValue, observed);
                    } while (!largest.compareAndSet(oldValue, newValue));
                }
            });
        }
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println(largest);
    }
}
