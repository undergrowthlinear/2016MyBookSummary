package com.undergrowth.refactoring;

/**
 * 
* Description: TODO(这里用一句话描述这个类的作用)
* @author <a href="undergrowth@126.com">undergrowth</a>
* @date 2016年7月8日
* @version 1.0.0
 */
public class ChildrenMoivePrice extends MoviePrice {

	@Override
	public double getPrice(int rentalDay) {
		// TODO Auto-generated method stub
		double result=0;
		result += 1.5;
		if (rentalDay > 3) {
			result += (rentalDay - 3) * 1.5;
		}
		return result;
	}

}
