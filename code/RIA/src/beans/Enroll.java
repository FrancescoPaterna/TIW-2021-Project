package beans;


public class Enroll {
	private int IDstudent;
	private String name;
	private String surname;
	private String mail;
	private String mark;
	private String courseDeg;
	private Status status;
	private int IDSession;
	
	//rimosso course_id e coursename, verificare corretto funzionamento v.31
	


	public int getIDSession() {
		return IDSession;
	}

	public void setIDSession(int iDSession) {
		IDSession = iDSession;
	}


	public int getIDstudent() {
		return IDstudent;
	}
	
	public void setIDstudent(int iDstudent) {
		IDstudent = iDstudent;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getMail() {
		return mail;
	}
	
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public String getCourseDeg() {
		return courseDeg;
	}
	
	public void setCourseDeg(String courseDeg) {
		this.courseDeg = courseDeg;
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
