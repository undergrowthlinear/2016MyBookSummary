message = new javafx.scene.control.Label()
message.font = new javafx.scene.text.Font(100)
stage.scene = new javafx.scene.Scene(message)
stage.title = 'Hello'
var list = new java.util.ArrayList()
list.add(1);
list.add(42);
list['remove(Object)'](1)
message['text'] = list.get(0)
stage.show();
