package com.undergrowth.google.gson.adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

/**
 * 自定义适配器
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2017-05-12-16:09
 */
public class PointAdapter extends TypeAdapter<Point> {

    public Point read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        String xy = reader.nextString();
        String[] parts = xy.split(",");
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        return new Point(x, y);
    }

    public void write(JsonWriter writer, Point value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        String xy = value.getX() + "," + value.getY();
        writer.value(xy);
    }

    public static void main(String[] arsg) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Point.class, new PointAdapter());
        Gson gson = builder.create();
        Point point = new Point(100, 200);
        String jsonPoint = gson.toJson(point, Point.class);
        System.out.println(jsonPoint);
        Point otherPoint = gson.fromJson(jsonPoint, Point.class);
        System.out.println(otherPoint);
        System.out.println(otherPoint.equals(point));
    }

}
