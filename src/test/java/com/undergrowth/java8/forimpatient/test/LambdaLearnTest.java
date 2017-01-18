package com.undergrowth.java8.forimpatient.test;

import com.undergrowth.java8.forimpatient.JavaFxHelloWorld;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description Lambda函数式编程
 * lambda是一段可以传递的代码
 * lambda表示参数,三部分组成
 * 参数---->(参数类型 参数名...)
 *      ---->当参数类型可推导时,参数类型可省略
 *      ---->当只有一个参数时,()可省略
 * 箭头(->)
 * 表达式---->3部分组成
 *      ---->一段代码
 *      ---->参数
 *      ---->自由变量的值(不是参数且没有在变量中定义的变量/自由变量的代码块称为闭包/被引用的自由变量的值不可更改,类似于内部类的final)
 * ---->当在lambda中使用this关键字时,引用的是创建该lambda的方法的this参数
 * 函数式接口---->该接口只包含一个抽象方法,可通过lambda表达式创建该接口的对象(类似于匿名对象)
 * 函数式接口的转换是Java中使用Lambda表达式唯一能做的事情
 * Lambda表达式转换为函数式接口实例时，需注意受检异常
 * 方法引用---->使用::简化方法的引用,当已有实现的方法想进行传递时,可使用方法引用
 *          ---->对象::实例方法
 *          ---->类::静态方法
 *          ---->类::实例方法
 *  构造器引用---->构造器引用的方法名为::new
 *  默认方法---->允许接口带有具体的实现/类优先(优先考虑父类实现)
 *  静态方法---->接口中添加静态方法
 * @date 2017-01-18-21:28
 */
public class LambdaLearnTest {

	class Worker implements Runnable {

		/**
		 * When an object implementing interface <code>Runnable</code> is used
		 * to create a thread, starting the thread causes the object's
		 * <code>run</code> method to be called in that separately executing
		 * thread.
		 * <p>
		 * The general contract of the method <code>run</code> is that it may
		 * take any action whatsoever.
		 *
		 * @see Thread#run()
		 */
		@Override
		public void run() {
			for (int i = 0; i < 100; i++) {
				doWorker(i);
			}
		}

		private void doWorker(int i) {
			System.out.println("Lambda HelloWorld " + i);
		}
	}


	class LengthComparator implements Comparator<String> {

		/**
		 * Compares its two arguments for order.  Returns a negative integer,
		 * zero, or a positive integer as the first argument is less than, equal
		 * to, or greater than the second.<p>
		 * <p>
		 * In the foregoing description, the notation
		 * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
		 * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
		 * <tt>0</tt>, or <tt>1</tt> according to whether the value of
		 * <i>expression</i> is negative, zero or positive.<p>
		 * <p>
		 * The implementor must ensure that <tt>sgn(compare(x, y)) ==
		 * -sgn(compare(y, x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
		 * implies that <tt>compare(x, y)</tt> must throw an exception if and only
		 * if <tt>compare(y, x)</tt> throws an exception.)<p>
		 * <p>
		 * The implementor must also ensure that the relation is transitive:
		 * <tt>((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))</tt> implies
		 * <tt>compare(x, z)&gt;0</tt>.<p>
		 * <p>
		 * Finally, the implementor must ensure that <tt>compare(x, y)==0</tt>
		 * implies that <tt>sgn(compare(x, z))==sgn(compare(y, z))</tt> for all
		 * <tt>z</tt>.<p>
		 * <p>
		 * It is generally the case, but <i>not</i> strictly required that
		 * <tt>(compare(x, y)==0) == (x.equals(y))</tt>.  Generally speaking,
		 * any comparator that violates this condition should clearly indicate
		 * this fact.  The recommended language is "Note: this comparator
		 * imposes orderings that are inconsistent with equals."
		 *
		 * @param o1 the first object to be compared.
		 * @param o2 the second object to be compared.
		 * @return a negative integer, zero, or a positive integer as the
		 * first argument is less than, equal to, or greater than the
		 * second.
		 * @throws NullPointerException if an argument is null and this
		 *                              comparator does not permit null arguments
		 * @throws ClassCastException   if the arguments' types prevent them from
		 *                              being compared by this comparator.
		 */
		@Override
		public int compare(String o1, String o2) {
			return Integer.compare(o1.length(), o2.length());
		}
	}


	@Test
	public void workTest() throws InterruptedException {
		Worker worker = new Worker();
		new Thread(worker).start();
		Thread.sleep(1000);
	}


	@Test
	public void lengthComparatorTest() {
		String[] countryArrayString = {"china", "us", "russian"};
		Arrays.sort(countryArrayString, new LengthComparator());
		outArray(countryArrayString);
	}

	@Test
	public void lengthComparatorLambdaTest() {
		String[] countryArrayString = {"china", "us", "russian"};
		Comparator<String> lengthCompa = (first, second) -> Integer.compare(first.length(), second.length());
		Arrays.sort(countryArrayString, lengthCompa);
		outArray(countryArrayString);
	}

	private void outArray(String[] countryArrayString) {
		for (String country :
				countryArrayString) {
			System.out.println(country);
		}
	}

	@Test
	public void javaFxTest() {
		JavaFxHelloWorld.main(null);
	}

	@Test
	public void methodRefTest(){
		String[] countryArrayString = {"china", "us", "russian"};
		Arrays.sort(countryArrayString,String::compareToIgnoreCase);
		outArray(countryArrayString);
	}


	/**
	 * 方法引用
	 */
	class Greeter{
		public void greet(){
			System.out.println(Greeter.class+"\t"+Thread.currentThread().getName());
			System.out.println("Hello World");
		}
	}

	class ConcurrentGreeter extends Greeter{
		@Override
		public void greet() {
			System.out.println(ConcurrentGreeter.class+"\t"+Thread.currentThread().getName());
			new Thread(super::greet).start();
		}
	}

	@Test
	public void methodRef2Test() throws InterruptedException {
		Greeter greeter=new ConcurrentGreeter();
		greeter.greet();
		Thread.sleep(1000);
	}

	/**
	 * 默认方法
	 * 优先考虑父类实现
	 */
	abstract class Person{
		abstract  long getId();
		public String getName(){ return "Hello Default Method";}
	}
	interface Named{
		default String getName(){ return "Named";}
	}
	class Student extends Person implements Named{

		@Override
		long getId() {
			return 0;
		}
	}

	@Test
	public void  defaultMethodTest(){
		Student student=new Student();
		System.out.println(student.getName());
	}


	@Test
	public void staticMethodTest(){
		System.out.println(Paths.get("hello","world").toString());
	}


}
