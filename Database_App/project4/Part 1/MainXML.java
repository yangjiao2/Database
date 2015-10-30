import java.io.File;


public class MainXML {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File dtd = new File("C:/Users/Kunal/Desktop/dblp-data-big/dblp.dtd");
		File data = new File("C:/Users/Kunal/Desktop/dblp-data-big/dblp-data.xml");
		
		XMLParser parser = new XMLParser("C:/Users/Kunal/Desktop/dblp-data-small/final-data.xml","C:/Users/Kunal/Desktop/dblp-data-small/dblp.dtd");
		parser.parseDocument();
	}

}
