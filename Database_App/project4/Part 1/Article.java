
public class Article {

	private String mdate;
	private String key;
	private DocumentInfo info;
	
	public Article(String mdate,String key){
		this.mdate = mdate;
		this.key = key;
		
	}
	
	
	public String getMdate(){
		return mdate;
	}
	
	public String getKey(){
		return key;
	}
	
	public void setDocumentInfo(DocumentInfo info){
		this.info = info;
	}
	
	public DocumentInfo getInfo(){
		return info;
	}
}
