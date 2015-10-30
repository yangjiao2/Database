
import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.io.IOException;
import java.util.*;
public class XMLParser extends DefaultHandler {

	
	private String data;
	private String dtd;
	private SAXParser parser;
	private boolean inElement = false;
	private String tag = null;
	private DocumentInfo doc;
	
	
	private Article article;
	private Book book;
	private Incollection incollection;
	private Inproceedings inproceedings;
	private MastersThesis mastersthesis;
	private PhdThesis phdthesis;
	private Proceedings proceedings;
	private WWW www;
	private DatabaseInsert insert;
	
	public XMLParser(String data,String dtd){
		this.data = data;
		this.dtd = dtd;
		insert = new DatabaseInsert();
	}
	
	
	
	public void parseDocument(){
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try{
			parser = spf.newSAXParser();
			InputSource in = new InputSource(data);
			in.setEncoding("ISO-8859-1");
			parser.parse(in, this);
			insert.commit();
		}catch(SAXException se){
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void startElement(String uri,String localName,
			String qName,Attributes attributes) throws SAXException{
		if(qName.equals("article") && inElement == false){
			article = new Article(attributes.getValue("mdate"),attributes.getValue("key"));
			doc = new DocumentInfo();
			
			inElement = true;
		}
		else if(qName.equals("book") && inElement == false){
			book = new Book(attributes.getValue("mdate"),attributes.getValue("key"));
			doc = new DocumentInfo();
			
			inElement = false;
		}
		else if(qName.equals("incollection") && inElement == false){
			incollection = new Incollection(attributes.getValue("mdate"),attributes.getValue("key"));
			doc = new DocumentInfo();
			
			inElement = false;
		}
		else if(qName.equals("inproceedings") && inElement == false){
			inproceedings = new Inproceedings(attributes.getValue("mdate"),attributes.getValue("key"));
			doc = new DocumentInfo();
			
			inElement = false;
		}
		else if(qName.equals("mastersthesis") && inElement == false){
			mastersthesis = new MastersThesis(attributes.getValue("mdate"),attributes.getValue("key"));
			doc = new DocumentInfo();
			
			inElement = false;
		}
		else if(qName.equals("phdthesis") && inElement == false){
			phdthesis = new PhdThesis(attributes.getValue("mdate"),attributes.getValue("key"));
			doc = new DocumentInfo();
			
			inElement = false;
		}
		else if(qName.equals("proceedings") && inElement == false){
			proceedings = new Proceedings(attributes.getValue("mdate"),attributes.getValue("key"));
			doc = new DocumentInfo();
			
			inElement = false;
		}
		else if(qName.equals("www") && inElement == false){
			www = new WWW(attributes.getValue("mdate"),attributes.getValue("key"));
			doc = new DocumentInfo();
			
			inElement = false;
		}
	}
	
	
	 public void endElement(String uri, String localName, String qName) 
	 throws SAXException{
		 //scan for document info
		 if(qName.equals("title")){
			 doc.setTitle(tag);
			// System.out.println("	Title : " + tag);
		 }
		 else if(qName.equals("year")){
			 doc.setYear(tag);
			 //System.out.println("	Year : " + tag);
		 }
		 else if(qName.equals("volume")){
			 doc.setVolume(tag);
			// System.out.println("	Volume : " + tag);
		 }
		 else if(qName.equals("number")){
			 doc.setNumber(tag);
			 //System.out.println("	Number : " + tag);
		 }
		 else if(qName.equals("url")){
		 	 doc.setUrl(tag);
		 	//System.out.println("	url : " + tag);
		 }
		 else if(qName.equals("ee")){
		 	 doc.setEe(tag);
		 	//System.out.println("	ee : " + tag);
		 }
		 else if(qName.equals("cdrom")){
			 doc.setCdrom(tag);
			// System.out.println("	cdrom : " + tag);
		 }
		 else if(qName.equals("crossref")){
		 	 doc.setCrossref(tag);
		 	//System.out.println("	crossref : " + tag);
		 }
		 else if(qName.equals("cite")){
			 doc.setCite(tag);
			// System.out.println("	cite : " + tag);
		 }
		 else if(qName.equals("isbn")){
		 	 doc.setIsbn(tag);
		 	//System.out.println("	isbn : " + tag);
		 }
		 else if(qName.equals("series")){
		 	 doc.setSeries(tag);
		 	//System.out.println("	series : " + tag);
		 }
		 else if(qName.equals("editor")){
		 	 doc.setEditor(tag);
		 	//System.out.println("	editor : " + tag);
		 }
		 else if(qName.equals("pages")){
		 	 doc.setPages(tag);
		 	//System.out.println("	pages : " + tag);
		 }
		 else if(qName.equals("booktitle")){
		 	 doc.setBookTitle(tag);
		 	//System.out.println("	booktitle : " + tag);
		 }
		 else if(qName.equals("publisher")){
		 	 doc.setPublisher(tag);
		 	//System.out.println("	publisher : " + tag);
		 }
		 else if(qName.equals("genre")){
		 	 doc.setGenre(tag);
		 	//System.out.println("	genre : " + tag);
		 }
		 else if(qName.equals("author")){
			 doc.addAuthor(tag);
			// System.out.println("	author : " + tag);
		 }
		 //scan for genre end tag
		 if(qName.equals("book")) {
			 book.setDocumentInfo(doc);
			 //System.out.println("End of book, inserting....");
			 insert.addBook(book);
			 inElement = false;
		 } else if(qName.equals("article")) {
			 //System.out.println("End of article, inserting....");
			 article.setDocumentInfo(doc);
			 insert.addArticle(article);
			 inElement = false;
		 } else if(qName.equals("inproceedings")) {
			 inproceedings.setDocumentInfo(doc);
			 insert.addInproceedings(inproceedings);
			// System.out.println("End of inproceedings, inserting....");
			 inElement = false;
		 } else if(qName.equals("proceedings")) {
			 proceedings.setDocumentInfo(doc);
			 insert.addProceedings(proceedings);
			 //System.out.println("End of proceedings, inserting....");
			 inElement = false;
		 } else if(qName.equals("incollection")) {
			 incollection.setDocumentInfo(doc);
			 insert.addIncollection(incollection);
			// System.out.println("End of incollection, inserting....");
			 
			 inElement = false;
		 } else if(qName.equals("phdthesis")) {
			 phdthesis.setDocumentInfo(doc);
			 insert.addPhdThesis(phdthesis);
			// System.out.println("End of phdthesis, inserting....");
			 inElement = false;
		 } else if(qName.equals("mastersthesis")) {
			 mastersthesis.setDocumentInfo(doc);
			 insert.addMastersThesis(mastersthesis);
			// System.out.println("End of mastersthesis, inserting....");
			 inElement = false;
		 } else if(qName.equals("www")) {
			// System.out.println("Set Text: " + tag);
			 www.setDocumentInfo(doc);
			 insert.addWWW(www);
			 //System.out.println("End of www, inserting....");
			 inElement = false;
		 }
	 }
	 
	 public void characters(char[] ch, int start, int length) 
	 throws SAXException{
		 StringBuilder s = new StringBuilder();
		 for(int i = start; i <= length - 1; i++) {
			 s.append(ch[i]);
			
			 
		 }
		// if(tagValue.isEmpty() || tagValue.) {
		//	 System.out.println("is empty");
		 //}
		 tag = new String(ch,start,length);
		 tag = tag.trim();
		 //System.out.println("length " +new String(ch,start,length));
	 }
}
