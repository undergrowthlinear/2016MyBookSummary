import java.beans.*;
import java.io.*;
import java.util.*;
import javax.annotation.processing.*;
import javax.lang.model.*;
import javax.lang.model.element.*;
import javax.tools.*;
import javax.tools.Diagnostic.*;

@SupportedAnnotationTypes({"TestCase","TestCases"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class TestCaseProcessor extends AbstractProcessor
{

   @Override
   public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
   {
      for (TypeElement t : annotations)
      {
         for (Element e : roundEnv.getElementsAnnotatedWith(t))
         {
            System.out.println(e + " " + t);
         }
      }
      return true;
   }
}
