package beans;


public class Enroll {
	private int IDstudent;
	private int IDexamdate;
	private String mark;
	private Status status;
	
	public int getIDstudent() {
		return IDstudent;
	}
	public void setIDstudent(int iDstudent) {
		IDstudent = iDstudent;
	}
	public int getIDexamdate() {
		return IDexamdate;
	}
	public void setIDexamdate(int iDexamdate) {
		IDexamdate = iDexamdate;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	
	
}
