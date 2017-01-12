package com.undergrowth.refactoring;

/**
 * 
 * Description: TODO(电影)
 * 
 * @author <a href="undergrowth@126.com">undergrowth</a>
 * @date 2016年7月8日
 * @version 1.0.0
 */
public class Movie {


	private String title;

	private MoviePrice moviePrice;

	public Movie(String title, MoviePrice moviePrice) {
		super();
		this.title = title;
		this.moviePrice = moviePrice;
	}

	public MoviePrice getMoviePrice() {
		return moviePrice;
	}

	public void setMoviePrice(MoviePrice moviePrice) {
		this.moviePrice = moviePrice;
	}

	public String getTitle() {
		return title;
	}

}
