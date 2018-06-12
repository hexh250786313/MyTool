package MainWin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BoxLayout;
//import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class MyWordViewer {

	private boolean flag;
	private final String DEFAULT_XML_URL = "./file/MyVocabulary.xml";
	private JFrame frame;
	private JTable table;
	private JScrollPane jp;
	private Vector<Vector<String>> data;
	private Vector<String> title;
	private ArrayList<MyVocabulary> vocabulary;
	//private ArrayList<MyVocabulary> cloneVocabulary;
	private Iterator<MyVocabulary> listIterator;
	//private Iterator<MyVocabulary> cloneListIterator;
	private DefaultTableModel model;
	
	
	/*
	public static void main(String[] args) {
		//swing创建表格测试
		MyWordViewer test = new MyWordViewer();
		test.gui();
	}
	
	*/
	
	void gui() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu optionMenu = new JMenu("Option");
		JMenuItem newMenuItem = new JMenuItem("New");
		JMenuItem shuffleMenuItem = new JMenuItem("Shuffle");
		JMenuItem refreshMenuItem = new JMenuItem("Refresh");
		
		flag = true;
		//container = new JPanel();
		//panel = new JPanel();
		title = new Vector<String>();
		frame = new JFrame("MyVocabulary");
		
		refreshMenuItem.addActionListener(new RefreshListener());
		newMenuItem.addActionListener(new NewMenuListener());
		shuffleMenuItem.addActionListener(new ShuffleListener());
		
		optionMenu.add(shuffleMenuItem);
		fileMenu.add(newMenuItem);
		fileMenu.add(refreshMenuItem);
		menuBar.add(fileMenu);
		menuBar.add(optionMenu);
		
		title.add("Word");
		title.add("Translation");
		
		readMyVocabulary();
		setVocabulary();
		
		model = new  DefaultTableModel(data, title);
		table = new JTable(model){  
			private static final long serialVersionUID = 6401295888790008991L;
			public boolean isCellEditable(int row,int col){ 
				return false;
			}
		};
		setTableSize();
		
		MouseListener listener = new ClickMouseEvent(model);
		table.addMouseListener(listener);
        
		jp = new JScrollPane(table);
        
		//panel.add(jp);
		//container.add(panel);
		frame.setJMenuBar(menuBar);
		//frame.add(jp);
        frame.add(jp);
        frame.setSize(new Dimension(280,297));
        frame.setResizable(false);
        //frame.setMinimumSize(new Dimension(200, 300));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        //frame.pack();
        frame.setVisible(true);
		
	}
	
	public class NewMenuListener implements ActionListener {
		
		public void actionPerformed(ActionEvent ev) { 
			WordCardBuilder builder = new WordCardBuilder();
			builder.go();
       }
	}
	
	public class ClickMouseEvent implements MouseListener {

		DefaultTableModel model;
		
		public ClickMouseEvent (DefaultTableModel model) {
			this.model = model;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			
			if(e.getClickCount() == 2) {
				int row = ((JTable) e.getSource()).rowAtPoint(e.getPoint());
				//int col = ((JTable) e.getSource()).columnAtPoint(e.getPoint());
				if(flag) 
					wordCard(row);
					//String cellVal = (String)(model.getValueAt(row, col));
					//JOptionPane.showMessageDialog(null, "" + (row + 1), "Title", JOptionPane.WARNING_MESSAGE); 
				else 
					shuffleWordCard(row);
			}else return;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {	
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

	}
	
	public class RefreshListener implements ActionListener{
		
		public void actionPerformed(ActionEvent ev) { 
			flag = true;
			model.getDataVector().clear();
					
			readMyVocabulary();
			setVocabulary();

			model.setDataVector(data, title);
			
			setTableSize();

       }
	}

	public class ShuffleListener implements ActionListener{
		
		public void actionPerformed(ActionEvent ev) { 
			
			flag = false;
			//cloneVocabulary = new ArrayList<MyVocabulary>();
			//cloneVocabulary = (ArrayList<MyVocabulary>) vocabulary.clone();
			Collections.shuffle(vocabulary);
			listIterator = vocabulary.iterator();
			
			model.getDataVector().clear();
			setVocabulary();
			model.setDataVector(data, title);

			setTableSize();
       }
	}
	
	public class DeleteWordListener implements ActionListener{
		
		int i;
		JFrame cardFrame;
		
		public DeleteWordListener(int i, JFrame cardFrame) {
			this.i = i; 
			this.cardFrame = cardFrame;
		}
		
		public void actionPerformed(ActionEvent e) {
			
			SAXReader reader = new SAXReader();
    		
    		try {
    			Document document = reader.read(new File(DEFAULT_XML_URL));

				int ifDelete = JOptionPane.showConfirmDialog(null, "移除？", "注意",JOptionPane.YES_NO_OPTION); 
	    		if(ifDelete == JOptionPane.YES_OPTION) {
	    			
	    			Element vcblrlist = document.getRootElement();
					Element vcblr = vcblrlist.elements("vcblr").get(i);
					//JOptionPane.showMessageDialog(null, todo.element("item").getText(), "Title", JOptionPane.ERROR_MESSAGE); 
					vcblrlist.remove(vcblr);
	    		}
    			//回写操作
	    		OutputFormat format = OutputFormat.createPrettyPrint();
				format.setEncoding("utf-8");
				XMLWriter xmlWriter = new XMLWriter(
						new FileOutputStream(DEFAULT_XML_URL),format);
				xmlWriter.write(document);
				xmlWriter.close();
    		}catch (DocumentException | IOException ev) {
	    		ev.printStackTrace();
	    	}
			cardFrame.dispose();
			flag = true;
			model.getDataVector().clear();
					
			readMyVocabulary();
			setVocabulary();

			model.setDataVector(data, title);
			
			setTableSize();
		}
	}

	public class EditWordListener implements ActionListener{
		
		int i;
		JFrame cardFrame;
		
		public EditWordListener(int i, JFrame cardFrame) {
			this.i = i;
			this.cardFrame = cardFrame;
		}
		
		public void actionPerformed(ActionEvent e) {
			WordInfoEditor editor = new WordInfoEditor(i);
			editor.go();
			cardFrame.dispose();
		}
	}
	
	private void readMyVocabulary() {
		//把XML的单词数据全部都储存到vocabulary对象组里
		vocabulary = new ArrayList<MyVocabulary>();
    	SAXReader reader = new SAXReader();
    	
    	try {
    		Document document = reader.read(new File(DEFAULT_XML_URL));
    		Element vcblrlist = document.getRootElement();
    		Iterator<Element> vocabularyIterator = vcblrlist.elementIterator();
    		
    		
    		//分别读取每个todo，把item的值赋给toDo，然后加入到toDoList中
    		while (vocabularyIterator.hasNext()) { 
    			Element vcblr = (Element) vocabularyIterator.next();
    			boolean flag = false;
    			//这里面开始是每一个word
    			Iterator<Element> wordIterator = vcblr.elementIterator();
    			MyVocabulary myVocabulary = new MyVocabulary(((Element)wordIterator.next()).getText());
    		    
    			while(wordIterator.hasNext()) {
    				if(flag) {
    					Element tp = (Element) wordIterator.next();
    					if((tp.attribute(0).getText().equals("1"))) 
    						myVocabulary.addTranslation(tp.getText());
    					else if(tp.attribute(0).getText().equals("2")) 
    						myVocabulary.addPhrase(tp.getText());
    					else if(tp.attribute(0).getText().equals("3")) 
    						myVocabulary.addExpSts(tp.getText());
    				}else
    					flag = true;
    			}
    			vocabulary.add(myVocabulary);
    		}
    	}catch (DocumentException e) {
    		e.printStackTrace();
    	}

    	//Collections.shuffle(vocabulary);
		//创建迭代器，分别读取每一个单词的信息
		listIterator = vocabulary.iterator();
	}
	
	private void setVocabulary() {
		
		data = new Vector<Vector<String>>();
		
		while(listIterator.hasNext()) {
			
			MyVocabulary myvocabulary = (MyVocabulary)listIterator.next();
			
			Vector<String> oneData = new Vector<String>();
			String str = "";
			
			//获取翻译，把每一个翻译都连接起来
			for(String trans : myvocabulary.getTranslation()) {
				str = str + trans +  "; " ;
			}
			oneData.add(myvocabulary.getWord());
			oneData.add(str);
	
			data.add(oneData);
		}
		//JOptionPane.showMessageDialog(null, data.get(0).get(0), "Title", JOptionPane.WARNING_MESSAGE);
	}

	private void wordCard(int i) {
		

		MyVocabulary myvocabulary = vocabulary.get(i);
		String word = myvocabulary.getWord();
		
		JMenu menu = new JMenu("Option");
		JMenuItem deleteItem = new JMenuItem("Delete");
		JMenuItem editItem = new JMenuItem("Edit");
		JMenuBar menuBar = new JMenuBar();
		JPanel centrePanel = new JPanel();
		JFrame cardFrame = new JFrame("Information of __" + word + "___");
		JPanel cardPanel = new JPanel();
		JLabel wordLabel = new JLabel(word);
		int m = 1;
		int n = 1;
		
		deleteItem.addActionListener(new DeleteWordListener(i, cardFrame));
		editItem.addActionListener(new EditWordListener(i, cardFrame));
		
		JLabel blue = new JLabel("【释义】");
		blue.setForeground(Color.BLUE);
		JLabel green = new JLabel("【词组】");
		green.setForeground(new Color(0, 100, 0));
		JLabel red = new JLabel("【例句】");
		red.setForeground(new Color(139, 58, 58));
		
		wordLabel.setFont(new Font("",1,17));
		
		centrePanel.setLayout(new BorderLayout());
		
		cardPanel.add(wordLabel);
		cardPanel.add(new JLabel(" "));
		cardPanel.add(blue);
		for(String trans : myvocabulary.getTranslation()) {
			JLabel translation = new JLabel(trans);
			cardPanel.add(translation);
		}
		cardPanel.add(new JLabel(" "));
		cardPanel.add(green);
		for(String phr : myvocabulary.getPhrase()) {
			JLabel phrase = new JLabel(m + ".  " + phr);
			cardPanel.add(phrase);
			m++;
		}
		cardPanel.add(new JLabel(" "));
		cardPanel.add(red);
		for(String sts : myvocabulary.getExpSts()) {
			JLabel expsts = new JLabel(n + ".  " + sts);
			cardPanel.add(expsts);
			n++;
		}
		
		menuBar.add(menu);
		menu.add(deleteItem);
		menu.add(editItem);
		
		centrePanel.add(new JLabel("     "), BorderLayout.WEST);
		centrePanel.add(new JLabel("     "), BorderLayout.EAST);
		centrePanel.add(new JLabel("     "), BorderLayout.NORTH);
		centrePanel.add(new JLabel("     "), BorderLayout.SOUTH);
		
		
		cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
		
		centrePanel.add(cardPanel, BorderLayout.CENTER);
		cardFrame.setJMenuBar(menuBar);
		cardFrame.add(centrePanel);
		cardFrame.setLocationRelativeTo(null);
		cardFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		cardFrame.setResizable(false);
		cardFrame.setMinimumSize(new Dimension(200, 50));
		cardFrame.pack();
		cardFrame.setVisible(true);
	}

	private void shuffleWordCard(int i) {
		MyVocabulary myvocabulary = vocabulary.get(i);
		String word = myvocabulary.getWord();
		
		JMenuBar menuBar = new JMenuBar();
		JPanel centrePanel = new JPanel();
		JFrame cardFrame = new JFrame("Information of __" + word + "___");
		JPanel cardPanel = new JPanel();
		JLabel wordLabel = new JLabel(word);
		int m = 1;
		int n = 1;
		
		JLabel blue = new JLabel("【释义】");
		blue.setForeground(Color.BLUE);
		JLabel green = new JLabel("【词组】");
		green.setForeground(new Color(0, 100, 0));
		JLabel red = new JLabel("【例句】");
		red.setForeground(new Color(139, 58, 58));
		
		wordLabel.setFont(new Font("",1,17));
		
		centrePanel.setLayout(new BorderLayout());
		
		cardPanel.add(wordLabel);
		cardPanel.add(new JLabel(" "));
		cardPanel.add(blue);
		for(String trans : myvocabulary.getTranslation()) {
			JLabel translation = new JLabel(trans);
			cardPanel.add(translation);
		}
		cardPanel.add(new JLabel(" "));
		cardPanel.add(green);
		for(String phr : myvocabulary.getPhrase()) {
			JLabel phrase = new JLabel(m + ".  " + phr);
			cardPanel.add(phrase);
			m++;
		}
		cardPanel.add(new JLabel(" "));
		cardPanel.add(red);
		for(String sts : myvocabulary.getExpSts()) {
			JLabel expsts = new JLabel(n + ".  " + sts);
			cardPanel.add(expsts);
			n++;
		}
		
		centrePanel.add(new JLabel("     "), BorderLayout.WEST);
		centrePanel.add(new JLabel("     "), BorderLayout.EAST);
		centrePanel.add(new JLabel("     "), BorderLayout.NORTH);
		centrePanel.add(new JLabel("     "), BorderLayout.SOUTH);
		
		
		cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
		
		centrePanel.add(cardPanel, BorderLayout.CENTER);
		cardFrame.setJMenuBar(menuBar);
		cardFrame.add(centrePanel);
		cardFrame.setLocationRelativeTo(null);
		cardFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		cardFrame.setResizable(false);
		cardFrame.setMinimumSize(new Dimension(200, 50));
		cardFrame.pack();
		cardFrame.setVisible(true);
	}
	
	private void setTableSize() {
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(80);
		table.getColumnModel().getColumn(1).setPreferredWidth(166); //181, 166
		//table.getColumnModel().getColumn(0).setResizable(false);
		
	}
	
	
}
