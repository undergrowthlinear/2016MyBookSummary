package com.undergrowth.reflect;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description 类型测试
 * @date 2017-08-20-14:17
 */
public class TypeAll {

  public static void main(String[] args) {

    Integer a = 6;
    displayType(a.getClass(), Integer.class.toString());
    List rawList = new ArrayList();
    displayType(rawList.getClass(), rawList.getClass().toString());
    List<String> pyList = new ArrayList<>();
    displayType(pyList.getClass(), pyList.getClass().toString());
    GereType<String>[] arrayPt = new GereType[10];
    displayType(arrayPt.getClass(), arrayPt.getClass().toString());

  }

  class GereType<T> {

    private T t;

    public GereType(T t) {
      this.t = t;
    }

    public T getT() {
      return t;
    }
  }

  public static void displayType(Type t, String prefix) {
    System.out.println(prefix);
    System.out.println(t.getTypeName());
    if (t instanceof Class){
      System.out.println("--------Class--------start");
      Class aClass=((Class) t);
      System.out.println(aClass.getTypeName());
      System.out.println("--------Class--------end");
    }
    if (t instanceof ParameterizedType){
      System.out.println("--------ParameterizedType--------start");
      ParameterizedType parameterizedType=((ParameterizedType) t);
      System.out.println(parameterizedType.getRawType().getTypeName()+"---->"+parameterizedType.getOwnerType().getTypeName());
      Type[] ptArray=parameterizedType.getActualTypeArguments();
      for (Type tItem:
      ptArray) {
        System.out.println(tItem.getTypeName());
      }
      System.out.println("--------ParameterizedType--------end");
    }
    if (t instanceof GenericArrayType){
      System.out.println("--------GenericArrayType--------start");
      GenericArrayType genericArrayType=((GenericArrayType) t);
      System.out.println(genericArrayType.getGenericComponentType().getTypeName());
      System.out.println("--------GenericArrayType--------end");
    }
  }

}
