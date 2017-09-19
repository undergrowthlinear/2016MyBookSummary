package com.undergrowth.java8.forimpatient.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description Stream是处理集合的关键抽象 A sequence of elements supporting sequential and parallel aggregate
 * operations. Stream自己不会存储元素 元素存储在底层集合或者根据需求产生出来 Stream操作符不会改变源对象 返回持有新结果的Stream对象 Stream操作可延迟执行
 * 使用Stream通过三阶段构建流水线操作 创建一个Stream 将Stream进行转换 流转换是指从一个流中读取数据，并将转换后的数据写入到另一个流中 用终止操作符产生结果
 * Optional<T>---->对象或者是对一个T类型对象的封装 收集结果---->collect 一个能够创建目标类型实例的方法 一个能够将元素添加到目标中的方法
 * 一个将两个对象整合到一起的方法
 * @date 2017-01-22-21:37
 */
public class StreamLearnTest {

  List<String> words = null;

  @Before
  public void before() throws URISyntaxException, IOException {
    String contents = new String(Files.readAllBytes(
        Paths.get(StreamLearnTest.class.getClassLoader().getResource("war-and-peace.txt").toURI())),
        StandardCharsets.UTF_8);
    words = Arrays.asList(contents.split("[\\P{L}]+"));
  }

  private void iteratorStream(Stream<String> wordsStream) {
    Iterator<String> iterator = wordsStream.iterator();
    while (iterator.hasNext()) {
      System.out.println(iterator.next());
    }
  }

  @Test
  public void countNumTest() throws IOException, URISyntaxException {
    int count = 0;
    for (String w :
        words) {
      if (w.length() > 12) {
        count++;
      }
    }
    System.out.println(count);
  }

  @Test
  public void countNumStreamTest() throws IOException, URISyntaxException {
    long count = words.stream().filter(w -> w.length() > 12).count();
    System.out.println(count);
    count = words.parallelStream().filter(w -> w.length() > 12).count();
    System.out.println(count);
  }


  @Test
  public void createStream() {
    Stream<String> song = Stream.of("gently", "down", "the");
    Stream<String> silence = Stream.empty();
    Stream<String> echo = Stream.generate(() -> "echo");
    iteratorStream(echo);
  }

  @Test
  public void mapStreamTest() {
    Stream<String> lowercaseWords = words.stream().map(String::toLowerCase);
    iteratorStream(lowercaseWords);
  }


  @Test
  public void limitStreamTest() {
    Stream<String> limitStream = Stream.generate(() -> "echo").limit(100);
    iteratorStream(limitStream);
    Object[] objects = Stream.iterate(1.0, p -> p * 2)
        .peek(e -> System.out.println("Fetching-->" + e)).limit(20).toArray();
    for (Object object :
        objects) {
      System.out.println(object);
    }
    Double[] strings = Stream.iterate(1.0, p -> p * 2)
        .peek(e -> System.out.println("Fetching-->" + e)).limit(20).toArray(Double[]::new);
    for (Double str :
        strings) {
      System.out.println(str);
    }
  }

  @Test
  public void optionalTest() {
    Optional<String> largest = words.stream().max(String::compareToIgnoreCase);
    if (largest.isPresent()) {
      System.out.println(largest.get());
    }
    Optional<String> startWithQ = words.stream().filter(w -> w.startsWith("Q")).findFirst();
    if (startWithQ.isPresent()) {
      System.out.println(startWithQ.get());
    }
  }

  @Test
  public void reduceTest() {
    int result = words.stream()
        .reduce(0, (total, word) -> total + word.length(), (total1, total2) -> total1 + total2);
    System.out.println(result);
  }

  @Test
  public void collectTest() {
    List<String> list = words.stream().collect(Collectors.toList());
    iterator(list);
    words.stream().forEach(System.out::println);
  }

  private void iterator(Collection<String> collection) {
    for (String str :
        collection) {
      System.out.println(str);
    }
  }


  class Person {

