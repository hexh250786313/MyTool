package MainWin;

import java.awt.BorderLayout;
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
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class QuizCardTable {
	
	ArrayList<QuizCard> cardData = new ArrayList<QuizCard>();
	private final String DEFAULT_XML_URL;
	private JFrame frame;
	private JTable table;
	private JScrollPane jp;
	private Vector<Vector<String>> data;
	private Vector<String> title;
	private JTextField indexTextField;
	private JTextArea contentTextArea;
	private ArrayList<QuizCard> quizCardList;
	private Iterator<QuizCard> listIterator;
	private DefaultTableModel model;
	
	
	 //制作不同学科的卡片时传入不同的url来读取不同的卡片
	public QuizCardTable(String url) {
		this.DEFAULT_XML_URL = url;
	}
	
	/*
	 * 
	 * 测试程序
	public static void main(String[] args) {
		QuizCardTable qc = new QuizCardTable();
		qc.gui();
	}*/
	
	//创建界面
	void gui() {
		frame = new JFrame("QuizCardTable");
		
		//菜单栏设计
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem newMenuItem = new JMenuItem("New");
		JMenuItem refreshMenuItem = new JMenuItem("Refresh");

		//表格设置
		title = new Vector<String>();
		title.add("Num.");
		title.add("Index");
		
		readXML();
		setData();
		
		model = new  DefaultTableModel(data, title);
		table = new JTable(model){  
			private static final long serialVersionUID = 2605667640305723489L;
			public boolean isCellEditable(int row,int col){ 
				return false;
			}
		};
		setTableSize();
		jp = new JScrollPane(table);

		table.addMouseListener(new ClickMouseEvent(model));
		refreshMenuItem.addActionListener(new RefreshListener());
		newMenuItem.addActionListener(new NewListener());
		
		fileMenu.add(newMenuItem);
		fileMenu.add(refreshMenuItem);
		menuBar.add(fileMenu);
		frame.setJMenuBar(menuBar);
		frame.add(jp);
		frame.setSize(new Dimension(280, 297));
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setVisible(true);
	}

	//监听New菜单
	public class NewListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) { 
			inputFrame();
       }
	}
	
	//监听鼠标点击事件
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
				quizCardFrame(row);
				//String cellVal = (String)(model.getValueAt(row, col)); 
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
	
	//监听Refresh菜单
	public class RefreshListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) { 
			model.getDataVector().clear();
			readXML();
			setData();
			model.setDataVector(data, title);
			setTableSize();
       }
	}

	//读取XML文件，把读取到的内容制作成quizCard，然后把每一个quizCard都存入quizCardList对象组
	private void readXML() {
		quizCardList = new ArrayList<QuizCard>();
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(new File(DEFAULT_XML_URL));
			Element list = document.getRootElement();
			Iterator<Element> listIterator = list.elementIterator();
			while(listIterator.hasNext()) {
				Element record = (Element)listIterator.next();
				QuizCard quizcard = new QuizCard();
				quizcard.setIndex(record.element("index").getText());
				quizcard.setCentent(record.element("content").getText());
				quizCardList.add(quizcard);
			}
		}catch(DocumentException e) {	
			e.printStackTrace();
		}
		listIterator = quizCardList.iterator();
	}
	
	//把quizCardList的数据放入data
	private void setData() {
		data = new Vector<Vector<String>>();
		int count = 1;
		while(listIterator.hasNext()) {
			Vector<String> oneData = new Vector<String>();
			oneData.add(count + "");
			oneData.add(((QuizCard)listIterator.next()).getIndex());
			data.add(oneData);
			count ++;
		}
	}

	//添加新内容的JFrame，被New菜单调用
	private void inputFrame() {
		JFrame inputFrame = new JFrame("Input");
		indexTextField = new JTextField();
		contentTextArea = new JTextArea(10, 14);
		
		JPanel bottomPanel = new JPanel();
		JPanel inputPanel = new JPanel();
		JButton newButton = new JButton("NEW");
		
		contentTextArea.setLineWrap(true);  //自动换行
		contentTextArea.setWrapStyleWord(true);  //换行时不会造成断字
		
		bottomPanel.setLayout(new BorderLayout());
		
		indexTextField.setPreferredSize(new Dimension(156, 20));
		
		JScrollPane jp = new JScrollPane(contentTextArea);
		jp.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		newButton.addActionListener(new InputNewContentListener());
		
		inputPanel.add(new JLabel("——INDEX——"));
		inputPanel.add(indexTextField);
		inputPanel.add(new JLabel("——CONTENT——"));
		inputPanel.add(jp);
		bottomPanel.add(inputPanel, BorderLayout.CENTER);
		bottomPanel.add(newButton, BorderLayout.SOUTH);
		inputFrame.add(bottomPanel);
		inputFrame.setResizable(false);
		inputFrame.setSize(new Dimension(200, 340));
		inputFrame.setLocationRelativeTo(null);
		//inputFrame.pack();
		inputFrame.setVisible(true);
		inputFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	}
	
	//新增内容的NEW按钮的监听方法
	public class InputNewContentListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) { 
			//JOptionPane.showMessageDialog(null, contentText + indexText, "ERROR", JOptionPane.ERROR_MESSAGE);
			String indexText = indexTextField.getText();
			String contentText = contentTextArea.getText();
			if((indexText.length() > 1) && (contentText.length() > 1)) {
				SAXReader reader = new SAXReader();
				try {
					Document document = reader.read(new File(DEFAULT_XML_URL));
					Element list = document.getRootElement();
					Element record = list.addElement("record");
					Element index = record.addElement("index");
					Element content = record.addElement("content");
					
					index.setText(indexText);
					content.setText(contentText);
					
					OutputFormat format = OutputFormat.createPrettyPrint();
					format.setEncoding("utf-8");
					XMLWriter xmlWriter = new XMLWriter(
							new FileOutputStream(DEFAULT_XML_URL),format);
					xmlWriter.write(document);
					xmlWriter.close();
					
					JOptionPane.showMessageDialog(null, "Done!", "ERROR", JOptionPane.INFORMATION_MESSAGE);
					indexTextField.setText("");
					contentTextArea.setText("");
				}catch(DocumentException | IOException e) {
					JOptionPane.showMessageDialog(null, "Error input! Please exit and retry.", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}else JOptionPane.showMessageDialog(null, "Please confirm if the text areas of INDEX and  CENTENT are empty!", "WARNING", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	//卡片展示框
	private void quizCardFrame(int i){
		JFrame cardFrame = new JFrame("Information");
		JTextArea contentArea = new JTextArea();
		QuizCard quizCard = quizCardList.get(i);
		
		contentArea.setText(quizCard.getContent());
		contentArea.setFont(new Font("", 1, 17));
		contentArea.setLineWrap(true);  //自动换行
		contentArea.setWrapStyleWord(true);  //换行时不会造成断字
		contentArea.setEditable(false);  //设置不可编辑
		
		cardFrame.add(new JScrollPane(contentArea));
		cardFrame.setLocationRelativeTo(null);
		cardFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		cardFrame.setResizable(false);
		//cardFrame.setMinimumSize(new Dimension(200, 50));
		//cardFrame.pack();
		cardFrame.setSize(new Dimension(309, 200));
		cardFrame.setVisible(true);
	}
	
	/*
	 * 实现JLabel文本按照width宽度自动换行，会改变label地preferredSize为width，高度按照内容自动计算
	 * 
	 * 
	private JLabel cteateJLabelWithWrapWidth(int width, JLabel label) {
		if(width <= 0 || label == null) {
			return label;
		}
		String text = label.getText();
		if(!text.startsWith("<html>")) {
			StringBuilder strBuilder = new StringBuilder("<html>");
			strBuilder.append(text);
			strBuilder.append("</html>");
			text = strBuilder.toString();
		}
		label.setText(text);
		View labelView = BasicHTML.createHTMLView(label, label.getText());
		labelView.setSize(width, 100);
		label.setPreferredSize(new Dimension(width, (int) labelView.getMinimumSpan(View.Y_AXIS)));
		return label;
	} 
	
	*/

	//设置表格大小
	private void setTableSize() {
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(40);
		table.getColumnModel().getColumn(1).setPreferredWidth(206); //181, 166
		//table.getColumnModel().getColumn(0).setResizable(false);
	}

}
