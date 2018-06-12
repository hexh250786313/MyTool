package MainWin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

//import MainWin.ToDoListViewer.OpenButtonListener;


public class ItemBuilder {
	
	//private ArrayList<ToDoList> toDoList;
	private String DEFAULT_XML_URL = "./file/ToDoList.xml";
	private JTextField itemText;
    private JFrame frame;
	
	void go() {
		//JOptionPane.showMessageDialog(null, "success", "Title", JOptionPane.ERROR_MESSAGE);
		frame = new JFrame("ItemBuilder");
		itemText = new JTextField();
		JPanel mainPanel = new JPanel();
		JPanel topPanel = new JPanel(); 
		JButton createButton = new JButton("create");
		
		frame.setLayout(new BorderLayout());
		
		createButton.setPreferredSize(new Dimension(71, 20));
		
		JScrollPane iScroller = new JScrollPane(itemText);
	    iScroller.setVerticalScrollBarPolicy(
	              ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
	    iScroller.setHorizontalScrollBarPolicy(
	              ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
	    topPanel.setPreferredSize(new Dimension(1,3));
	    itemText.setPreferredSize(new Dimension(200,20));
	    
		createButton.addActionListener(new NewItemListener());
		itemText.addActionListener(new NewItemListener());
	    
		mainPanel.add(new JLabel("Item:"));
		mainPanel.add(iScroller);
		mainPanel.add(createButton);
		frame.add(mainPanel, BorderLayout.CENTER);
		frame.add(topPanel, BorderLayout.NORTH);
		
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setSize(new Dimension(370,73));
		//frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	public class NewItemListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			String str = new ToDoList(itemText.getText()).getItem();
			
			if(str.equals("")) {
				JOptionPane.showMessageDialog(null, "ÊäÈë¿òÎª¿Õ", "WARNING", JOptionPane.WARNING_MESSAGE);
			}else {
				saveItem(str);
			}
			
			//toDoList.add(card);
			clearTextArea();
		}
	}
	
	private void saveItem(String itemStr) {
		
		SAXReader reader = new SAXReader();
		
		try {
			Document document = reader.read(new File(DEFAULT_XML_URL));
	    	Element root = document.getRootElement();
	          
	    	Element todo = root.addElement("todo");
	    	Element item = todo.addElement("item");
	    	item.setText(itemStr);
	    	  
	          
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
	
	private void clearTextArea() {
		itemText.setText("");
	    itemText.requestFocus();
	}

}
