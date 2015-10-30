import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class DocumentInfo {
	
	
	private List<String> authors;
	private String title;
	private String year;
	private String volume;
	private String number;
	private String url;
	private String ee;
	private String cdrom;
	private String cite;
	private String crossref;
	private String isbn;
	private String series;
	private String pages;
	private String editor;
	private String booktitle;
	private String publisher;
	private String genre;
	
	private String startPage;
	private String endPage;
	
	public DocumentInfo(){
		authors = new ArrayList<String>();
	}
	
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setYear(String year){
		this.year = year;
	}
	
	public String getYear(){
		return year;
	}
	
	public void setVolume(String volume){
		this.volume = volume;
	}
	
	public String getVolume(){
		return volume;
	}
	
	public void setPages(String pages){
		this.pages = pages;
		parsePages(this.pages);
	}
	
	public String getPages(){
		return pages;
	}
	
	public void setNumber(String number){
		this.number = number;
	}
	
	public String getNumber(){
		return number;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public String getUrl(){
		return url;
	}
	
	public void setEe(String ee){
		this.ee = ee;
	}
	
	public String getEe(){
		return ee;
	}
	
	public void setCdrom(String cdrom){
		this.cdrom = cdrom;
	}
	
	public String getCdrom(){
		return cdrom;
	}
	
	public void setCite(String cite){
		this.cite = cite;
	}
	
	public String getCite(){
		return cite;
	}
	
	public void setCrossref(String crossref){
		this.crossref = crossref;
	}
	
	public String getCrossref(){
		return crossref;
	}
	
	public void setIsbn(String isbn){
		this.isbn = isbn;
	}
	
	public String getIsbn(){
		return isbn;
	}
	
	public void setSeries(String series){
		this.series = series;
	}
	
	public String getSeries(){
		return series;
	}
	
	public void setEditor(String editor){
		this.editor = editor;
	}
	
	public String getEditor(){
		return editor;
	}
	
	public void setBookTitle(String booktitle){
		this.booktitle = booktitle;
	}
	
	public String getBookTitle(){
		return booktitle;
	}
	
	public void setGenre(String genre){
		this.genre = genre;
	}
	
	public String getGenre(){
		return genre;
	}
	
	public void setPublisher(String publisher){
		this.publisher = publisher;
	}
	
	public String getPublisher(){
		return publisher;
	}
	
	public void addAuthor(String author){
		this.authors.add(author);
	}
	
	public List<String> getAuthors(){
		return authors;
	}
	public String getStartPage(){
		return startPage;
	}
	public String getEndPage(){
		return endPage;
	}
	
	
	private void parsePages(String pages){
		try{
			startPage = pages.substring(0, pages.indexOf('-'));
			endPage = pages.substring(pages.indexOf('-')+1, pages.length());
		}catch(StringIndexOutOfBoundsException e){
			System.out.println("Pages not properly formatted");
		}
			
		
		
	}
	
	
	
	
}
