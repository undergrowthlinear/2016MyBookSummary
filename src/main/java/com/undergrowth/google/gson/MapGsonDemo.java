package com.undergrowth.google.gson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.undergrowth.google.gson.writer.Message;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mapgson
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-09-18-17:09
 */
public class MapGsonDemo {

    public static void main(String[] arsg) {
        Map<String,Object> map=new HashMap<String,Object>(){
            {
                put("1",2);
            }
        };
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String,Object>>() {
        }.getType();
        System.out.println(gson.toJson(map,mapType));
    }
}