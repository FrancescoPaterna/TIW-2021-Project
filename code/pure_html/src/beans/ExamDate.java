package beans;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExamDate {
	
	private int ID;
	private String data; 
	private DateFormat data2 = new SimpleDateFormat("dd-MM-yyyy");
	private int IDcourse;
	
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getData() {
		return data;
	}
	public void setData(Date date) {
		this.data = data2.format(date);
	}

	public int getIDcourse() {
		return IDcourse;
	}
	public void setIDcourse(int iDcourse) {
		IDcourse = iDcourse;
	}
	
	
	
	
}
