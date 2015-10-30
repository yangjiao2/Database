
public class Error {
	
	public String body;
	public int count;
	public Error(String body,int count){
		this.body = body;
		this.count = count;
	}
	
	
	public String getBody(){
		return body;
	}
	
	public int getCount(){
		return count;
	}
	
	public void setBody(String s){
		body = s;
	}
	
	public void setCount(int a){
		count = a;
	}

}
