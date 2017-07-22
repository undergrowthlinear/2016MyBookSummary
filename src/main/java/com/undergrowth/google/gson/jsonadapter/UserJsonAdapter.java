package com.undergrowth.google.gson.jsonadapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

/**
 * json适配器
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2017-05-12-17:22
 */
public class UserJsonAdapter extends TypeAdapter<UserInfo> {

  @Override
  public void write(JsonWriter out, UserInfo user) throws IOException {
    // implement write: combine firstName and lastName into name
    out.beginObject();
    out.name("name");
    out.value(user.firstName + " " + user.lastName);
    out.endObject();
    // implement the write method
  }

  @Override
  public UserInfo read(JsonReader in) throws IOException {
    // implement read: split name into firstName and lastName
    in.beginObject();
    in.nextName();
    String[] nameParts = in.nextString().split(" ");
    in.endObject();
    return new UserInfo(nameParts[0], nameParts[1]);
  }

  public static void main(String[] arsg) {
    Gson gson = new Gson();
    UserInfo userInfo = new UserInfo("under", "last");
    String jsonUserInfo = gson.toJson(userInfo, UserInfo.class);
    System.out.println(jsonUserInfo);
    UserInfo otherUser = gson.fromJson(jsonUserInfo, UserInfo.class);
    System.out.println(otherUser.equals(userInfo));
  }
}