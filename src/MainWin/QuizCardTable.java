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
	
	
	 //������ͬѧ�ƵĿ�Ƭʱ���벻ͬ��url����ȡ��ͬ�Ŀ�Ƭ
	public QuizCardTable(String url) {
		this.DEFAULT_XML_URL = url;
	}
	
	/*
	 * 
	 * ���Գ���
	public static void main(String[] args) {
		QuizCardTable qc = new QuizCardTable();
		qc.gui();
	}*/
	
	//��������
	void gui() {
		frame = new JFrame("QuizCardTable");
		
		//�˵������
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem newMenuItem = new JMenuItem("New");
		JMenuItem refreshMenuItem = new JMenuItem("Refresh");

		//�������
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

	//����New�˵�
	public class NewListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) { 
			inputFrame();
       }
	}
	
	//����������¼�
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
	
	//����Refresh�˵�
	public class RefreshListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) { 
			model.getDataVector().clear();
			readXML();
			setData();
			model.setDataVector(data, title);
			setTableSize();
       }
	}

	//��ȡXML�ļ����Ѷ�ȡ��������������quizCard��Ȼ���ÿһ��quizCard������quizCardList������
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
	
	//��quizCardList�����ݷ���data
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

	//��������ݵ�JFrame����New�˵�����
	private void inputFrame() {
		JFrame inputFrame = new JFrame("Input");
		indexTextField = new JTextField();
		contentTextArea = new JTextArea(10, 14);
		
		JPanel bottomPanel = new JPanel();
		JPanel inputPanel = new JPanel();
		JButton newButton = new JButton("NEW");
		
		contentTextArea.setLineWrap(true);  //�Զ�����
		contentTextArea.setWrapStyleWord(true);  //����ʱ������ɶ���
		
		bottomPanel.setLayout(new BorderLayout());
		
		indexTextField.setPreferredSize(new Dimension(156, 20));
		
		JScrollPane jp = new JScrollPane(contentTextArea);
		jp.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		newButton.addActionListener(new InputNewContentListener());
		
		inputPanel.add(new JLabel("����INDEX����"));
		inputPanel.add(indexTextField);
		inputPanel.add(new JLabel("����CONTENT����"));
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
	
	//�������ݵ�NEW��ť�ļ�������
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
	
	//��Ƭչʾ��
	private void quizCardFrame(int i){
		JFrame cardFrame = new JFrame("Information");
		JTextArea contentArea = new JTextArea();
		QuizCard quizCard = quizCardList.get(i);
		
		contentArea.setText(quizCard.getContent());
		contentArea.setFont(new Font("", 1, 17));
		contentArea.setLineWrap(true);  //�Զ�����
		contentArea.setWrapStyleWord(true);  //����ʱ������ɶ���
		contentArea.setEditable(false);  //���ò��ɱ༭
		
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
	 * ʵ��JLabel�ı�����width����Զ����У���ı�label��preferredSizeΪwidth���߶Ȱ��������Զ�����
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

	//���ñ���С
	private void setTableSize() {
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(40);
		table.getColumnModel().getColumn(1).setPreferredWidth(206); //181, 166
		//table.getColumnModel().getColumn(0).setResizable(false);
	}

}
