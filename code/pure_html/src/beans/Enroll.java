package beans;


public class Enroll {
	private int IDstudent;
	private int IDexamdate;
	private int mark;
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
	public int getMark() {
		return mark;
	}
	public void setMark(int mark) {
		this.mark = mark;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	
	
}
