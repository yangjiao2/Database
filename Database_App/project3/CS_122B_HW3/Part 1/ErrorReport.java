import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



public class ErrorReport {
	public static Connection connection;
	public String base = "call ";
	
	public ErrorReport(Connection c){
		connection = c;
	}
	
	
public void generateErrorReport() throws SQLException{
		
	
		
				
		String filePath = "c:/users/kunal/desktop/error.html";
		File file = new File(filePath);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write("<html><head><title>Error Report</title></head><body BGCOLOR=\"#FDF5E6\">");
			writer.write("<h2 align = \"center\">Error Report</h2>");
			//System.out.println(duplicate_movies().getBody());
			writer.write(duplicate_movies().getBody() + "<p><hr><hr>");
			writer.write(duplicate_stars().getBody()+ "<p><hr><hr>");
			writer.write(duplicate_genres().getBody()+ "<p><hr><hr>");
			writer.write(stars_without_movies().getBody()+ "<p><hr><hr>");
			writer.write(movies_without_stars().getBody()+ "<p><hr><hr>");
			writer.write(genres_without_movies().getBody()+ "<p><hr><hr>");
			writer.write(movies_without_genres().getBody()+ "<p><hr><hr>");
			writer.write(stars_with_ivalid_dob().getBody()+ "<p><hr><hr>");
			writer.write(customers_with_invalid_email().getBody()+ "<p><hr><hr>");
			writer.write(expired_creditcards().getBody()+ "<hr><hr>");
			writer.write(stars_with_invalid_names().getBody()+ "<p><hr><hr>");
			writer.write("</body></html>");
			writer.close();
			System.out.println(filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("file not found");
		}
		catch(SQLException e){
			System.out.println(e);
		}
	}


private static Error duplicate_movies() throws SQLException{
	String body = "";
	int count = 0;
	Error err = new Error(body,count);
	
	String query = "call duplicate_movies";
	PreparedStatement stmt = connection.prepareStatement(query);
	ResultSet result = stmt.executeQuery();
	result.last();
	count = result.getRow();
	result.beforeFirst();
	result.next();
	body += "<p><h3 align = \"center\"><u>Detected duplicates in movies table</u></h3>";
	body += "<p>"+count+" errors detected</p>";
	body += "<p> INFORMATION: This table is generated from movies that have multiple entries in the movies table"+
	". We list the relevent ID's in a single column for easy access. Each ID in the duplicate ID's column"
	+" corresponds to an erroneous entry in the database.";
	body += "<p>Recommendation : Use Title and Year to locate all duplicate ID's and delete them</p>";
	body += "<TABLE border><tr>"+
	"<th>Title</th>"+
	"<th>Director</th>"+
	"<th>Year</th>"+
	"<th>ID of Duplicate Records</th></tr>";
	try{
	String oldTitle = result.getString(2);
	String oldYear =result.getString(4);
	String oldDirector=result.getString(3);
	String ids =result.getString(1)+"<br>";
	while(result.next()){
		String title = result.getString(2);
		String director = result.getString(3);
		String year = result.getString(4);
		if(!oldTitle.equals(title)&& !oldYear.equals(year)){
			body += "<tr><td>"+oldTitle+"</td>"+
			"<td>"+oldDirector+"</td>"+
			"<td>"+oldYear+"</td>"+
			"<td>"+ids+"</td></tr>";
			
			oldTitle = title;
			oldYear = year;
			oldDirector = director;
			ids = result.getString(1)+"<br>";
			
		}
		else{
			ids += result.getString(1)+"<br>";
		}
	}
	
	body += "<tr><td>"+oldTitle+"</td>"+
	"<td>"+oldDirector+"</td>"+
	"<td>"+oldYear+"</td>"+
	"<td>"+ids+"</td></tr>";
	}catch(SQLException e){
		
	}
	body += "</table>";
	err.setBody(body);
	err.setCount(count);
	
	return err;
}

private Error duplicate_stars() throws SQLException{
	String body = "";
	int count = 0;
	Error err = new Error(body,count);
	String query = "call duplicate_stars";
	PreparedStatement stmt = connection.prepareStatement(query);
	ResultSet result = stmt.executeQuery();
	result.last();
	count = result.getRow();
	result.beforeFirst();
	result.next();
	body += "<p><h3 align= \"center\"><u>Detected duplicates in Stars table</u></h3>";
	body += "<p>"+count+" errors detected</p>";
	body += "<p> INFORMATION: This table is generated from stars that have multiple entries in the stars table"+
	". We list the relevent ID's in a single column for easy access. Each ID in the duplicate ID's column"
	+" corresponds to an erroneous entry in the database.";
	body += "<p>Recommendation : Use FIrst and Last name and DOB to locate all duplicate ID's and delete them</p>";
	body += "<TABLE border><tr>"+
	"<th>First Name</th>"+
	"<th>Last Name</th>"+
	"<th>Date Of Birth</th>"+
	"<th>ID of Duplicate Records</th></tr>";
	try{
	String old_first_name = result.getString(2);
	String old_last_name = result.getString(3);
	String old_dob = result.getString(4);
	String ids =result.getString(1)+" <br> ";
	while(result.next()){
		String first_name = result.getString(2);
		String last_name = result.getString(3);
		String dob = result.getString(4);
		if(!old_first_name.equals(first_name)&&!old_last_name.equals(last_name)&&!old_dob.equals(dob)){
			
			body += "<tr><td>"+old_first_name+"</td>"+
			"<td>"+old_last_name+"</td>"+
			"<td>"+old_dob+"</td>"+
			"<td>"+ids+"</td></tr>";
			
			old_first_name = first_name;
			old_last_name = last_name;
			old_dob = dob;
			
			ids = result.getString(1) +" <br> ";
		}
		else{
			ids += result.getString(1)+" <br> ";
		}
		
	}
	
	body += "<tr><td>"+old_first_name+"</td>"+
	"<td>"+old_last_name+"</td>"+
	"<td>"+old_dob+"</td>"+
	"<td>"+ids+"</td></tr>";
	}catch(SQLException e){
		
	}
	body += "</table>";
	err.setBody(body);
	err.setCount(count);
	return err;
}

private Error duplicate_genres() throws SQLException{
	String body = "";
	int count = 0;
	Error err = new Error(body,count);
	String query = "call duplicate_genres";
	PreparedStatement stmt = connection.prepareStatement(query);
	ResultSet result = stmt.executeQuery();
	result.last();
	count = result.getRow();
	result.beforeFirst();
	result.next();
	body += "<p><h3 align= \"center\"><u>Detected duplicates in Genres table</u></h3>";
	body += "<p>"+count+" errors detected</p>";
	body += "<p> INFORMATION: This table is generated from genres that have multiple entries in the genres table"+
	". We list the relevent ID's in a single column for easy access. Each ID in the duplicate ID's column"
	+" corresponds to an erroneous entry in the database.";
	body += "<p>Recommendation : Use Genre name to locate all duplicate ID's and delete them</p>";
	body += "<TABLE border><tr>"+
	"<th>Genre Name</th>"+
	"<th>ID of Duplicate Records</th></tr>";
	try{
	String old_name = result.getString(2);
	String ids =result.getString(1)+" <br> ";
	while(result.next()){
		String name = result.getString(2);
		
		if(!old_name.toLowerCase().equals(name.toLowerCase())){
			
			body += "<tr><td>"+old_name+"</td>"+
			"<td>"+ids+"</td></tr>";
			
			old_name = name;
			
			
			ids = result.getString(1) +" <br> ";
		}
		else{
			ids += result.getString(1)+" <br> ";
		}
		
	}
	
	body += "<tr><td>"+old_name+"</td>"+
	"<td>"+ids+"</td></tr>";
	}catch(SQLException e){
		
	}
	body += "</table>";
	err.setBody(body);
	err.setCount(count);
	
	
	return err;
}

private Error stars_without_movies() throws SQLException{
	String body = "";
	int count = 0;
	Error err = new Error(body,count);
	String query = "call stars_without_movies";
	PreparedStatement stmt = connection.prepareStatement(query);
	ResultSet result = stmt.executeQuery();
	result.last();
	count = result.getRow();
	result.beforeFirst();
	body += "<h3 align = \"center\"><u>List of stars with no movies associated with them</u></h3>";
	body += "<p>"+count+" errors detected</p>";
	body += "<p> INFORMATION: This report is generated based on stars that are not associated with any movies."+
	" The report has been generated by subtracting the set of star ID's in stars_in_movies "+
	"from the set of star ID's in stars.";
	body += "<p> Recommendation: Check actor lists on movies thoroughly before adding movies to database</p>";
	body += "<TABLE border><tr>"+
	"<th>ID</th>"+
	"<th>First Name</th>"+
	"<th>Last Name</th>"+
	"<th>Date Of Birth</th>"+
	"</tr>";
	while(result.next()){
		body += "<tr><td>"+result.getString(1)+"</td>"+
		"<td>"+result.getString(2)+"</td>"+
		"<td>"+result.getString(3)+"</td>"+
		"<td>"+result.getString(4)+"</td></tr>";
	}
	body += "</table>";
	err.setBody(body);
	err.setCount(count);
	
	return err;
}

private Error movies_without_stars() throws SQLException{
	String body = "";
	int count = 0;
	Error err = new Error(body,count);
	String query = "call movies_without_stars";
	PreparedStatement stmt = connection.prepareStatement(query);
	ResultSet result = stmt.executeQuery();
	result.last();
	count = result.getRow();
	result.beforeFirst();
	body += "<h3 align = \"center\"><u>List of movies with no stars associated with them</u></h3>";
	body += "<p>"+count+" errors detected</p>";
	body += "<p> INFORMATION: This report is generated based on movies that are not associated with any stars."+
	" The report has been generated by subtracting the set of movie ID's in stars_in_movies "+
	"from the set of movie ID's in movies.";
	body += "<p> Recommendation: Check actor lists on movies thoroughly before adding movies to database</p>";
	body += "<TABLE border><tr>"+
	"<th>ID</th>"+
	"<th>Title</th>"+
	"<th>Year</th>"+
	"<th>Director</th>"+
	"</tr>";
	while(result.next()){
		body += "<tr><td>"+result.getString(1)+"</td>"+
		"<td>"+result.getString(2)+"</td>"+
		"<td>"+result.getString(3)+"</td>"+
		"<td>"+result.getString(4)+"</td></tr>";
	}
	body += "</table>";
	err.setBody(body);
	err.setCount(count);
	
	return err;
}

private Error genres_without_movies() throws SQLException{
	String body = "";
	int count = 0;
	Error err = new Error(body,count);
	String query = "call genres_without_movies";
	PreparedStatement stmt = connection.prepareStatement(query);
	ResultSet result = stmt.executeQuery();
	result.last();
	count = result.getRow();
	result.beforeFirst();
	body += "<h3 align = \"center\"><u>List of genres with no movies associated with them</u></h3>";
	body += "<p>"+count+" errors detected</p>";
	body += "<p> INFORMATION: This report is generated based on genres that are not associated with any movies."+
	" The report has been generated by subtracting the set of genre ID's in genres_in_movies "+
	"from the set of genre ID's in genres.";
	body += "<p> Recommendation: Check genre lists on movies thoroughly before adding movies to database</p>";
	body += "<TABLE border><tr>"+
	"<th>ID</th>"+
	"<th>Name</th>"+
		"</tr>";
	while(result.next()){
		body += "<tr><td>"+result.getString(1)+"</td>"+
		"<td>"+result.getString(2)+"</td></tr>";
	}
	body += "</table>";
	err.setBody(body);
	err.setCount(count);
	
	return err;
}

private Error movies_without_genres() throws SQLException{
	String body = "";
	int count = 0;
	Error err = new Error(body,count);
	String query = "call movies_without_genres ";
	PreparedStatement stmt = connection.prepareStatement(query);
	ResultSet result = stmt.executeQuery();
	result.last();
	count = result.getRow();
	result.beforeFirst();
	body += "<h3 align = \"center\"><u>List of movies with no genres associated with them</u></h3>";
	body += "<p>"+count+" errors detected</p>";
	body += "<p> INFORMATION: This report is generated based on movies that are not associated with any genre."+
	" The report has been generated by subtracting the set of movie ID's in genress_in_movies "+
	"from the set of movie ID's in movies.";
	body += "<p> Recommendation: Check genre lists on movies thoroughly before adding movies to database</p>";
	body += "<TABLE border><tr>"+
	"<th>ID</th>"+
	"<th>Title</th>"+
	"<th>Year</th>"+
	"<th>Director</th>"+
	"</tr>";
	while(result.next()){
		body += "<tr><td>"+result.getString(1)+"</td>"+
		"<td>"+result.getString(2)+"</td>"+
		"<td>"+result.getString(3)+"</td>"+
		"<td>"+result.getString(4)+"</td></tr>";
	}
	body += "</table>";
	err.setBody(body);
	err.setCount(count);
	
	return err;
}

private Error stars_with_ivalid_dob() throws SQLException{
	String body = "";
	int count = 0;
	Error err = new Error(body,count);
	String query = "call stars_with_ivalid_dob";
	PreparedStatement stmt = connection.prepareStatement(query);
	ResultSet result = stmt.executeQuery();
	result.last();
	count = result.getRow();
	result.beforeFirst();
	body += "<h3 align = \"center\"><u>List of stars with invalid dates of birth</u></h3>";
	body += "<p>"+count+" errors detected</p>";
	body += "<p> INFORMATION: This report contains the list of stars whose date of birth is beyond the "+
	"valid range. i.e before 1900 or after the current date";
	
	body += "<TABLE border><tr>"+
	"<th>ID</th>"+
	"<th>First Name</th>"+
	"<th>Last Name</th>"+
	"<th>Date Of Birth</th>"+
	"</tr>";
	while(result.next()){
		body += "<tr><td>"+result.getString(1)+"</td>"+
		"<td>"+result.getString(2)+"</td>"+
		"<td>"+result.getString(3)+"</td>"+
		"<td>"+result.getString(4)+"</td></tr>";
	}
	body += "</table>";
	err.setBody(body);
	err.setCount(count);
	return err;
}

private Error customers_with_invalid_email() throws SQLException{
	String body = "";
	int count = 0;
	Error err = new Error(body,count);
	String query = "call customers_with_invalid_email";
	PreparedStatement stmt = connection.prepareStatement(query);
	ResultSet result = stmt.executeQuery();
	result.last();
	count = result.getRow();
	result.beforeFirst();
	body += "<h3 align = \"center\"><u>List of customers with invalid email addresses</u></h3>";
	body += "<p>"+count+" errors detected</p>";
	body += "<p> INFORMATION: This report contains the list of customers whose email does not contain an @";
	body += "<p> Recommendation: Users must be notified and the emails re-entered</p>";
	body += "<TABLE border><tr>"+
	"<th>ID</th>"+
	"<th>First Name</th>"+
	"<th>Last Name</th>"+
	"<th>Email Address</th>"+
	"</tr>";
	while(result.next()){
		body += "<tr><td>"+result.getString(1)+"</td>"+
		"<td>"+result.getString(2)+"</td>"+
		"<td>"+result.getString(3)+"</td>"+
		"<td>"+result.getString(6)+"</td></tr>";
	}
	body += "</table>";
	err.setBody(body);
	err.setCount(count);
	return err;
}

private Error expired_creditcards() throws SQLException{
	String body = "";
	int count = 0;
	Error err = new Error(body,count);
	String query = "call expired_creditcards";
	PreparedStatement stmt = connection.prepareStatement(query);
	ResultSet result = stmt.executeQuery();
	result.last();
	count = result.getRow();
	result.beforeFirst();
	body += "<h3 align = \"center\"><u>List of customers with expired credit cards</u></h3>";
	body += "<p>"+count+" errors detected</p>";
	body +="<p> INFORMATION: This report contains the list of customers whose credit cards are expired";
	body += "<p> Recommendation: Customers must be contacted to update their billing information</p>";
	body += "<TABLE border><tr>"+
	"<th>Credit Card Number</th>"+
	"<th>First Name</th>"+
	"<th>Last Name</th>"+
	"<th>Date Of Expiry</th>"+
	"</tr>";
	while(result.next()){
		body += "<tr><td>"+result.getString(1)+"</td>"+
		"<td>"+result.getString(2)+"</td>"+
		"<td>"+result.getString(3)+"</td>"+
		"<td>"+result.getString(4)+"</td></tr>";
	}
	body += "</table>";
	err.setBody(body);
	err.setCount(count);
	return err;
}

private Error stars_with_invalid_names() throws SQLException{
	String body = "";
	int count = 0;
	Error err = new Error(body,count);
	String query = "call stars_with_invalid_names ";
	PreparedStatement stmt = connection.prepareStatement(query);
	ResultSet result = stmt.executeQuery();
	result.last();
	count = result.getRow();
	result.beforeFirst();
	body += "<h3 align = \"center\"><u>List of stars with invalid names</u></h3>";
	body += "<p>"+count+" errors detected</p>";
	body += "<p>INFORMATION:  This report contains the list of stars whose names are null values";
	body += "<p> Recommendation: These records should be deleted or updated with star names </p>";
	body += "<TABLE border><tr>"+
	"<th>ID</th>"+
	"<th>First Name</th>"+
	"<th>Last Name</th>"+
	"<th>Date Of Birth</th>"+
	"</tr>";
	while(result.next()){
		body += "<tr><td>"+result.getString(1)+"</td>"+
		"<td>"+result.getString(2)+"</td>"+
		"<td>"+result.getString(3)+"</td>"+
		"<td>"+result.getString(4)+"</td></tr>";
	}
	body += "</table>";
	err.setBody(body);
	err.setCount(count);
	return err;
}
}
