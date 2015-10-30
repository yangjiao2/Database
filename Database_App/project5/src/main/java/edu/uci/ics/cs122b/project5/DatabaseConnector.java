package edu.uci.ics.cs122b.project5;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.Cursor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Kunal on 3/11/2015.
 */
public class DatabaseConnector extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "fade";
    private static final String DATABASE_NAME ="moviedb";
    private static final int DATABASE_VERSION = 1;


    private Context context;
    private SQLiteDatabase d;


    public DatabaseConnector(Context c){
        super(c,DATABASE_NAME,null,DATABASE_VERSION);
        System.out.println("Inside connector constructor");
        context = c;
        this.d = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase database){
        System.out.println("Inside create");
        database.execSQL("CREATE TABLE movies (id integer primary key autoincrement, title text not null, year integer not null, director text not null, banner_url text not null, trailer_url text not null);");
        database.execSQL("CREATE TABLE stars (id integer primary key autoincrement, first_name text not null, last_name integer not null, dob text not null, photo_url text not null);");
        database.execSQL("CREATE TABLE stars_in_movies (star_id integer, movie_id integer, FOREIGN KEY(star_id) REFERENCES stars(id), FOREIGN KEY(movie_id) REFERENCES movies(id));");

        try{
            BufferedReader buffer = new BufferedReader(new InputStreamReader(context.getAssets().open("movies.csv")));
            String line = "";
            String table = "movies";
            String columns = "id,title,year,director,banner_url,trailer_url";
            String query = "INSERT INTO "+table+" ("+columns+") VALUES (";

            database.beginTransaction();
            while((line = buffer.readLine()) != null){
                StringBuilder build = new StringBuilder(query);
                String[] s = line.split(";");
                build.append(s[0] + ",");
                build.append(s[1] + ",");
                build.append(s[2] + ",");
                build.append(s[3] + ",");
                build.append(s[4] + ",");
                build.append(s[5]);
                build.append(");");
                System.out.println(build.toString());
                database.execSQL(build.toString());
            }
            database.setTransactionSuccessful();
            database.endTransaction();


            buffer = new BufferedReader(new InputStreamReader(context.getAssets().open("stars.csv")));
            table = "stars";
            columns = "id,first_name,last_name,dob,photo_url";
            query = "INSERT INTO "+table+" ("+columns+") VALUES (";

            database.beginTransaction();
            while((line = buffer.readLine()) != null){
                StringBuilder build = new StringBuilder(query);
                String[] s = line.split(";");
                build.append(s[0] + ",");
                build.append(s[1] + ",");
                build.append(s[2] + ",");
                build.append(s[3] + ",");
                build.append(s[4]);
                build.append(");");
                System.out.println(build.toString());
                database.execSQL(build.toString());
            }
            database.setTransactionSuccessful();
            database.endTransaction();


            buffer = new BufferedReader(new InputStreamReader(context.getAssets().open("stars_in_movies.csv")));
            table = "stars_in_movies";
            columns = "star_id,movie_id";
            query = "INSERT INTO "+table+" ("+columns+") VALUES (";
            database.beginTransaction();
            while((line = buffer.readLine()) != null) {
                StringBuilder build = new StringBuilder(query);
                String[] s = line.split(";");
                build.append(s[0] + ",");
                build.append(s[1]);
                build.append(");");
                System.out.println(build.toString());
                database.execSQL(build.toString());
            }
            database.setTransactionSuccessful();
            database.endTransaction();
        }
        catch (IOException e){

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public Cursor getResults(String sql){
        SQLiteDatabase data = this.getReadableDatabase();
        return data.rawQuery(sql,null);
    }

    public void close(){
        d.close();
    }
}

