package edu.uci.ics.cs122b.project5;

/**
 * Created by Kunal on 3/11/2015.
 */
public class Movie {

    private int id;
    private String title;
    private String director;
    private int year;

    public Movie(int id, String title,int year, String director){
        this.id = id;
        this.title = title;
        this.director = director;
        this.year = year;
    }

    public void setID(int id){
        this.id = id;
    }

    public int getID(){
        return id;
    }

    public void setTitle(String title){
        this.title = title;

    }

    public String getTitle(){
        return title;
    }

    public void setYear(int year){
        this.year = year;
    }

    public int getYear(){
        return year;
    }

    public void setDirector(String director){
        this.director = director;
    }

    public String getDirector(){
        return director;
    }
}

