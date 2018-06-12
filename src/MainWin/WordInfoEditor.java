package MainWin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.event.ComponentAdapter;
//import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class WordInfoEditor {
	
	private int count;
	private static Dimension dismension = new Dimension(150, 20);
	private final String DEFAULT_XML_URL = "./file/MyVocabulary.xml";
	private JFrame frame;
	private JPanel panel;
	private JPanel centrePanel;
	private JTextField wordText;
	private JButton addTrans;
	private JButton addPhrase;
	private JButton addExp;
	private JButton newButton;
	private MyVocabulary vocabulary;
	
	private ArrayList<JTextField> transTextList;
	private ArrayList<JTextField> phraseTextList;
	private ArrayList<JTextField> expTextList;
	
	private int i;
	
	public WordInfoEditor(int i) {
		this.i = i;
	}
	
	void go() {
		
		count = 0;
		transTextList = new ArrayList<JTextField>();
		phraseTextList = new ArrayList<JTextField>();
		expTextList = new ArrayList<JTextField>();
		
		frame = new JFrame("Editor");
		panel = new JPanel();
		centrePanel = new JPanel();
		wordText = new JTextField();
		addTrans = new JButton("Add Translation");
		addPhrase = new JButton("Add Phrase");
		addExp = new JButton("Add Example");
		newButton = new JButton("NEW");
		
		centrePanel.setLayout(new BorderLayout());
		
		addTrans.setPreferredSize(dismension);
		addPhrase.setPreferredSize(dismension);
		addExp.setPreferredSize(dismension);
		wordText.setPreferredSize(dismension);
		
		readXMLFile();
		setText();
		
		addTrans.addActionListener(new addTransListener());
		addPhrase.addActionListener(new addPhraseListener());
		addExp.addActionListener(new addExpListener());
		newButton.addActionListener(new saveCardListener());
		
		centrePanel.add(panel, BorderLayout.CENTER);
		centrePanel.add(newButton, BorderLayout.SOUTH);
		frame.add(centrePanel);
		
		//frame.pack();
		frame.setResizable(false);
		frame.setSize(new Dimension(200, 420 + count));
		//frame.setMinimumSize(new Dimension(200, 430));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public class addTransListener implements ActionListener{
	
		public void actionPerformed(ActionEvent ev){

			int flag = transTextList.size();
			
			if(transTextList.get(flag - 1).getText().equals("")) {
				JOptionPane.showMessageDialog(null, "输入框为空", "WARNING", JOptionPane.WARNING_MESSAGE);
			}else {
				count = count + 25;

				panel.removeAll();
				panel.add(new JLabel("——WORD——"));
				panel.add(wordText);
				panel.add(new JLabel("                                     "));
				
				panel.add(new JLabel("——TRANSLATION——"));
				for(int i = 0; i < transTextList.size(); i++) {
					panel.add(transTextList.get(i));
				}
				panel.add(setTextField(1, ""));
				transTextList.get(flag).requestFocus();
				panel.add(addTrans);
				panel.add(new JLabel("                                     "));
				
				panel.add(new JLabel("——PHRASE——"));
				for(int i = 0; i < phraseTextList.size(); i++) {
					panel.add(phraseTextList.get(i));
				}
				//panel.add(getTextField(2));
				panel.add(addPhrase);
				panel.add(new JLabel("                                     "));
				
				panel.add(new JLabel("——EXAMPLE——"));
				for(int i = 0; i < expTextList.size(); i++) {
						panel.add(expTextList.get(i));
				}
				//panel.add(getTextField(3));
				panel.add(addExp);

				panel.repaint();
				frame.setSize(200, 420 + count);
			}
		}
	}
	
	public class addPhraseListener implements ActionListener{
		
		public void actionPerformed(ActionEvent ev){
			
			int flag = phraseTextList.size();
			
			if(phraseTextList.get(flag - 1).getText().equals("")) {
				JOptionPane.showMessageDialog(null, "输入框为空", "WARNING", JOptionPane.WARNING_MESSAGE);
			}else {
				count = count + 25;
				
				panel.removeAll();
				panel.add(new JLabel("——WORD——"));
				panel.add(wordText);
				panel.add(new JLabel("                                     "));
				
				panel.add(new JLabel("——TRANSLATION——"));
				for(int i = 0; i < transTextList.size(); i++) {
					panel.add(transTextList.get(i));
				}
				//panel.add(setTextField(1, ""));
				panel.add(addTrans);
				panel.add(new JLabel("                                     "));
				
				panel.add(new JLabel("——PHRASE——"));
				for(int i = 0; i < phraseTextList.size(); i++) {
					panel.add(phraseTextList.get(i));
				}
				panel.add(setTextField(2, ""));
				phraseTextList.get(flag).requestFocus();
				panel.add(addPhrase);
				panel.add(new JLabel("                                     "));
				
				panel.add(new JLabel("——EXAMPLE——"));
				for(int i = 0; i < expTextList.size(); i++) {
						panel.add(expTextList.get(i));
				}
				//panel.add(getTextField(3, ""));
				panel.add(addExp);
				
				panel.repaint();
				frame.setSize(200, 420 + count);
			}	
		}
	}
	
	public class addExpListener implements ActionListener{
		
		public void actionPerformed(ActionEvent ev){
			
			int flag = expTextList.size();
			
			if(expTextList.get(flag - 1).getText().equals("")) {
				JOptionPane.showMessageDialog(null, "输入框为空", "WARNING", JOptionPane.WARNING_MESSAGE);
			}else {
				count = count + 25;
				
				panel.removeAll();
				panel.add(new JLabel("——WORD——"));
				panel.add(wordText);
				panel.add(new JLabel("                                     "));
				
				panel.add(new JLabel("——TRANSLATION——"));
				for(int i = 0; i < transTextList.size(); i++) {
					panel.add(transTextList.get(i));
				}
				//panel.add(setTextField(1, ""));
				panel.add(addTrans);
				panel.add(new JLabel("                                     "));
				
				panel.add(new JLabel("——PHRASE——"));
				for(int i = 0; i < phraseTextList.size(); i++) {
					panel.add(phraseTextList.get(i));
				}
				//panel.add(setTextField(2, ""));
				panel.add(addPhrase);
				panel.add(new JLabel("                                     "));
				
				panel.add(new JLabel("——EXAMPLE——"));
				for(int i = 0; i < expTextList.size(); i++) {
						panel.add(expTextList.get(i));
				}
				panel.add(setTextField(3, ""));
				expTextList.get(flag).requestFocus();
				panel.add(addExp);
				
				panel.repaint();
				frame.setSize(200, 420 + count);
			}
		}
	}
	
	public class saveCardListener implements ActionListener{
		
		public void actionPerformed(ActionEvent ev){
			
			if(wordText.getText().length() < 1) {
				JOptionPane.showMessageDialog(null, "请输入单词信息", "WARNING", JOptionPane.WARNING_MESSAGE);
			}else {
				int ifAdd = JOptionPane.showConfirmDialog(null, "确认修改？", "注意",JOptionPane.YES_NO_OPTION); 
	    		if(ifAdd == JOptionPane.YES_OPTION) {
	    			cutTheEmptyText();
	    			removeCard();
	    			saveCard();
	    			JOptionPane.showMessageDialog(null, "——SUCESS——!", "WARNING", JOptionPane.WARNING_MESSAGE);
	    			frame.dispose();
	    		}
			}
		}
	}
	
	private void readXMLFile() {
		
		SAXReader reader = new SAXReader();
		
		try {
			
			Document document = reader.read(new File(DEFAULT_XML_URL));
    		Element vcblrlist = document.getRootElement();
    		Element vcblr = vcblrlist.elements("vcblr").get(i);
    		Iterator<Element> vcblrIterator = vcblr.elementIterator();
    		boolean flag = false;
    		
    		vocabulary = new MyVocabulary(((Element)vcblrIterator.next()).getText());
    		
    		while(vcblrIterator.hasNext()) {
				if(flag) {
					Element tp = (Element) vcblrIterator.next();
					if((tp.attribute(0).getText().equals("1"))) 
						vocabulary.addTranslation(tp.getText());
					else if(tp.attribute(0).getText().equals("2")) 
						vocabulary.addPhrase(tp.getText());
					else if(tp.attribute(0).getText().equals("3")) 
						vocabulary.addExpSts(tp.getText());
				}else
					flag = true;
			}
		}catch(DocumentException e) {
			e.printStackTrace();
		}
	}
	
	private void setText() {
		
		
		//为单词文本栏添加读取到的单词
		panel.add(new JLabel("——WORD——"));
		wordText.setText(vocabulary.getWord());
		panel.add(wordText);
		panel.add(new JLabel("                                     "));
		
		//为翻译文本栏添加读取到的翻译
		panel.add(new JLabel("——TRANSLATION——"));
		for(String str:vocabulary.getTranslation()) {
			panel.add(setTextField(1, str));
			count = count + 25;
		}
		panel.add(setTextField(1, ""));
		panel.add(addTrans);
		panel.add(new JLabel("                                     "));
		
		//为词组文本栏添加读取到的词组
		panel.add(new JLabel("——PHRASE——"));
		for(String str:vocabulary.getPhrase()) {
			panel.add(setTextField(2, str));
			count = count + 25;
		}
		panel.add(setTextField(2, ""));
		panel.add(addPhrase);
		panel.add(new JLabel("                                     "));
		
		//为词组文本栏添加读取到的词组
		panel.add(new JLabel("——EXAMPLE——"));
		for(String str:vocabulary.getExpSts()) {
			panel.add(setTextField(3, str));
			count = count + 25;
		}
		panel.add(setTextField(3, ""));
		panel.add(addExp);
		panel.add(new JLabel("                                     "));
		
	}
	
	private JTextField setTextField(int i, String str) {
		JTextField textField = new JTextField();
		textField.setPreferredSize(dismension);
		textField.setText(str);
		if(i == 1) {
			transTextList.add(textField);
		}else if(i == 2) {
			phraseTextList.add(textField);
		}else if(i == 3) {
			expTextList.add(textField);
		}
		return textField;
	}
	
	private void cutTheEmptyText() {
		for(int i = 0; i < transTextList.size(); i++) {
			if(transTextList.get(i).getText().length() < 1) transTextList.remove(i);
		}
		
		for(int i = 0; i < phraseTextList.size(); i++) {
			if(phraseTextList.get(i).getText().length() < 1) phraseTextList.remove(i);
		}
		
		for(int i = 0; i < expTextList.size(); i++) {
			if(expTextList.get(i).getText().length() < 1) expTextList.remove(i);
		}
	}

	private void removeCard() {
		
		SAXReader reader = new SAXReader();
		
		try {
			Document document = reader.read(new File(DEFAULT_XML_URL));
	    	Element vcblrlist = document.getRootElement();
	    	Element vcblr = vcblrlist.elements("vcblr").get(i);
			vcblrlist.remove(vcblr);
	          
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
	
	private void saveCard() {

		SAXReader reader = new SAXReader();
		
		try {
			Document document = reader.read(new File(DEFAULT_XML_URL));
	    	Element vcblrlist = document.getRootElement();
	    	List<Element> vcblrlistList = vcblrlist.elements();
	        
	    	
	    	//Element vcblr = vcblrlist.addElement("vcblr");
	    	Element vcblr = DocumentHelper.createElement("vcblr");
	    	vcblrlistList.add(i, vcblr);
	    	
	    	Element word = vcblr.addElement("word");
	    	word.setText(wordText.getText());
	    	
	    	for(int i = 0; i < transTextList.size(); i++) {
	    		Element translation = vcblr.addElement("translation");
	    		translation.addAttribute("id", "1");
	    		translation.setText(transTextList.get(i).getText());
			}
	    	
	    	for(int i = 0; i < phraseTextList.size(); i++) {
	    		Element phrase = vcblr.addElement("phrase");
	    		phrase.addAttribute("id", "2");
	    		phrase.setText(phraseTextList.get(i).getText());
			}
	    	
	    	for(int i = 0; i < expTextList.size(); i++) {
	    		Element expSts = vcblr.addElement("expSts");
	    		expSts.addAttribute("id", "3");
	    		expSts.setText(expTextList.get(i).getText());
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
