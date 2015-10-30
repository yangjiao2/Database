import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class Main {

	/**
	 * @param args
	 * @throws Exception 
	 * @throws IllegalAccessException 
	 * @throws  
	 */
	
	public static Scanner in;
	public static String username;
	public static String password;
	public static String db;
	public static void main(String[] args) throws Exception {
	
		
		//Incorporate mySql Driver
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		
		in = new Scanner(System.in);
		
		
		Connection connection = null;
		connection = DriverManager.getConnection("jdbc:mysql:///moviedb","root","fourdelta65");
		boolean connected = false;
		boolean quit = false;
		while(!connected && !quit){
			
				System.out.println("Please connect to the back-end");
				System.out.println("Username:");
				username = in.nextLine();
				System.out.println("Password:");
				password = in.nextLine();
				
				String loginQuery = "select count(*) from employees where email = '" + username+"' and password = '"
				+ password +"';";
				
				PreparedStatement p = connection.prepareStatement(loginQuery);
				ResultSet r = p.executeQuery();
				r.beforeFirst();
				r.next();
				if(r.getInt(1) == 1){
					connected = true;
					System.out.println("logged in");
					System.out.println("Welcome to the FabFlix backend");
					System.out.println("Type help for a list of acceptable commands");
				}
				else{
					System.out.println("Invalid id/pass combination, try again");
				}
				
				
			
			/*catch(SQLException e){
				if(e.getErrorCode() == 1049){
					System.out.println("Database not Found");
				}
				else if(e.getErrorCode() == 1045){
					System.out.println("Invalid username/password combination");
				
				}
				System.out.println("Press enter to try to reconnect, or type 'quit' to exit!");
				String next = in.nextLine();
				if(next.equals("quit;")){
					quit = true;
				}
			}*/
		}
		
		
		
		while(!quit){
			
			String command = in.nextLine();
			if(command.equals("help;")){
				System.out.println("Type 'add star' to add a star to the database");
				System.out.println("Type 'add movie' to add a movie to the database");
				System.out.println("Type 'search for movies' to search for movies by star");
				System.out.println("Type 'add customer' to add a customer to the database");
				System.out.println("Type 'delete customer' to remove a customer from the database");
				System.out.println("Type 'query database' to enter a SQL query to the database");
				System.out.println("Type 'meta data' to get database meta data");
				System.out.println("Type 'error report' to generate an html error report for the database");
				System.out.println("Type 'logout' to logout of the database");
				System.out.println("Type ' quit' to quit the program");
				System.out.println("Remember to end each command with a ';'");
			}
			else if(command.equals("quit;")){
				quit = true;
				System.out.println("Program terminated");
			}
			else if(command.equals("login;") && connection.isClosed()){
				System.out.println("Please login");
				System.out.println("Username:");
				username = in.nextLine();
				System.out.println("Password:");
				password = in.nextLine();
				
				connection = DriverManager.getConnection("jdbc:mysql:///moviedb","root","fourdelta65");
				
				String loginQuery = "select count(*) from employees where email = '" + username+"' and password = '"
				+ password +"';";
				
				PreparedStatement p = connection.prepareStatement(loginQuery);
				ResultSet r = p.executeQuery();
				r.beforeFirst();
				r.next();
				if(r.getInt(1) == 1){
					connected = true;
					System.out.println("logged in");
				}
				else{
					connection.close();
					System.out.println("Invalid id/pass combination, try again");
				}
					
				/*catch (SQLException e){
					if(e.getErrorCode() == 1049){
						System.out.println("Database not Found");
					}
					else if(e.getErrorCode() == 1045){
						System.out.println("Invalid username/password combination");
					}
					System.out.println("Type 'login' to try to reconnect or type 'quit' to exit!");
				}*/
				
			}
			else if(command.equals("logout;") && !connection.isClosed()){
				connection.close();
				System.out.println("You have succesfully logged out");
				System.out.println("Type 'login' to reconnect, or type 'quit' to exit the program");
			}
			else if(command.equals("add star;")&&!connection.isClosed()){
				addStarToDataBase(connection);
			}
			else if(command.equals("add customer;")&&!connection.isClosed()){
				addCustomerToDatabase(connection);
			}
			else if(command.equals("query database;")&&!connection.isClosed()){
				freeQuery(connection);
			}
			else if(command.equals("delete customer;")&&!connection.isClosed()){
				deleteCustomer(connection);
			}
			else if(command.equals("search for movies;")&&!connection.isClosed()){
				getMovieByStar(connection);
			}
			else if(command.equals("meta data;") && !connection.isClosed()){
				getMetaData(connection);
			}
			else if(command.equals("add movie;") && !connection.isClosed()){
				try{
					
					addMovie(connection);
				}catch(SQLException e){
					System.out.println("The data you entered was invalid, please try again");
					System.out.println(e);
				}
				
			}
			else if(command.equals("error report;") && !connection.isClosed()){
				ErrorReport err = new ErrorReport(connection);
				err.generateErrorReport();
			}
			else{
				if(!connection.isClosed()){
					System.out.println("Unrecognized command, please type help for list of valid commands");
				}
				else{
					System.out.println("The connection is closed, please reconnect to the database by logging in");
					System.out.println("Or quit the program by typing 'quit'");;
				}
			}
			
			
		}
		
		
		
		
	}
	
	
	public static void getMovieByStar(Connection c) throws SQLException{
		System.out.println("Please enter the following details of the star");
		System.out.println("First name:");
		String first_name = in.nextLine();
		System.out.println("Last name:");
		String last_name = in.nextLine();
		System.out.println("Id:");
		String id = in.nextLine();
		String query = "select m.id,m.title,m.year,m.director,s.first_name,s.last_name,m.banner_url,m.trailer_url from stars s" +
		" inner join stars_in_movies sm inner join movies m on" +
		" s.id = sm.star_id and m.id = sm.movie_id where";
		boolean valid = true;
		if(id.equals("")){
		
			
			if(first_name.equals("") && last_name.equals("")){
				System.out.println("Invalid search parameters, you must enter a name!");
				valid = false;
			}
			else if(first_name.equals("")){
				last_name = "'" + last_name + "'";
				query = query+ " s.last_name ="+last_name+" order by m.title";
			}
			else if(last_name.equals("")){
				first_name = "'" + first_name + "'";
				query = query+ " s.first_name ="+first_name+" order by m.title";
			}
			else{
				first_name = "'" + first_name + "'";
				last_name = "'" + last_name + "'";
				query = query + " s.first_name ="+first_name+" and s.last_name ="+last_name+" order by m.title";
			}
		}
		else{
			id = "'" + id + "'";
			query = query + " s.id =" + id;
		}
		
				
				
		if(valid){		
		
			PreparedStatement sel = c.prepareStatement(query);
			ResultSet r = sel.executeQuery();
		
	
			while(r.next()){
				System.out.println("ID: "+r.getString(1));
				System.out.println("Title: " + r.getString(2));
				System.out.println("Year: " + r.getString(3));
				System.out.println("Director: " + r.getString(4));
				System.out.println("Actor: " + r.getString(5) + " " + r.getString(6));
				System.out.println("Banner URL: " + r.getString(7));
				System.out.println("Trailer URL: " + r.getString(8));
				System.out.println();
				System.out.println();
		
			}
		}
		
	}
	
	public static void addStarToDataBase(Connection c){
		System.out.println("Please enter the following details");
		System.out.println("First name:");
		String first_name = "'"+in.nextLine()+"'";
		System.out.println("Last name:");
		String last_name = in.nextLine();
		if(last_name.equals("")){
			last_name = first_name;
			first_name = "'\"\"'";
		}
		else{
			last_name = "'"+last_name+"'";
		}
		
		System.out.println("Date of birth: Please enter as YYYY/MM/DD(optional)");
		String dob =  in.nextLine();
		System.out.println("Photo url: (optional)");
		String url =  in.nextLine();
		
		if(dob.equals("")){
			dob = "NULL";
		}
		else{
			dob =  "'"+dob+"'";
		}
		if(url.equals("")){
			url = "NULL";
		}
		else{
			url =  "'"+url+"'";
		}
		String query = "insert into stars(first_name,last_name,dob,photo_url) values("+
		first_name+","+last_name+","+dob+","+url+")";
		
		try {
			PreparedStatement s = c.prepareStatement(query);
			int rowsAffected = s.executeUpdate();
			System.out.println("Star successfully added to database :" + rowsAffected+" row(s) affected" );
		} catch (SQLException e) {
			
			System.out.println("Error:Invalid syntax");
		}
		
		
		
	}
	
	
	public static void freeQuery(Connection c){
		System.out.println("Enter a valid SQL query");
		String query = in.nextLine();
		Scanner scan = new Scanner(query);
		String keyword = scan.next();
		
		//Check if we are inserting or updating
		//if yes, executeUpdate and return int
		//if no, executeQuery and return data
		if(keyword.toLowerCase().equals("insert") || keyword.toLowerCase().equals("update") || keyword.toLowerCase().equals("delete")){
			try {
				PreparedStatement s = c.prepareStatement(query);
				int rowsAffected = s.executeUpdate();
				System.out.println("Execution succesful :" + rowsAffected+" row(s) affected" );
			} catch (SQLException e) {
				
				System.out.println("Error: Invalid Syntax On Query");
				
			}
			
		}
		else{
			PreparedStatement s;
			try {
				s = c.prepareStatement(query);
				ResultSet r = s.executeQuery();
				ResultSetMetaData m = r.getMetaData();
				String head = "";
				for(int i = 1; i <= m.getColumnCount(); i++){
					head += m.getColumnLabel(i) + " ";
				}
				System.out.println(head);
				
				while(r.next()){
					
					String result = "";
				
					
					for(int i = 1; i <= m.getColumnCount(); i++){
						result += r.getString(i) + " ";
					}
					
					System.out.println(result);
				}
			} catch (SQLException e) {
				
				System.out.println("Error: Invalid Syntax on Query");
				
			}
			
		}
		
		
	}
	
	//TODO if no last name, first name is last name
	public static void addCustomerToDatabase(Connection c){
		System.out.println("Please enter the following information");
		System.out.println("First Name?");
		String first_name = in.nextLine();
		System.out.println("Last Name?");
		String last_name = in.nextLine();
		System.out.println("Credit Card Number:");
		String cc_id = "'"+in.nextLine()+"'";
		//Change address to be better
		System.out.println("Current Address:");
		String address = "'"+in.nextLine()+"'";
		if(last_name.equals("")){
			last_name = "'"+first_name+"'";
			first_name = "'\"\"'";
		}
		else{
			last_name = "'"+last_name+"'";
			first_name = "'"+first_name+"'";
		}
		
		System.out.println("Please enter a valid email address:");
		String email = "'"+in.nextLine()+"'";
		System.out.println("Please choose a password now:");
		String password = "'"+in.nextLine()+"'";
		
		//Two queries, one to make sure cc exists in tabke
		//other to add data
		
		String query = "select count(*) from creditcards where id =" + cc_id;
		int count = 0;
		try {
			PreparedStatement s = c.prepareStatement(query);
			ResultSet r = s.executeQuery(query);
			r.first();
			count = r.getInt(1);
			
		} catch (SQLException e) {
			
			System.out.println("Error:Invalid values");
			
		}
		
		if(count == 0){
			System.out.println("The credit card does not exist in the DB");
		}
		else{
			query = "insert into customers(first_name,last_name,cc_id,address,email,password) values("
				+first_name+","+last_name+","+cc_id+","+address+","+email+","+password+")";
			try {
				PreparedStatement s = c.prepareStatement(query);
				int rows = s.executeUpdate();
				System.out.println("Customer added to database: " + rows +" row(s) affected");
			} catch (SQLException e) {
				
				System.out.println("Error:Invalid values");
			}
			
		}
		
	}
	
	
	public static void deleteCustomer(Connection c){
		System.out.println("Enter the id of the customer");
		String id = in.nextLine();
		String query = "";
		PreparedStatement s;
		int rows = 0;
		if(id.equals("")){
			System.out.println("Please enter the full name of the customer");
			String name = in.nextLine();
			Scanner scan = new Scanner(name);
			String first_name = "'"+scan.next()+"'";
			String last_name = "'"+scan.next()+"'";
			query = "delete from customers where first_name = " +first_name +" and last_name = " + last_name;
			
			
			
		}
		else{
			query  = "delete from customers where id ="+id;
		}
		try {
			s = c.prepareStatement(query);
			rows = s.executeUpdate();
		} catch (SQLException e) {
		
			System.out.println("Ivalid values for id, or name");
		}
		System.out.println("Customer deleted : " + rows + " row(s) affected");
	}
	
	
	public static void getMetaData(Connection c) throws SQLException{
		DatabaseMetaData d = c.getMetaData();
		//Get all table names
		System.out.println();
		System.out.println("The database " + db+" contains the following tables" );
		System.out.println();
		ResultSet tables = d.getTables(null, null, "%", null);
		while(tables.next()){
			System.out.println("Table name: "+tables.getString(3));
			ResultSet columns = d.getColumns(null,null,tables.getString(3),null);
			while(columns.next()){
				System.out.println("Column Name: " + columns.getString(4) + "   Type: "+columns.getString(6));
			}
			System.out.println();
		}
	}
	
	
	public static void addMovie(Connection c) throws SQLException{
		System.out.println("Please enter the following details for the movie");
		System.out.println("Title: (required)");
		String title = in.nextLine();
		System.out.println("Year: (required)");
		String year = in.nextLine();
		System.out.println("Director: (required)");
		String director = in.nextLine();
		System.out.println("Banner URL: ");
		String burl = in.nextLine();
		System.out.println("Trailer URL: ");
		String turl = in.nextLine();
		boolean valid = false;
		String first_name = "";
		String last_name = "";
		while(!valid){
			System.out.println("Lead star's full name");
			String fullname = in.nextLine();
			if(!fullname.equals("")){
				Scanner name = new Scanner(fullname);
				try{
					first_name = name.next();
					last_name = name.next();
				}catch(NoSuchElementException e){
					System.out.println("Please enter the stars full name!");
					System.out.println(e);
				}
			}
			
		
		
			if(first_name.equals("") && last_name.equals("")){
				
			}
			else{
				if(last_name.equals("")){
					last_name = first_name;
					first_name = "";
				}
				valid  = true;
			}
		}
		
		System.out.println("Genre (required)");
		String genre = in.nextLine();
		
		String query = "select count(*) from movies where title = '"+title+"' and year='"+
		year+"' and director = '"+director+"';";
		PreparedStatement stmt = c.prepareStatement(query);
		ResultSet r = stmt.executeQuery();
		r.next();
		if(r.getInt(1) == 0){
			query = "call add_movie('"+title+"','"+year+"','"+director+"','"+
			burl+"','"+turl+"','"+first_name+"','"+last_name+"','"+genre+"');";
			
			stmt = c.prepareStatement(query);
			stmt.execute();
			System.out.println("Movie :" + title + " added!");
			System.out.println("Relevant Tables updated");
		}
		else{
			System.out.println("Error: Movie already exists in database");
		}
		
		
		
	}
	
	
	
	
	
	
	

}
