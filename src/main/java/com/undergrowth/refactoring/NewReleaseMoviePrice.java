package com.undergrowth.refactoring;

public class NewReleaseMoviePrice extends MoviePrice {

    @Override
    public double getPrice(int rentalDay) {
        // TODO Auto-generated method stub
        double result = 0;
        result += rentalDay * 3;
        return result;
    }

}
