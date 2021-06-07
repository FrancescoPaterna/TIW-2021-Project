package beans;

import java.util.List;

public class RecordedEnrolls {
	private List<Enroll> recordedEnrolls;
	private String date;
	private String time;
	
	public RecordedEnrolls(List<Enroll> recordedEnrolls, String date, String time) {
		this.recordedEnrolls = recordedEnrolls;
		this.date = date;
		this.time = time;
	}
	
}
