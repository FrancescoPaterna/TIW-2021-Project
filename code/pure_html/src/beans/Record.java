package beans;
import java.sql.Timestamp;

public class Record {
	
	private int id; 
	private Timestamp timestamp;
	private int IDExamDate;
	
	
	
	
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getIDExamDate() {
		return IDExamDate;
	}
	public void setIDExamDate(int iDExamDate) {
		IDExamDate = iDExamDate;
	}
	
	
	
}
