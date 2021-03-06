package com.ass9.service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

import com.ass9.DBConnect.BDConnect;
import com.ass9.Movie.*;
import com.ass9.Movie.Movie.Category;
import com.ass9.Movie.Movie.Language;


public class MovieService {
	Connection c=BDConnect.getConnection();
	public List<Movie> populateMovies(File file){
		List<Movie> ls=new ArrayList<>();
		Scanner sc;
		try {
			sc = new Scanner(file);
		
			while(sc.hasNextLine()) {
				String s[]=sc.nextLine().split(",");
				Movie m=new Movie();
				m.setMovieId(Integer.parseInt(s[0]));
				m.setMovieName(s[1]);
				m.setLanguage(s[2]);
				m.setReleaseDate(Date.valueOf(s[3]));
				m.setMovieType(s[4]);
				List<String> l=new ArrayList<>();
					String[] ss=s[5].split("-");
					for(int i=0;i<ss.length;i++) {
						l.add(ss[i]);
					}
					
				m.setCasting(l);
				m.setRating(Double.parseDouble(s[6]));
				m.setTotalBusinessDone(Double.parseDouble(s[7]));
				ls.add(m);
			}
		} catch (FileNotFoundException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ls;
	}
	
	public boolean allAllMoviesInDb(List<Movie> movies) {
		try {	
			
			//String sql="create table MovieData (MovieID number(10) PRIMARY KEY,MovieName varchar2(20),MovieType varchar2(2) check(MovieType in ('U','A','UA')),Language varchar2(10),ReleaseDate Date, Casting varchar2(50),Rating BINARY_DOUBLE,TotalBusiness BINARY_DOUBLE)";
			PreparedStatement	pstmt = c.prepareStatement("insert into MovieData values(?,?,?,?,?,?,?,?)");
			int n=0;
			for (Movie movie : movies) {
				pstmt.setInt(1, movie.getMovieId());
				pstmt.setString(2, movie.getMovieName());
				pstmt.setString(3, movie.getMovieType().toString());
				pstmt.setString(4, movie.getLanguage().toString());
				pstmt.setDate(5, movie.getReleaseDate());
				pstmt.setString(6,ListToString(movie.getCasting()));
				pstmt.setDouble(7, movie.getRating());
				pstmt.setDouble(8, movie.getTotalBusinessDone());
				pstmt.executeUpdate();
				n++;
			}
			if(n==movies.size())
				return true;
			
			
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	 
	public String ListToString(List<String> ls) {
		String s="";
		String delimiter = ",";
		for (String string : ls) {
		    s += s.equals("") ? string : delimiter + string;
		}
		return s;
	}
	
	public void addMovie(Movie movie,List<Movie> ls) {
		PreparedStatement pstmt;
		try {
			pstmt = c.prepareStatement("insert into MovieData values(?,?,?,?,?,?,?,?)");
		
			pstmt.setInt(1, movie.getMovieId());
			pstmt.setString(2, movie.getMovieName());
			pstmt.setString(3, movie.getMovieType().toString());
			pstmt.setString(4, movie.getLanguage().toString());
			pstmt.setDate(5, movie.getReleaseDate());
			pstmt.setString(6,ListToString(movie.getCasting()));
			pstmt.setDouble(7, movie.getRating());
			pstmt.setDouble(8, movie.getTotalBusinessDone());
			pstmt.executeUpdate();
			ls.add(movie);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("New Movie Inserted.....");
	}
	
	public void serializeMovies(List<Movie> movies, String fileName) {
		File file = new File(fileName);
		
		try (FileOutputStream fos = new FileOutputStream(file);
				ObjectOutputStream out = new ObjectOutputStream(fos)){
			
			out.writeObject(movies);
			System.out.println("\nSerialization Complete...");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Movie> deserializeMovies(String fileName) {
		File file = new File(fileName);
		List<Movie> movies = new ArrayList<>();
		
		try(FileInputStream fis = new FileInputStream(file);
				ObjectInputStream in = new ObjectInputStream(fis)) {
	
			movies = (ArrayList<Movie>) in.readObject();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\nDeserialization Complete...");
		return movies;
	}
	
	public List<Movie> getMoviesReleasedInYear(int year) {
		List <Movie> ls = new ArrayList<>();
		Statement stmt;
			try {
				stmt = c.createStatement();
				String sql = "select * from MovieData where to_char(ReleaseDate, 'yyyy') = '"+year+"'";
				ResultSet rs = stmt.executeQuery(sql);
				ls=getMovieData(rs);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return ls;
	}
	
	public List<String> StringToList(String s) {
		List<String> l=new ArrayList<>();
		String[] ss=s.split(",");
		for (String string : ss) {
			l.add(string);
		}
		return l;
	}
	
	public List<Movie> getMoviesByActor(String ... actorNames) {
		String[] s=actorNames;
		List<Movie> ll=new ArrayList<>();
		List<Movie> ls=getMovieData();
		for (Movie movie : ls) {
			List<String> l=movie.getCasting();
			for(int i=0;i<s.length;i++)
				if(l.contains(s[i]))
					ll.add(movie);
		}
	
		return ll;
	}
	
	public List<Movie> getMovieData(ResultSet rs){
		List<Movie> ls=new ArrayList<>();
			Movie m=null;
			try {
				while(rs.next()) {
					m=new Movie();
					m.setMovieId(rs.getInt(1));
					m.setMovieName(rs.getString(2));
					m.setMovieType(rs.getString(3));
					m.setLanguage(rs.getString(4));
					m.setReleaseDate(rs.getDate(5));
					m.setCasting(StringToList(rs.getString(6)));
					m.setRating(rs.getDouble(7));
					m.setTotalBusinessDone(rs.getDouble(8));
					ls.add(m);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return ls;
	}
	
	public List<Movie> getMovieData(){
		List<Movie> ls=new ArrayList<>();
		Statement stmt;
		try {
			stmt = c.createStatement();
	
			String sq = "select * from MovieData";
			ResultSet rs = stmt.executeQuery(sq);
			Movie m=null;
			while(rs.next()) {
				m=new Movie();
				m.setMovieId(rs.getInt(1));
				m.setMovieName(rs.getString(2));
				m.setMovieType(rs.getString(3));
				m.setLanguage(rs.getString(4));
				m.setReleaseDate(rs.getDate(5));
				m.setCasting(StringToList(rs.getString(6)));
				m.setRating(rs.getDouble(7));
				m.setTotalBusinessDone(rs.getDouble(8));
				ls.add(m);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ls;
	}
	
	public void updateRatings(Movie m, double rating, List<Movie> ls) {
		for(Movie movie : ls) {
			if (m.equals(movie)) {
				movie.setRating(rating);
			}
		}
	}
	
	public void updateBusiness(Movie m, double amt, List<Movie> ls) {
		for (Movie movie : ls) {
			if (m.equals(movie)) {
				movie.setTotalBusinessDone(amt);
			}
		}
	}

	public Map<Language, LinkedHashSet<Movie>> businessDone (double amount) {
		Map <Language, LinkedHashSet<Movie>> langToMovie = new LinkedHashMap<> ();
		
		String sql = "select * from MovieData where TotalBusiness > ? order by TotalBusiness desc";
		try {
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setDouble(1, amount);
			ResultSet rs = prep.executeQuery();
			
			List<Movie> movies = getMovieData(rs);
			
			for (Movie movie : movies) {
				Language curLang = movie.getLanguage();
				
				if (langToMovie.containsKey(curLang)) {
					langToMovie.get(curLang).add(movie);
				} else {
					LinkedHashSet<Movie> curSet = new LinkedHashSet<>();
					curSet.add(movie);
					langToMovie.put(movie.getLanguage(), curSet);
				}
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return langToMovie;
	}
	
	public void displayLangToMovieMap(Map<Language, LinkedHashSet<Movie>> langToMovie) {
		for (Language lang : langToMovie.keySet()) {
			System.out.println(lang + " = ");
			for (Movie movie : langToMovie.get(lang)) {
				System.out.println(movie);
			}
			System.out.println("-");
		}
	}
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		BDConnect.closeConnection();
		super.finalize();
	}
}
