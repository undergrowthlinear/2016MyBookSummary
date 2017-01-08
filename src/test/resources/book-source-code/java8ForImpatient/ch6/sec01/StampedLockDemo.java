import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

class Vector {
   private int size;
   private Object[] elements = new Object[10];
   private StampedLock lock = new StampedLock();

   public Object get(int n) {
      long stamp = lock.tryOptimisticRead();
      Object[] currentElements = elements;
      int currentSize = size; 
      if (!lock.validate(stamp)) { // Someone else had a write lock
         stamp = lock.readLock(); // Get a pessimistic lock
         currentElements = elements;
         currentSize = size; 
         lock.unlockRead(stamp);
      }
      return n < currentSize ? currentElements[n] : null;
   }

   public int size() {
      long stamp = lock.tryOptimisticRead();
      int currentSize = size; 
      if (!lock.validate(stamp)) { // Someone else had a write lock
         stamp = lock.readLock(); // Get a pessimistic lock
         currentSize = size; 
         lock.unlockRead(stamp);
      }
      return currentSize;
   }

   public void add(Object obj) {
      long stamp = lock.writeLock();
      try {
         if (size == elements.length) 
            elements = Arrays.copyOf(elements, 2 * size);
         elements[size] = obj;
         size++;
      } finally {
         lock.unlockWrite(stamp);
      }
   }
}
