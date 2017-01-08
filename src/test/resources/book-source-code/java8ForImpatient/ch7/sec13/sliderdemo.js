var message = new javafx.scene.control.Label("Hello, JavaFX!");
var Font = javafx.scene.text.Font
message.font = new Font(100);
var slider = new javafx.scene.control.Slider()
slider.value = 100
slider.valueProperty().addListener(
   new javafx.beans.InvalidationListener(function(property) 
      message.font = new Font(slider.value)))
var root = new javafx.scene.layout.VBox()
root.children.addAll(slider, message)
$STAGE.scene = new javafx.scene.Scene(root);
$STAGE.title = "Hello";

/*
*/

