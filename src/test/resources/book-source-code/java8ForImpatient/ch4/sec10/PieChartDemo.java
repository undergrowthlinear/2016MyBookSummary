import javafx.application.*;
import javafx.collections.*;
import javafx.scene.*;
import javafx.scene.chart.*;
import javafx.stage.*;

 
public class PieChartDemo extends Application {
   public void start(Stage stage) {
      ObservableList<PieChart.Data> pieChartData =
         FXCollections.observableArrayList(
            new PieChart.Data("Asia", 4298723000.0),
            new PieChart.Data("North America", 355361000.0),
            new PieChart.Data("South America", 616644000.0),
            new PieChart.Data("Europe", 742452000.0),
            new PieChart.Data("Africa", 1110635000.0),
            new PieChart.Data("Oceania", 38304000.0));
      final PieChart chart = new PieChart(pieChartData);
      chart.setTitle("Population of the Continents");

      Group group = new Group(chart);
      Scene scene = new Scene(group);
      stage.setWidth(500);
      stage.setHeight(500);
      stage.setScene(scene);
      stage.show();
   }
 
}
