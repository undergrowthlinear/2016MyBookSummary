import java.util.*;

public class Person {
   private String first;
   private String last;
   
   public boolean equals(Object otherObject) {
      if (this == otherObject) return true;
      if (otherObject == null) return false;
      if (getClass() != otherObject.getClass()) return false;
      Person other = (Person) otherObject;
      
      return Objects.equals(first, other.first) && Objects.equals(last, other.last);
   }

   public int hashCode() {
      return Objects.hash(first, last);
   }
}
