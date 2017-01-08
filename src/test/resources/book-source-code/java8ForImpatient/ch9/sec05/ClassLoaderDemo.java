import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class ClassLoaderDemo {
   public static void main(String[] args) throws IOException, ReflectiveOperationException {
      URL[] urls = { 
         new URL("file:junit-4.11.jar"), 
         new URL("file:hamcrest-core-1.3.jar")
      };
      try (URLClassLoader loader = new URLClassLoader(urls)) {
         Class<?> klass = loader.loadClass("org.junit.runner.JUnitCore");
         System.out.println(klass.getMethod("main", String[].class).invoke(null, (Object) args));
      }
   }
}
