package MainWin;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class ToDoListViewer {

	private String DEFAULT_XML_URL = "./file/ToDoList.xml";
	private JFrame frame;
	private JPanel mainPanel;
	//private JCheckBox itemBuilder;
	private ButtonGroup group = new ButtonGroup(); 
	private ArrayList<JRadioButton> functionBox;
	private ArrayList<JCheckBox> checkBoxList;
	private ArrayList<JCheckBox> exitCheckBox;
	private ArrayList<ToDoList> toDoList;
	private ToDoList toDo;
	private Iterator<ToDoList> listIterator;
	private String time;
	private int ONE_SECOND = 1000;
	private String DEFAULT_TIME_FORMAT = "HH:mm:ss";
	private JLabel displayArea;
	private JLabel timeLabel;
	private JButton newButton;
	private boolean flag;
	
	public static void main(String[] args) {
		ToDoListViewer list = new ToDoListViewer();
		list.go();
	}
	
	public void go() {
		flag = false;
		//����gui
		frame = new JFrame("ToDoList Viewer");
		mainPanel = new JPanel();
		JPanel centrePanel = new JPanel();
		JPanel topPanel = new JPanel();
		checkBoxList = new ArrayList<JCheckBox>();
		exitCheckBox = new ArrayList<JCheckBox>();
		functionBox = new ArrayList<JRadioButton>();
		
		//��ȡ��Ļ�߶ȺͿ�������ô��ڳ�ʼλ��
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int)screensize.getWidth();
		//int screenHeight = (int)screensize.getHeight();
		
		mainPanel.setLayout(new BorderLayout());
		
		timeLabel = new JLabel("CurrentTime: ");
		displayArea = new JLabel();
		newButton = new JButton("new");
		//itemBuilder = new JCheckBox("<html><font color='blue'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Build_Item</font><html>");
		
		configTimeArea();
		//��ȡXML�ļ������浽toDoList��
		getItem();
		//���������˳���ѡ��
		setExit();
		setFuncitonBox();
		
		topPanel.add(timeLabel);
		topPanel.add(displayArea);
		group.add(functionBox.get(0));
		group.add(functionBox.get(1));
		group.add(functionBox.get(2));
		group.add(functionBox.get(3));
		group.add(functionBox.get(4));
		group.add(functionBox.get(5));
		
		centrePanel.setLayout(new BoxLayout(centrePanel, BoxLayout.Y_AXIS));
		
		ActionListener listener = new OpenButtonListener(centrePanel);
		newButton.addActionListener(listener);
		//��ʼ��ʱ�½���Ŀ��ѡ��������˳���ѡ��
		for(JCheckBox item:setItem()) {
			checkBoxList.add(item);
			centrePanel.add(item);
		}
		
		centrePanel.add(exitCheckBox.get(0));
		centrePanel.add(exitCheckBox.get(1));
		
		centrePanel.add(functionBox.get(0));
		centrePanel.add(functionBox.get(4));
		centrePanel.add(functionBox.get(5));
		centrePanel.add(functionBox.get(1));
		centrePanel.add(functionBox.get(2));
		centrePanel.add(functionBox.get(3));
		
		//centrePanel.add(itemBuilder);
		
		
		mainPanel.add(new JLabel("        "), BorderLayout.WEST);
		mainPanel.add(new JLabel("        "), BorderLayout.EAST);
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(centrePanel, BorderLayout.CENTER);
		mainPanel.add(newButton, BorderLayout.SOUTH);
		frame.add(mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(0);
		//frame.setSize(new Dimension(280,95));
		//��ʼ��ʱʹ������Ӧȫ�������С
		frame.pack();
		frame.setMinimumSize(new Dimension(200, 95));
		frame.setLocation(screenWidth - (int)(frame.getSize().getWidth() * 1.5), (int)(frame.getSize().getHeight() * 0.5));
		//ʹ��ʼ��ʱ�������
		//frame.setLocationRelativeTo(null);
		//ʹ�������촰��
		frame.setResizable(false);
		//ʹ�ö�
		frame.setAlwaysOnTop(true);
		frame.setVisible(true); 
		
		//JOptionPane.showMessageDialog(null, checkBoxList.get(1).getText(), "Title", JOptionPane.ERROR_MESSAGE); 
		
		//test();
		
	}
	
	protected class JLabelTimerTask extends TimerTask{
		//����ʱ��ĸ�ʽ
		SimpleDateFormat dateFormatter = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
		@Override
		public void run() {
			time = dateFormatter.format(Calendar.getInstance().getTime());
			displayArea.setText(time);
		 }
	}
		
	public class OpenButtonListener implements ActionListener {
	   	//�����ִ�еĲ���
		JPanel centrePanel;
		
		public OpenButtonListener (JPanel centrePanel) {
			this.centrePanel = centrePanel;
		}
		
	    public void actionPerformed(ActionEvent ev) { 
	    	//�����ж��Ƿ�ͬʱѡ���������˳���ѡ��������񵯳�ѡ��Ի���ȷ���û���ͼ
	    	if(exitCheckBox.get(0).isSelected() && exitCheckBox.get(1).isSelected()) {
	    		int ifExit = JOptionPane.showConfirmDialog(null, "�˳���", "ע��",JOptionPane.YES_NO_OPTION); 
	    		if(ifExit == JOptionPane.YES_OPTION) {
	    			System.exit(0);
	    		}
	    	//���û��ͬʱѡ�������˳���ѡ����ִ����һ��
	    	}else if(functionBox.get(0).isSelected()){
	    		//ItemBuilder itemBuilder = new ItemBuilder();
	    		new ItemBuilder().go();
	    		group.clearSelection();
	    	}else if(functionBox.get(1).isSelected()){
	    		new DailyWork().go();
	    		group.clearSelection();
	    		centrePanel.remove(functionBox.get(1));
	    		flag = true;
	    	}else if(functionBox.get(2).isSelected()){
	    		group.clearSelection();
	    	}else if(functionBox.get(3).isSelected()){
	    		group.clearSelection();
	    		frame.setSize(new Dimension(200,95));
	    		frame.repaint();
	    	}else if(functionBox.get(4).isSelected()){
	    		group.clearSelection();
	    		frame.setSize(new Dimension(200,95));
	    		frame.repaint();
	    		new MyWordViewer().gui();
	    	}else if(functionBox.get(5).isSelected()){
	    		group.clearSelection();
	    		frame.setSize(new Dimension(200,95));
	    		frame.repaint();
	    		new QuizCardTable("./file/DigitalLogic.xml").gui();
	    	}else{
	    		
	    		SAXReader reader = new SAXReader();
	    		
	    		try {
	    			
	    			Document document = reader.read(new File(DEFAULT_XML_URL));
	    			
	    			for(int j = 0; j < checkBoxList.size(); j++) {
	    				JCheckBox jcb = (JCheckBox) checkBoxList.get(j);
	    				if(jcb.isSelected()) {
	    					
	    					int ifDelete = JOptionPane.showConfirmDialog(null, "�Ƴ���", "ע��",JOptionPane.YES_NO_OPTION); 
	    		    		if(ifDelete == JOptionPane.YES_OPTION) {
	    		    			
	    		    			Element toDoListXml = document.getRootElement();
					    		int count = 0;
					    		//Element item = toDoListXml.elements("todo").get(0).element("item");
					    		
					    		//������checkbox�Ƿ�ѡ��
					    		for(int i = 0; i < checkBoxList.size(); i++) {
					    			
					    			JCheckBox jc = (JCheckBox) checkBoxList.get(i);
					    			if(jc.isSelected()) {
					    				//��ѡ�ϵĻ���ɾ����checkbox��Ӧ��ͬһ��todo��Ŀ��ע��todo��Ŀÿɾ��һ����λ������һ��������Ҫ��count����λ׼ȷλ�ã�i-countΪ��ȷλ��
					    				Element todo = toDoListXml.elements("todo").get(i - count);
					    				//JOptionPane.showMessageDialog(null, todo.element("item").getText(), "Title", JOptionPane.ERROR_MESSAGE); 
					    				toDoListXml.remove(todo);
					    				count ++;//�ǵ�ÿ�Ƴ�һ����Ҫ����һ��count
					    			}
					    		}	
	    		    		}
							break;//����һ�μ��ɣ��м��ɣ�
	    				}
	    				
	    			}//�˳��Ƿ��ϵ��ж�ѭ��
	    			//��д����
		    		OutputFormat format = OutputFormat.createPrettyPrint();
					format.setEncoding("utf-8");
					XMLWriter xmlWriter = new XMLWriter(
							new FileOutputStream(DEFAULT_XML_URL),format);
					xmlWriter.write(document);
					xmlWriter.close();
	    		}catch (DocumentException | IOException e) {
		    		e.printStackTrace();
		    	}
	    		
	    		//�����������ȫ��checkbox�����¶�ȡxml�ʹ����µ�checkbox
		    	centrePanel.removeAll();
		    	getItem();
		    	checkBoxList.clear();
		    	for(JCheckBox item:setItem()) {
		    		checkBoxList.add(item);
					centrePanel.add(item);
				}
		    	
		    	//����µ��˳���鸴ѡ��
		    	centrePanel.add(exitCheckBox.get(0));
				centrePanel.add(exitCheckBox.get(1));
				centrePanel.add(functionBox.get(0));
				centrePanel.add(functionBox.get(4));
				centrePanel.add(functionBox.get(5));
				if(flag == false) centrePanel.add(functionBox.get(1));
				centrePanel.add(functionBox.get(2));
				centrePanel.add(functionBox.get(3));
		    	mainPanel.repaint();
		    	frame.pack();
	    	}
	    }
	}

	/*
	 * ���Գ���
	 * 
	private void test() {
			
		SAXReader reader = new SAXReader();
		
		try {
    		Document document = reader.read(new File(DEFAULT_XML_URL));
    		Element toDoListXml = document.getRootElement();
    		Element item = toDoListXml.elements("todo").get(0).element("item");
    		String str = checkBoxList.get(0).getText();
    		str = str.substring(2, 2 + item.getText().length());
    		
    		
    		if(item.getText().equals(str)) {
    			JOptionPane.showMessageDialog(null, str+str.length()+" and "+item.getText().length(), "Title"+str, JOptionPane.ERROR_MESSAGE); 
    		}else {
    			//JOptionPane.showMessageDialog(null, str+str.length()+" and "+item.getText().length(), "Title"+str, JOptionPane.ERROR_MESSAGE); 
    		}

    	}catch (DocumentException e) {
    		e.printStackTrace();
    	}

	}
	*/
	
	private void getItem() {
		
		toDoList = new ArrayList<ToDoList>();
    	SAXReader reader = new SAXReader();
    	
    	try {
    		Document document = reader.read(new File(DEFAULT_XML_URL));
    		Element toDoListXml = document.getRootElement();
    		Iterator<Element> toDoListIterator = toDoListXml.elementIterator();
    		
    		//�ֱ��ȡÿ��todo����item��ֵ����toDo��Ȼ����뵽toDoList��
    		while (toDoListIterator.hasNext()) {
    			Element todo = (Element) toDoListIterator.next();
    			String item = todo.getStringValue();
    			makeList(item);
    		}
    	}catch (DocumentException e) {
    		e.printStackTrace();
    	}
    	//����toDoList�ĵ�����
    	listIterator = toDoList.iterator();
	}
	
	private void makeList (String item) {
	     
        ToDoList todo = new ToDoList(item);
        toDoList.add(todo);
  }
	
	private JCheckBox[] setItem() {
		
		final int listSize = toDoList.size();
		JCheckBox checkArray[] = new JCheckBox[listSize];
		int i = 0;
		
		//��toDoList��ֵ�ֱ𸳸�checkbox���鲢��������
		while(listIterator.hasNext()) {
			toDo = (ToDoList) listIterator.next();
			checkArray[i] = new JCheckBox(toDo.getItem());
			i++;
		}
		
		return checkArray;
		
	}

	private void setExit() {
		//String str = "EXIT_1";
		exitCheckBox.add(new JCheckBox("<html><font color='red'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;EXIT_1</font><html>"));
		exitCheckBox.add(new JCheckBox("<html><font color='red'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;EXIT_2</font><html>"));
		
	}
	
	private void setFuncitonBox() {
		
		functionBox.add(new JRadioButton("<html><font color='blue'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Build_Item</font><html>"));
		functionBox.add(new JRadioButton("<html><font color='#58ACFA'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Daily_Work</font><html>"));
		functionBox.add(new JRadioButton("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Reset<html>"));
		functionBox.add(new JRadioButton("<html><font color='#AEB404'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Zoom_Out</font><html>"));
		functionBox.add(new JRadioButton("<html><font color='#668B8B'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Vocabulary_Table</font><html>"));
		functionBox.add(new JRadioButton("<html><font color='#8B6914'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Digital_Logic</font><html>"));
	}
	
	private void configTimeArea() {
		Timer tmr = new Timer();
		tmr.scheduleAtFixedRate(new JLabelTimerTask(),new Date(), ONE_SECOND);
		}
	
}
