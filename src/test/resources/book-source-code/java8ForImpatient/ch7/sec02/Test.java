import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Test {
    public static void main(String[] args) throws ScriptException, IOException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        Object result = engine.eval("'Hello, World!'.length");
        System.out.println(result);
        result = engine.eval(Files.newBufferedReader(Paths.get("hello.js")));
        System.out.println(result);
    }
}
