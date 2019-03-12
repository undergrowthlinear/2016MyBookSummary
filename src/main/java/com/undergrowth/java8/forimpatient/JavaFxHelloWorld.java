package com.undergrowth.java8.forimpatient;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description JavaFxHelloWorld学习类 舞台上有场景 场景上放置布局 布局里放置组件
 * @date 2017-01-18-22:00
 */
public class JavaFxHelloWorld extends Application {

    /**
     * The main entry point for all JavaFX applications. The start method is called after the init method has returned, and after the system is ready
     * for the application to begin running. <p> <p> NOTE: This method is called on the JavaFX Application Thread. </p>
     *
     * @param primaryStage the primary stage for this application, onto which the application scene can be set. The primary stage will be embedded in
     * the browser if the application was launched as an applet. Applications may create other stages, if needed, but they will not be primary stages
     * and will not be embedded in the browser.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        //舞台上有场景
        //场景上放置组件
        Label message = new Label("Hello World,JavaFx");
        Button red = new Button("red");
        //绑定属性事件
        red.setOnAction(event -> message.setTextFill(Color.RED));
        //垂直布局
        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(message, red);
        Scene scene = new Scene(vBox);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hello");
        primaryStage.setHeight(200);
        primaryStage.setWidth(300);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(JavaFxHelloWorld.class, args);
    }
}
