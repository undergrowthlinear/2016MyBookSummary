package com.undergrowth.google.gson.writer;

import com.google.common.collect.Lists;
import com.google.gson.stream.JsonWriter;
import com.undergrowth.google.gson.writer.Message.User;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * JsonWriterDemo [ { "id": 912345678901, "text": "How do I stream JSON in Java?", "geo": null,
 * "user": { "name": "json_newb", "followers_count": 41 } }, { "id": 912345678902, "text":
 * "@json_newb just use JsonWriter!", "geo": [50.454722, -104.606667], "user": { "name": "jesse",
 * "followers_count": 2 } } ]
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2017-05-12-16:41
 */
public class JsonWriterDemo {

  public void writeJsonStream(OutputStream out, List<Message> messages) throws IOException {
    JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
    writer.setIndent("    ");
    writeMessagesArray(writer, messages);
    writer.close();
  }

  public void writeMessagesArray(JsonWriter writer, List<Message> messages) throws IOException {
    writer.beginArray();
    for (Message message : messages) {
      writeMessage(writer, message);
    }
    writer.endArray();
  }

  public void writeMessage(JsonWriter writer, Message message) throws IOException {
    writer.beginObject();
    writer.name("id").value(message.getId());
    writer.name("text").value(message.getText());
    if (message.getGeo() != null) {
      writer.name("geo");
      writeDoublesArray(writer, message.getGeo());
    } else {
      writer.name("geo").nullValue();
    }
    writer.name("user");
    writeUser(writer, message.getUser());
    writer.endObject();
  }

  public void writeUser(JsonWriter writer, User user) throws IOException {
    writer.beginObject();
    writer.name("name").value(user.getName());
    writer.name("followers_count").value(user.getFollowers_count());
    writer.endObject();
  }

  public void writeDoublesArray(JsonWriter writer, List<Double> doubles) throws IOException {
    writer.beginArray();
    for (Double value : doubles) {
      writer.value(value);
    }
    writer.endArray();
  }

  public static void main(String[] arsg) {
    JsonWriterDemo jsonWriterDemo = new JsonWriterDemo();
    Random randomNumber = new Random();
    List<Message> messageList = createMessages(randomNumber);
    try {
      jsonWriterDemo.writeJsonStream(System.out, messageList);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static List<Message> createMessages(Random randomNumber) {
    List<Message> messageList = Lists.newArrayList();
    User user = new User();
    user.setName(RandomStringUtils.randomAlphabetic(10));
    user.setFollowers_count(randomNumber.nextInt());
    messageList.add(
        new Message(randomNumber.nextLong(), randomNumber.nextInt(1000) + "_gson", null, user));
    messageList.add(
        new Message(randomNumber.nextLong(), randomNumber.nextInt(1000) + "_gson",
            Arrays.asList(12.34, 56.78), user));
    return messageList;
  }


}