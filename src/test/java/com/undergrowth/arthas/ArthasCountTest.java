package com.undergrowth.arthas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ArthasCountTest {
    static class Counter {
        private static AtomicInteger count = new AtomicInteger(0);

        public static void increment() {
            count.incrementAndGet();
        }

        public static int value() {
            return count.get();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            Counter.increment();
            System.out.println("counter: " + Counter.value());
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
