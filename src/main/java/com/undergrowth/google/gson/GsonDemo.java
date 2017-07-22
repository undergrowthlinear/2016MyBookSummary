package com.undergrowth.google.gson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.undergrowth.google.gson.writer.JsonWriterDemo;
import com.undergrowth.google.gson.writer.Message;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

/**
 * gson演示
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2017-05-12-17:38
 */
public class GsonDemo {

  public static void main(String[] arsg) {
    Random randomNumber = new Random();
    List<Message> messageList = JsonWriterDemo.createMessages(randomNumber);
    Gson gson = new Gson();
    Type listType = new TypeToken<List<Message>>() {
    }.getType();
    String jsonMessgaelist = gson.toJson(messageList, listType);
    System.out.println(jsonMessgaelist);
    List<Message> otherMessageList = gson.fromJson(jsonMessgaelist, listType);
    for (Message message :
        otherMessageList) {
      System.out.println(message);
    }
  }

}