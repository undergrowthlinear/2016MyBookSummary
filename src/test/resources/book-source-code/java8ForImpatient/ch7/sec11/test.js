// Run as jjs test.js

list = new java.util.ArrayList()
try {
   var first = list.get(0)
   print(first)
} catch (e) {
   if (e instanceof java.lang.IndexOutOfBoundsException)
      print('list is empty')     
}
