package MainWin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class DrinkWater {

	private static final String DEFAULT_XML_URL = "./file/ToDoList.xml";

	void go() {
		saveDrinkItem();
	}
	
	private void saveDrinkItem() {
			
			SAXReader reader = new SAXReader();
			
			try {
				Document document = reader.read(new File(DEFAULT_XML_URL));
		    	Element root = document.getRootElement();
		          
		    	for(int i = 1; i < 9; i++) {
		    		Element todo = root.addElement("todo");
			    	Element item = todo.addElement("item");
			    	item.setText("No. " + i + " Drink.");
		    	}

				OutputFormat format = OutputFormat.createPrettyPrint();
				format.setEncoding("utf-8");
				XMLWriter xmlWriter = new XMLWriter(
						new FileOutputStream(DEFAULT_XML_URL),format);
				xmlWriter.write(document);
				xmlWriter.close();
				
			} catch (DocumentException | IOException e) {
				e.printStackTrace();
			}
			
	}

}
