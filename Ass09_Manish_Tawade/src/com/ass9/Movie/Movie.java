package com.ass9.Movie;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class Movie implements Serializable{
	public enum Category{U,UA,A;}
	public enum Language{HINDI,ENGLISH,MARATHI;}
	
	private int movieId;
	private String movieName; 
	private Category movieType;
	private Language language; 
	private Date releaseDate; 
	private List<String> casting;
	private Double rating;
	private Double totalBusinessDone;
	
	
	public int getMovieId() {
		return movieId;
	}
	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}
	public String getMovieName() {
		return movieName;
	}
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
	public Category getMovieType() {
		return movieType;
	}
	public void setMovieType(String movieType) {
		this.movieType = Category.valueOf(movieType);
	}
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = Language.valueOf(language.toUpperCase());
	}
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	public List<String> getCasting() {
		return casting;
	}
	public void setCasting(List<String> casting) {
		this.casting = casting;
	}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
	public Double getTotalBusinessDone() {
		return totalBusinessDone;
	}
	public void setTotalBusinessDone(Double totalBusinessDone) {
		this.totalBusinessDone = totalBusinessDone;
	}
	@Override
	public String toString() {
		return "Movie [movieId=" + movieId + ", movieName=" + movieName + ", movieType=" + movieType + ", language="
				+ language + ", releaseDate=" + releaseDate + ", casting=" + casting + ", rating=" + rating
				+ ", totalBusinessDone=" + totalBusinessDone + "K]";
	}
	public Movie(int movieId, String movieName, String movieType, String language, Date releaseDate,
			List<String> casting, Double rating, Double totalBusinessDone) {
		super();
		this.movieId = movieId;
		this.movieName = movieName;
		this.movieType = Category.valueOf(movieType);
		this.language = Language.valueOf(language.toUpperCase());
		this.releaseDate = releaseDate;
		this.casting = casting;
		this.rating = rating;
		this.totalBusinessDone = totalBusinessDone;
	}
	public Movie() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}
