package com.undergrowth.refactoring;

import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

import static org.junit.Assert.assertEquals;

/**
 * 
 * Description: TODO(测试购书环节)
 * 
 * @author <a href="undergrowth@126.com">undergrowth</a>
 * @date 2016年7月8日
 * @version 1.0.0
 */
public class CustomerBookTest {

	Customer customer = null;

	@Before
	public void before() {
		Vector<Rental> rentals = new Vector<>();
		rentals.addElement(new Rental(2, new Movie("行尸走肉", new NewReleaseMoviePrice())));
		rentals.addElement(new Rental(3, new Movie("大头儿子", new ChildrenMoivePrice())));
		rentals.addElement(new Rental(2, new Movie("生化危机", new RegularMoviePrice())));
		customer = new Customer("under", rentals);
	}

	// @Test
	public void testStetementInit() {
		// System.out.println(customer.statement());;
		assertEquals("under rental 0 movie ,as list:	 all movie amount is 0.0,all movie frequencyRental is 0",
				customer.statement());
	}

	@Test
	public void testStetementOThree() {
		// System.out.println(customer.statement());;
		assertEquals(
				"ShowDetail [header=under rental 3 movie ,as list , body=行尸走肉 rental 2 days, amount is 6.0	大头儿子 rental 3 days, amount is 1.5	生化危机 rental 2 days, amount is 2.0	, tail= all movie amount is 9.5,all movie frequencyRental is 4]",
				customer.statement());
	}

}
