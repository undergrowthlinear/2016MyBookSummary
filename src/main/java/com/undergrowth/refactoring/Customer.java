package com.undergrowth.refactoring;

import java.util.Iterator;
import java.util.Vector;

/**
 * Description: TODO(这里用一句话描述这个类的作用)
 *
 * @author <a href="undergrowth@126.com">undergrowth</a>
 * @version 1.0.0
 * @date 2016年7月8日
 */
public class Customer {

  private String name;

  private Vector<Rental> rentals = null;

  private ShowDetail showDetail = null;

  /**
   * 返回计算影片的详细明细、总价格、客户积分信息
   *
   * @return String result
   */
  public String statement() {
    showDetail.setHeader(name + " rental " + rentals.size() + " movie ,as list ");
    StringBuilder body = new StringBuilder();
    // 迭代租用影片
    for (Iterator<Rental> iterator = rentals.iterator(); iterator.hasNext(); ) {
      Rental rental = (Rental) iterator.next();
      // 计算价格
      // allMount += rental.getCharge();
      // 计算客户积分
      // frequencyRental += rental.getFrequencyRental();
      // 计算影片详细信息
      body.append(
          rental.getMovie().getTitle() + " rental " + rental.getRentalDay() + " days, amount is "
              + rental.getCharge() + "\t");
    }
    showDetail.setBody(body);
    showDetail.setTail(" all movie amount is " + getAllMount() + ",all movie frequencyRental is "
        + getFrequencyRental());
    return showDetail.toString();
  }

  public Customer(String name, Vector<Rental> rentals) {
    this(name, rentals, new ShowDetail());
  }

  public Customer(String name, Vector<Rental> rentals, ShowDetail showDetail) {
    super();
    this.name = name;
    this.rentals = rentals;
    this.showDetail = showDetail;
  }

  /**
   * 用查询替换局部变量
   *
   * @return Double allMount
   */
  private Double getAllMount() {
    double allMount = 0;
    for (Iterator<Rental> iterator = rentals.iterator(); iterator.hasNext(); ) {
      Rental rental = (Rental) iterator.next();
      allMount += rental.getCharge();
    }
    return allMount;
  }

  /**
   * 用查询替换局部变量
   *
   * @return int frequencyRental
   */
  private int getFrequencyRental() {
    int frequencyRental = 0;
    for (Iterator<Rental> iterator = rentals.iterator(); iterator.hasNext(); ) {
      Rental rental = (Rental) iterator.next();
      frequencyRental += rental.getFrequencyRental();
    }
    return frequencyRental;
  }
}
