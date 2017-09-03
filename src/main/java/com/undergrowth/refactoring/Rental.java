package com.undergrowth.refactoring;

/**
 * Description: TODO(租赁信息)
 *
 * @author <a href="undergrowth@126.com">undergrowth</a>
 * @version 1.0.0
 * @date 2016年7月8日
 */
public class Rental {

  private int rentalDay;
  private Movie movie;


  public Rental(int rentalDay, Movie movie) {
    super();
    this.rentalDay = rentalDay;
    this.movie = movie;
  }

  public int getRentalDay() {
    return rentalDay;
  }

  public void setRentalDay(int rentalDay) {
    this.rentalDay = rentalDay;
  }

  public Movie getMovie() {
    return movie;
  }

  /**
   * 根据影片类型 计算影片价格
   *
   * @return double result
   */
  public double getCharge() {
    return getMovie().getMoviePrice().getPrice(rentalDay);
  }

  /**
   * 根据影片类型 计算客户积分
   *
   * @return int
   */
  public int getFrequencyRental() {
    return getMovie().getMoviePrice().getFrequencyRental(rentalDay);
  }

}
