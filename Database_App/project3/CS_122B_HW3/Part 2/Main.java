import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;

import com.mysql.jdbc.Connection;


public class Main{

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	
	public static Scanner in;
	public static java.sql.Connection connection = null;
	public static DatabaseMetaData d = null;
	static ArrayList<Users> users = null;
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Users selectedUser = null;
		boolean userSelected = false;
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection("jdbc:mysql:///moviedb","root","fourdelta65");
		d = connection.getMetaData();
		in = new Scanner(System.in);
		boolean logged = false;
		boolean quit = false;
		users = new ArrayList<Users>();
		String query = "Select user,host from mysql.user;";
		PreparedStatement stmt = connection.prepareStatement(query);
		ResultSet results = stmt.executeQuery();
		
		while(results.next()){
			String name = results.getString(1);
			String host = results.getString(2);
			users.add(new Users(name,host));
			
		}
		
		
		
		while(!logged){
			System.out.println("Please connect to the back-end");
			System.out.println("Username:");
			String username = in.nextLine();
			System.out.println("Password:");
			String password = in.nextLine();
			
			String loginQuery = "select count(*) from employees where email = '" + username+"' and password = '"
			+ password +"';";
			
			PreparedStatement p = connection.prepareStatement(loginQuery);
			ResultSet r = p.executeQuery();
			r.beforeFirst();
			r.next();
			if(r.getInt(1) == 1){
				logged = true;
				System.out.println("logged in");
				System.out.println("You are connected to the backend of the MovieDB database");
				System.out.println("Type help for a list of commands");
				System.out.println("Type quit to terminate the program");
			}
			else{
				System.out.println("Invalid id/pass combination, try again");
			}
			
		}
			
			
			
			
		while(!quit){
					
			String command = in.nextLine().toLowerCase();
			
			if(command.equals("quit")){
				quit = true;
				System.out.println("Program terminated");
			}
			else if(command.equals("show users")){
				getUsers(users);
				System.out.println("done");
			}
			else if(command.equals("select user")){
				System.out.println("Select the username of the user you would like to modify");
				getUsers(users);
				String n = in.nextLine();
				userSelected = false;
				for(Users u : users){
					if(u.getName().equals(n)){
						userSelected = true;
						selectedUser = u;
						System.out.println("Selected");
					}
				}
				if(!userSelected){
					System.out.println("Error: That user does not exist, please try again");
				}
			}
			else if(command.equals("show privileges")){
				if(userSelected){
				getPrivileges(selectedUser);
				}
				else{
					System.out.println("Please select a user");
				}
			}
			else if(command.equals("grant privilege")){
					if(userSelected){
						grantPrivileges(selectedUser);
					}
					else{
						System.out.println("Please select a user");
					}
				
			}
			else if(command.equals("revoke privilege")){
				if(userSelected){
					revokePrivileges(selectedUser);
				}
				else{
					System.out.println("Please select a user");
				}
			}
			else if(command.equals("revoke all privileges")){
				if(userSelected){
					revokeAllPrivileges(selectedUser);
				}
				else{
					System.out.println("Please select a user");
				}
			}
			else if(command.equals("add user")){
				addUser();
			}
			else if(command.equals("help")){
				System.out.println("Type show users to see a list of users in the system");
				System.out.println("Type select user to select a user to perform tasks upon");
				System.out.println("Type add user to add a user to the system");
				System.out.println("Type grant privilege to assign privileges to a user. Note: you must have a user selected to do this");
				System.out.println("Type revoke privilege to revoke a user privilege. Note: you must have a user selected to do this");
				System.out.println("Type revoke all privileges to revoke all user privileges. Note: you must have a user selected to do this");
				
				
			}
		}
				

	}
	
	
	public static void getUsers(ArrayList<Users> users) throws SQLException{
		System.out.println("Users of the system are");
		for(Users user : users){
			System.out.println(user.getName());
		}
		
		
		
	}
	
	

	
	
	
	public static void getPrivileges(Users u) throws SQLException{
		String query = "show grants for "+u.getAddress()+";";
		System.out.println("User has the following privileges:");
		PreparedStatement stmt = connection.prepareStatement(query);
		ResultSet results = stmt.executeQuery();
		boolean database = false;
		boolean table = false;
		while(results.next()){
			String r = "";
			//System.out.println("loop entered");
			
			
			String c = results.getString(1);
			Scanner scan = new Scanner(c);
			scan.next();
			String priv = scan.next();
			String type = scan.next();
			while(!type.equals("ON")){
				if(type.contains("()") || type.equals("ROUTINE")){
					priv += type;
				}
				else{
					priv += " "+type;
				}
				type = scan.next();
			}
			String layer = scan.next();
			//System.out.println(priv);
			//System.out.println(layer);
			if(layer.equals("*.*")){
				System.out.println();
				System.out.println("GLOBAL PRIVILEGES:");
				
				if(priv.equals("ALL PRIVILEGES")){
					r += "ALL PRIVILEGES";
				}
				else{
					r += " "+priv+" ON "+layer+"";
				}
			}
			else if(layer.equals("`moviedb`.*")){
				System.out.println();
				System.out.println("DATABASE LEVEL PRIVILEGES:");
				
				if(priv.equals("ALL")){
					r += "ALL PRIVILEGES FOR DATABASE moviedb";
				}
				else{
					r += " "+priv+" ON "+layer+"";
				}
				database = true;
			}
			else if(layer.equals("PROCEDURE")){
				System.out.println();
				System.out.println("PROCEDURE LEVEL PRIVILEGES:");
				
				if(database && table){
					layer = scan.next();
					r += " "+priv+" ON "+layer+"";
				}
				else{
					r +=" "+priv+" ON "+layer+"";
				}
			}
			else{
				System.out.println();
				System.out.println("TABLE LEVEL PRIVILEGES:");
				
				if(database){
					r += " "+priv+" ON "+layer+"";
				}
				else{
					r +=" "+priv+" ON "+layer+"";
				}
				table = true;
			}
			System.out.println(r);
	}
	}
	
	
	public static void grantPrivileges(Users u) throws SQLException{
		
		String query = "";
		System.out.println("Please select the level of "
				+"privilege you wish to grant on this user");
		System.out.println("Database Level");
		System.out.println("Table Level");
		System.out.println("Column Level");
		System.out.println("Procedure Level");
		String level = in.nextLine();
		String privilege = "";
		
		
		if(level.toLowerCase().equals("table level")||level.toLowerCase().equals("column level")){
			System.out.println("Please select a table");
			ResultSet tables = d.getTables(null, null, "%", null);
			while(tables.next()){
				System.out.println("Table name: "+tables.getString(3));
			}
			String tablename = in.nextLine();
			String columnName = "";
			if(level.toLowerCase().equals("column level")){
				System.out.println("Please select a column");
				ResultSet columns = d.getColumns(null,null,tablename,null);
				while(columns.next()){
					System.out.println("Column Name: " + columns.getString(4));
				}
				columnName = in.nextLine();
				
			}
			
			System.out.println("Please select from the following set of privileges to grant");
			if(columnName.equals("")){
				System.out.println("SELECT");
				System.out.println("INSERT");
				System.out.println("UPDATE");
				System.out.println("CREATE");
				System.out.println("DROP");
				System.out.println("ALTER");
				System.out.println("DELETE");
				privilege = in.nextLine().toUpperCase();
				query = "GRANT "+privilege+" ON moviedb."+tablename+" TO " +
				u.getAddress();
			}
			else{
				System.out.println("SELECT");
				System.out.println("INSERT");
				System.out.println("UPDATE");
				privilege = in.nextLine().toUpperCase();
				query = "GRANT "+privilege+" ("+columnName+") ON moviedb."+tablename+" TO " +
				u.getAddress();
			}
			
			
			
		}
		
		else if (level.toLowerCase().equals("database level")){
			System.out.println("Please select from the following set of privileges to grant");
			System.out.println("CREATE");
			System.out.println("DROP");
			System.out.println("CREATE ROUTINE");
			System.out.println("SELECT");
			System.out.println("INSERT");
			System.out.println("UPDATE");
			
			privilege = in.nextLine().toUpperCase();
			query = "GRANT "+privilege+" ON moviedb.* TO " +
			u.getAddress();
		}
		
		else if (level.toLowerCase().equals("procedure level")){
			System.out.println("Please select a procedure from the following");
			query = "show procedure status where db = 'moviedb';";
			PreparedStatement stmt = connection.prepareStatement(query);
			ResultSet r = stmt.executeQuery();
			while(r.next()){
				System.out.println(r.getString(2));
			}
			
			String proc = in.nextLine();
			
			
			
			
			
			System.out.println("Please select from the following set of privileges to grant");
			System.out.println("ALTER ROUTINE");
			System.out.println("EXECUTE");
			privilege = in.nextLine().toUpperCase();
			query = "GRANT "+privilege+" ON PROCEDURE "+proc+" TO " +
			u.getAddress();
		}
		
		System.out.println("Are you sure? [Y/N]");
		String ans = in.nextLine();
		if(ans.toLowerCase().equals("y")){
			try{
			PreparedStatement exec = connection.prepareStatement(query);
			exec.executeUpdate();
			System.out.println("Changes commited, you have been returned to the main menu");
			}catch(SQLException e){
				System.out.println("There was an error");
				System.out.println(e);
			}
		}
		else{
			System.out.println("Action Cancelled, you have been returned to the main menu");
		}
		
		
		//System.out.println(query);
	}
	
	
	public static void revokePrivileges(Users u){
		String query = "";
		String columnName = "";
		String priv = "";
		String tablename = "";
		String proc = "";
		System.out.println("Please select one of the following privileges to revoke");
		try {
			getPrivileges(u);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Privelege to revoke");
		priv = in.nextLine().toUpperCase();
		System.out.println("Domain of privilege");
		System.out.println("Table name (if any)");
		tablename = in.nextLine();
		
		if(!tablename.equals("")){
			System.out.println("Column name (if any)");
			columnName = in.nextLine();
		}
		System.out.println("Procedure name (if any)");
		proc = in.nextLine();
		
		if(!tablename.equals("")){
			if(!columnName.equals("")){
				query = "REVOKE "+priv+" ("+columnName+") ON moviedb."+tablename+
				" FROM "+u.getAddress();
			}
			else{
				query = "REVOKE "+priv+" ON moviedb."+tablename+
				" FROM "+u.getAddress();
			}
		}
		else if(!proc.equals("")){
			query = "REVOKE "+priv+" ON PROCEDURE "+proc+" FROM "+u.getAddress();
		}
		else{
			query = "REVOKE "+priv+" ON moviedb.* FROM "+u.getAddress();
		}
		
		//System.out.println(query);
		System.out.println("Are you sure? [Y/N]");
		String ans = in.nextLine();
		
		try {
			if(ans.toLowerCase().equals("y")){
				PreparedStatement exec = connection.prepareStatement(query);
				exec.executeUpdate();
				System.out.println("Changes commited, you have been returned to the main menu");
			}
			else{
				System.out.println("Action Cancelled, you have been returned to the main menu");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("There was an error");
			System.out.println(e);
		}
		System.out.println();
		
	}

	
	public static void addUser() throws SQLException{
		System.out.println("Please enter the following details");
		System.out.println("Username :");
		String username = in.nextLine();
		System.out.println("Password :");
		String pass = in.nextLine();
		System.out.println("Hostname :");
		String host = in.nextLine();
		
		String query = "select count(*) from mysql.user where user ='"+username+"';";
		PreparedStatement stmt = connection.prepareStatement(query);
		ResultSet r = stmt.executeQuery();
		r.next();
		if(r.getInt(1) == 1){
			System.out.println("Error: The user already exists in the database");
		}
		else{
			query = "create user '"+username+"'@'"+host+"' identified by '"+pass+"';";
			stmt = connection.prepareStatement(query);
			stmt.executeUpdate();
			System.out.println("User added to the database system");
			users.add(new Users(username,host));
		}
		
	}
	
	
	public static void revokeAllPrivileges(Users u){
		
		String query = "REVOKE ALL PRIVILEGES, GRANT OPTION FROM "+u.getAddress();
		PreparedStatement stmt;
		System.out.println("Are you sure? [Y/N]");
		String ans = in.nextLine();
		try {
			if(ans.toLowerCase().equals("y")){
				stmt = connection.prepareStatement(query);
				stmt.executeUpdate();
				System.out.println("All privileges revoked!");
			}
			else{
				System.out.println("Action Cancelled, you have been returned to the main menu");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		
		
	}
	
}
