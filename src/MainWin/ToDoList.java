package MainWin;

public class ToDoList {
	
	private String item;
	//private int hour;
	//private int minute;
	//private int second;
	
	public ToDoList(String a) {
        item = a;
   }
	
	public String getItem() {
		return item;
	}
	
	public void setItem(String item) {
		this.item = item;
	}
	/*
	public int getHour() {
		return hour;
	}
	
	public void setHour(String hour) {
		this.hour = Integer.parseInt(hour);
	}
	
	public int getMinute() {
		return minute;
	}
	
	public void setMinute(String minute) {
		this.minute = Integer.parseInt(minute);
	}
	
	public int getSecond() {
		return second;
	}
	
	public void setSecond(String second) {
		this.second = Integer.parseInt(second);
	}
	*/
	
}
