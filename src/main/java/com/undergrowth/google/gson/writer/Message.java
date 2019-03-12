package com.undergrowth.google.gson.writer;

import java.util.List;

/**
 * 消息
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2017-05-12-16:43
 */
public class Message {

    private Long id;
    private String text;
    private List<Double> geo;
    private User user;

    public Message() {
    }

    public Message(Long id, String text, List<Double> geo,
        User user) {
        this.id = id;
        this.text = text;
        this.geo = geo;
        this.user = user;
    }

    public List<Double> getGeo() {
        return geo;
    }

    public void setGeo(List<Double> geo) {
        this.geo = geo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

  /*public static class Geo {

    private Double lat;
    private Double lon;

    public Double getLat() {
      return lat;
    }

    public void setLat(Double lat) {
      this.lat = lat;
    }

    public Double getLon() {
      return lon;
    }

    public void setLon(Double lon) {
      this.lon = lon;
    }
  }*/

    public static class User {

        private String name;
        private Integer followers_count;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getFollowers_count() {
            return followers_count;
        }

        public void setFollowers_count(Integer followers_count) {
            this.followers_count = followers_count;
        }
    }

    @Override
    public String toString() {
        return "Message{" +
            "id=" + id +
            ", text='" + text + '\'' +
            ", geo=" + geo +
            ", user=" + user +
            '}';
    }
}