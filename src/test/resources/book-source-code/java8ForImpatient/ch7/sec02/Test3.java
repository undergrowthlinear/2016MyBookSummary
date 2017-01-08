import java.io.*;
import java.nio.file.*;
import javafx.application.*;
import javafx.stage.*;
import javax.script.*;

public class Test3 extends Application {
   private static ScriptEngineManager manager = new ScriptEngineManager();
   private static ScriptEngine engine = manager.getEngineByName("nashorn");

   public void start(Stage stage) {
      Bindings scope = engine.createBindings();
      scope.put("stage", stage);
      try {
         engine.eval(Files.newBufferedReader(Paths.get("hellofx.js")), scope); 
         // Script code can access the object as stage
      } catch (IOException | ScriptException ex) {
         ex.printStackTrace();
      }
   }
}
