interface Person {
   long getId();
   default String getName() { return "John Q. Public"; }
}

interface Persistent {
   default String getName() { return getClass().getName() + "_" + hashCode(); }
}

class Student implements Person, Persistent {
   public long getId() { return 42; }
   public String getName() { return Person.super.getName(); }
}
