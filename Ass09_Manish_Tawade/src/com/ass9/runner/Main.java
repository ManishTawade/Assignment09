package com.ass9.runner;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import com.ass9.Movie.*;
import com.ass9.Movie.Movie.Language;
import com.ass9.service.*;
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File f=new File("D:\\JavaAssignments\\Ass09_Manish_Tawade\\MoviesDetails.txt");
		MovieService ms=new MovieService();
		List<Movie> ls=ms.populateMovies(f);
		for (Movie movie : ls) {
			System.out.println(movie.toString());
		}
		System.out.println();
		
		if(ms.allAllMoviesInDb(ls))
			System.out.println("Added to Database");
		else
			System.out.println("Failed Adding Data to Database");
		
		List<String> l=new ArrayList<>();
		l.add("Johnny Depp");
		l.add("Keira Knightley");
		l.add("Orlando Bloom");
		
		Movie m=new Movie(9,"Pirates Of Caribbean","UA","English",Date.valueOf("2016-4-21"),l,5.6,500000.0) ;
		ms.addMovie(m,ls);
		System.out.println();
		//ls=ms.getMovieData();
		for (Movie movie : ls) {
			System.out.println(movie.toString());
		}
		
		System.out.println("\nSerialization-");
		ms.serializeMovies(ls, "serialize");
		
		System.out.println("\nDeserialization-");
		List<Movie> ml=ms.deserializeMovies("serialize");
		System.out.println("\nDeserialized List:");
		System.out.println();
		for (Movie movie : ls) {
			System.out.println(movie.toString());
		}
		
		System.out.println("\nMovies Released in year 2012-");
		List<Movie> year=ms.getMoviesReleasedInYear(2012);
		System.out.println();
		for (Movie movie : year) {
			System.out.println(movie.toString());
		}
		
		String[] actors=new String[] {"Amir Khan","Johnny Depp","Raj"};
		System.out.println("\nMovies With Similar Actors-");
		for (String string : actors) {
			System.out.print(string+",");
		}
		System.out.println();
		List<Movie> act=ms.getMoviesByActor(actors);
		System.out.println();
		for (Movie movie : act) {
			System.out.println(movie.toString());
		}
		
		System.out.println("\nUpdate Ratings in Pirates of Caribbean:");
		ms.updateRatings(m, 9.5, ls);
		System.out.println();
		for (Movie movie : ls) {
			if(m.getMovieId()==movie.getMovieId())
				System.out.println(movie.toString());
		}
		
		System.out.println("\nUpdate Total Business in Pirates of Caribbean:");
		ms.updateBusiness(m, 550000, ls);
		System.out.println();
		for (Movie movie : ls) {
			if(m.getMovieId()==movie.getMovieId())
				System.out.println(movie.toString());
		}
		
		
		System.out.println("\nMovies Having Business Greater than 5000\n");
		Map<Language, LinkedHashSet<Movie>> langToMovie = ms.businessDone(5000);
		ms.displayLangToMovieMap(langToMovie);
	}
	
}
