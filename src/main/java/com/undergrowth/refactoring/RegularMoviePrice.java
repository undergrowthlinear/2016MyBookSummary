package com.undergrowth.refactoring;

/**
 * Description: TODO(常规电影价格)
 *
 * @author <a href="undergrowth@126.com">undergrowth</a>
 * @version 1.0.0
 * @date 2016年7月8日
 */
public class RegularMoviePrice extends MoviePrice {

  @Override
  public double getPrice(int rentalDay) {
    // TODO Auto-generated method stub
    double result = 0;
    result += 2;
    if (rentalDay > 2) {
      result += (rentalDay - 2) * 1.5;
    }
    return result;
  }

}