    private String name;
    private int age;
    private int salary;
    private Department department;
    private int grade;

    public Person(String name, int age) {
      this.name = name;
      this.age = age;
    }

    public Person(String name, int age, int salary) {
      this.name = name;
      this.age = age;
      this.salary = salary;
    }

    public Person(String name, int age, int salary, Department department) {
      this.name = name;
      this.age = age;
      this.salary = salary;
      this.department = department;
    }

    public Person(String name, int age, int salary, Department department, int grade) {
      this.name = name;
      this.age = age;
      this.salary = salary;
      this.department = department;
      this.grade = grade;
    }

    public Person() {
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }

    public int getSalary() {
      return salary;
    }

    public void setSalary(int salary) {
      this.salary = salary;
    }

    public Department getDepartment() {
      return department;
    }

    public void setDepartment(Department department) {
      this.department = department;
    }

    public int getGrade() {
      return grade;
    }

    public void setGrade(int grade) {
      this.grade = grade;
    }
  }

  class Department {

    private String deptName;

    public Department(String deptName) {
      this.deptName = deptName;
    }

    public String getDeptName() {
      return deptName;
    }

    public void setDeptName(String deptName) {
      this.deptName = deptName;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Department)) {
        return false;
      }

      Department that = (Department) o;

      return getDeptName().equals(that.getDeptName());

    }

    @Override
    public int hashCode() {
      return getDeptName().hashCode();
    }
  }

  @Test
  public void collectorsTest() {
    List<Person> persons = Arrays.asList(new Person("under", 18, 100, new Department("1号")),
        new Person("growth", 25, 500, new Department("1号")),
        new Person("qqwwee", 30, 1000, new Department("2号")));
    //转换list
    System.out.println("转换list");
    List<String> list = persons.stream().map(Person::getName).collect(Collectors.toList());
    iterator(list);
    //转换TreeSet
    System.out.println("转换TreeSet");
    Set<String> set = persons.stream().map(Person::getName)
        .collect(Collectors.toCollection(TreeSet::new));
    iterator(list);
    //转换元素用连接符
    System.out.println("转换元素用连接符");
    String joined = persons.stream().map(Object::toString).collect(Collectors.joining(", "));
    System.out.println(joined);
    //计算薪资
    System.out.println("计算薪资");
    int total = persons.stream().collect(Collectors.summingInt(Person::getSalary));
    System.out.println(total);
  }

  @Test
  public void collectors2Test() {
    List<Person> persons = Arrays.asList(new Person("under", 18, 100, new Department("1号"), 50),
        new Person("growth", 25, 500, new Department("1号"), 80),
        new Person("qqwwee", 30, 1000, new Department("2号"), 20));
    Map<Department, List<Person>> byDept = persons.stream()
        .collect(Collectors.groupingBy(Person::getDepartment));
    System.out.println("计算部门人员");
    for (Map.Entry<Department, List<Person>> departPersonEntry :
        byDept.entrySet()) {
      System.out.println(
          departPersonEntry.getKey().getDeptName() + "---->" + departPersonEntry.getValue().size());
    }
    //计算部门薪水
    System.out.println("计算部门薪水");
    Map<Department, Integer> totalByDept = persons.stream().collect(
        Collectors.groupingBy(Person::getDepartment, Collectors.summingInt(Person::getSalary)));
    for (Map.Entry<Department, Integer> departSalary :
        totalByDept.entrySet()) {
      System.out.println(departSalary.getKey().getDeptName() + "---->" + departSalary.getValue());
    }
    //按照成绩好坏分区
    System.out.println("按照成绩好坏分区");
    Map<Boolean, List<Person>> passingFailing = persons.stream()
        .collect(Collectors.partitioningBy(s -> s.getGrade() >= 60));
    for (Map.Entry<Boolean, List<Person>> departSalary :
        passingFailing.entrySet()) {
      System.out.println(departSalary.getKey() + "---->" + departSalary.getValue().size());
    }
  }


}
