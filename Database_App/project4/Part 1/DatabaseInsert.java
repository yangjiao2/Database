import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;


public class DatabaseInsert {

	
	private Connection connection;
	private HashMap<String,String> map = new HashMap<String,String>();
	
	public DatabaseInsert(){
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection("jdbc:mysql:///xmlparse","root","fourdelta65");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		start();
	}
	
	
	public void start(){
		try{
			connection.setAutoCommit(false);
		}catch(SQLException e){
			
		}
	}
	
	public void commit(){
		try {
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void addArticle(Article article){
		if(map.containsKey(article.getKey())){
			System.out.println("Duplicate at article: " + article.getKey());
		}
		else{
			System.out.println();
			System.out.println("Inserting "+article.getKey());
			map.put(article.getKey(), article.getMdate());
			article.getInfo().setGenre("article");
			int id = insertDblpDocument(article.getInfo());
			insertDocumentMap(id,article.getInfo().getAuthors());
		}
	}
	
	public void addBook(Book book){
		if(map.containsKey(book.getKey())){
			System.out.println("Duplicate at " + book.getKey());
		}
		else{
			System.out.println();
			System.out.println("Inserting "+book.getKey());
			map.put(book.getKey(), book.getMdate());
			book.getInfo().setGenre("book");
			int id = insertDblpDocument(book.getInfo());
			insertDocumentMap(id,book.getInfo().getAuthors());
				
		}
	}
	
	public void addIncollection(Incollection incollection){
		if(map.containsKey(incollection.getKey())){
			System.out.println("Duplicate at incollection: " + incollection.getKey());
		}
		else{
			System.out.println();
			System.out.println("Inserting "+incollection.getKey());
			map.put(incollection.getKey(), incollection.getMdate());
			incollection.getInfo().setGenre("incollection");
			int id = insertDblpDocument(incollection.getInfo());
			insertDocumentMap(id,incollection.getInfo().getAuthors());
		}
	}
	
	public void addInproceedings(Inproceedings inproceedings){
		if(map.containsKey(inproceedings.getKey())){
			System.out.println("Duplicate at inproceedings: " + inproceedings.getKey());
		}
		else{
			System.out.println();
			System.out.println("Inserting "+inproceedings.getKey());
			map.put(inproceedings.getKey(), inproceedings.getMdate());
			inproceedings.getInfo().setGenre("inproceedings");
			int id = insertDblpDocument(inproceedings.getInfo());
			insertDocumentMap(id,inproceedings.getInfo().getAuthors());
		}
		
	}
	public void addMastersThesis(MastersThesis mastersthesis){
		if(map.containsKey(mastersthesis.getKey())){
			System.out.println("Duplicate at mastersthesis: " + mastersthesis.getKey());
		}
		else{
			System.out.println();
			System.out.println("Inserting "+mastersthesis.getKey());
			map.put(mastersthesis.getKey(), mastersthesis.getMdate());
			mastersthesis.getInfo().setGenre("mastersthesis");
			int id = insertDblpDocument(mastersthesis.getInfo());
			insertDocumentMap(id,mastersthesis.getInfo().getAuthors());
		}
	}
	public void addPhdThesis(PhdThesis phdthesis){
		if(map.containsKey(phdthesis.getKey())){
			System.out.println("Duplicate at phdthesis: " + phdthesis.getKey());
		}
		else{
			System.out.println();
			System.out.println("Inserting "+phdthesis.getKey());
			map.put(phdthesis.getKey(), phdthesis.getMdate());
			phdthesis.getInfo().setGenre("phdthesis");
			int id = insertDblpDocument(phdthesis.getInfo());
			insertDocumentMap(id,phdthesis.getInfo().getAuthors());
		}
	}
	public void addProceedings(Proceedings proceedings){
		if(map.containsKey(proceedings.getKey())){
			System.out.println("Duplicate at proceedings: " + proceedings.getKey());
		}
		else{
			System.out.println();
			System.out.println("Inserting "+proceedings.getKey());
			map.put(proceedings.getKey(), proceedings.getMdate());
			proceedings.getInfo().setGenre("proceedings");
			int id = insertDblpDocument(proceedings.getInfo());
			insertDocumentMap(id,proceedings.getInfo().getAuthors());
		}
	}
	public void addWWW(WWW www){
		if(map.containsKey(www.getKey())){
			System.out.println("Duplicate at www: " + www.getKey());
		}
		else{
			System.out.println();
			System.out.println("Inserting "+www.getKey());
			map.put(www.getKey(),www.getMdate());
			www.getInfo().setGenre("www");
			int id = insertDblpDocument(www.getInfo());
			insertDocumentMap(id,www.getInfo().getAuthors());
		}
	}
	
	
	
	private int insertDblpDocument(DocumentInfo info) {
		int id = -1;
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement("INSERT INTO tbl_dblp_document(title," +
					"start_page,end_page,year,volume,number,url,ee,cdrom,cite,crossref,isbn,series,editor_id" +
					",booktitle_id,genre_id,publisher_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, info.getTitle());
			//System.out.println(info.getTitle());
			if(info.getStartPage() == null||info.getStartPage().equals("")){
				statement.setNull(2, java.sql.Types.NULL);
				statement.setNull(3, java.sql.Types.NULL);
			}
			else{
				try{
					
					statement.setInt(2, Integer.parseInt(info.getStartPage()));
					statement.setInt(3, Integer.parseInt(info.getEndPage()));
				}catch(NumberFormatException e){
					System.out.println("Pages are not formatted correctly, setting value to null");
					//System.out.println(info.getStartPage());
					statement.setNull(2, java.sql.Types.NULL);
					statement.setNull(3, java.sql.Types.NULL);
				}
			}
			
			if(info.getYear() == null){
				statement.setNull(4, java.sql.Types.NULL);
			}
			else{
				try{
					//System.out.println(info.getYear());
					statement.setInt(4, Integer.parseInt(info.getYear()));
				}catch(NumberFormatException e){
					System.out.println("Year not formatted correctly, setting value to null  " +info.getYear());
					statement.setNull(4, java.sql.Types.NULL);
				}
			}
			
			if(info.getVolume() == null){
				statement.setNull(5, java.sql.Types.NULL);
			}
			else{
				try{
					statement.setInt(5, Integer.parseInt(info.getVolume()));
				}catch(NumberFormatException e){
					System.out.println("Volume not specified correctly, setting value to null");
					statement.setNull(5, java.sql.Types.NULL);
				}
			}
			
			if(info.getNumber() == null){
				statement.setNull(6, java.sql.Types.NULL);
			}
			else{
				try{
					statement.setInt(6, Integer.parseInt(info.getNumber()));
				}catch(NumberFormatException e){
					System.out.println("Number not specified correctly, setting value to null");
					statement.setNull(6, java.sql.Types.NULL);
				}
				
			}
			
			if(info.getUrl() == null){
				statement.setNull(7, java.sql.Types.NULL);
			}
			else{
				statement.setString(7, info.getUrl());
			}
			
			if(info.getEe() == null){
				statement.setNull(8, java.sql.Types.NULL);
			}
			else{
				statement.setString(8, info.getEe());
			}
			
			if(info.getCdrom() == null){
				statement.setNull(9, java.sql.Types.NULL);
			}
			else{
				statement.setString(9, info.getCdrom());
			}
			
			if(info.getCite() == null){
				statement.setNull(10, java.sql.Types.NULL);
			}
			else{
				statement.setString(10, info.getCite());
			}
			
			if(info.getCrossref() == null){
				statement.setNull(11, java.sql.Types.NULL);
			}
			else{
				statement.setString(11, info.getCrossref());
			}
			
			if(info.getIsbn() == null){
				statement.setNull(12, java.sql.Types.NULL);
			}
			else{
				statement.setString(12, info.getIsbn());
			}
			
			if(info.getSeries() == null){
				statement.setNull(13, java.sql.Types.NULL);
			}
			else{
				statement.setString(13, info.getSeries());
			}
			int editor_id = getEditorId(info.getEditor());
			if(editor_id == 0){
				System.out.println("Inconsistency Detected: Editor is not specified.");
				statement.setInt(14, java.sql.Types.NULL);
			}
			else{
				statement.setInt(14, editor_id);
			}
			int booktitle_id = getBookTitleId(info.getBookTitle());
			if(booktitle_id == 0){
				System.out.println("Inconsistency Detected: Booktitle is not specified.");
				statement.setInt(15, java.sql.Types.NULL);
			}
			else
			{
			statement.setInt(15, booktitle_id);
			}
			int genre_id = getGenreId(info.getGenre());
			if(genre_id == 0){
				System.out.println("Inconsistency Detected: Genre is not specified.");
				statement.setInt(16, java.sql.Types.NULL);
			}
			else
			{
				statement.setInt(16, genre_id);
			}
			int publisher_id = getPublisherId(info.getPublisher());
			if(publisher_id == 0){
				System.out.println("Inconsistency Detected: Publisher is not specified.");
				statement.setInt(17, java.sql.Types.NULL);
			}
			else
			{
				statement.setInt(17, publisher_id);
			}
			
			
			
			//statement.setInt(16, genre_id);
			//statement.setInt(17,publisher_id);
			//System.out.println(editor_id);
			//System.out.println(booktitle_id);
			//System.out.println(genre_id);
			//System.out.println(publisher_id);
			statement.executeUpdate();
			ResultSet rs = statement.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);
			System.out.println("SUCCESS! Document has been added, attempting to map to authors");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
	
			//System.out.println(e1);
		}
		
		
		
		
		
		return id;
	}
	
	
	private int getEditorId(String editor){
		int id = -1;
		PreparedStatement statement;
		if(editor == null){
			id = java.sql.Types.NULL;
		}
		else{
			try {
				statement = connection.prepareStatement("SELECT id FROM tbl_people WHERE name = ?");
				statement.setString(1, editor);
				//System.out.println(statement.toString());
				ResultSet rs = statement.executeQuery();
				if(rs.isBeforeFirst()){
					rs.next();
					id = rs.getInt("id");
				}
				else{
					statement = connection.prepareStatement("INSERT INTO tbl_people(name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
					statement.setString(1, editor);
					statement.executeUpdate();
					rs = statement.getGeneratedKeys();
					rs.next();
					id = rs.getInt(1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return id;
	}
	
	
	private int getBookTitleId(String booktitle){
		int id = -1;
		PreparedStatement statement;
		if(booktitle == null){
			id = java.sql.Types.NULL;
		}
		else{
			
			try {
				statement = connection.prepareStatement("SELECT id FROM tbl_booktitle WHERE title = ?");
				statement.setString(1, booktitle);
				ResultSet rs = statement.executeQuery();
				if(rs.isBeforeFirst()){
					rs.next();
					id = rs.getInt("id");
				}
				else{
					statement = connection.prepareStatement("INSERT INTO tbl_booktitle(title) VALUES (?)",Statement.RETURN_GENERATED_KEYS);
					statement.setString(1, booktitle);
					statement.executeUpdate();
					
					rs = statement.getGeneratedKeys();
					rs.next();
					id = rs.getInt(1);
				}
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return id;
	}
	
	
	
	private int getPublisherId(String publisher){
		int id = -1;
		PreparedStatement statement;
		if(publisher == null){
			id = java.sql.Types.NULL;
		}
		else{
			try {
				statement = connection.prepareStatement("SELECT id FROM tbl_publisher WHERE publisher_name = ?");
				statement.setString(1, publisher);
				ResultSet rs = statement.executeQuery();
				if(rs.isBeforeFirst()){
					rs.next();
					id = rs.getInt("id");
				}
				else{
					statement = connection.prepareStatement("INSERT INTO tbl_publisher(publisher_name) VALUES (?)",Statement.RETURN_GENERATED_KEYS);
					statement.setString(1, publisher);
					statement.executeUpdate();
					rs = statement.getGeneratedKeys();
					rs.next();
					id = rs.getInt(1);
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return id;
	}
	
	private int getGenreId(String genre){
		int id = -1;
		PreparedStatement statement;
		if(genre == null){
			id = java.sql.Types.NULL;
		}
		else{
			try {
				statement = connection.prepareStatement("SELECT id FROM tbl_genres WHERE genre_name = ?");
				statement.setString(1, genre);
				ResultSet rs = statement.executeQuery();
				if(rs.isBeforeFirst()){
					rs.next();
					id =rs.getInt("id");
				}
				else{
					statement = connection.prepareStatement("INSERT INTO tbl_genres(genre_name) VALUES (?)",Statement.RETURN_GENERATED_KEYS);
					statement.setString(1, genre);
					statement.executeUpdate();
					rs = statement.getGeneratedKeys();
					rs.next();
					id = rs.getInt(1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return id;
	}
	
	
	private void insertDocumentMap(int document_id,List<String> authors){
		PreparedStatement statement;
		
		if(document_id <= 0){
			System.out.println("Error: Cannot Add Document, please fix incosistent data");
			
		}
		else{
			try {
				statement = connection.prepareStatement("INSERT INTO tbl_author_document_mapping (doc_id,author_id) VALUES (?,?)");
				if(authors.size() == 0){
					System.out.println("No authors detected, cannot map document " + document_id+" to any author");
				}
				else{
					for(String author : authors){
					//System.out.println("AUTHOR: " + author);
						statement.setInt(1, document_id);
					//System.out.println(getEditorId(author));
						statement.setInt(2, getEditorId(author));
						statement.executeUpdate();
						
					}
					System.out.println("SUCCESS! Document " + document_id+" successfully mapped to all authors");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
}
