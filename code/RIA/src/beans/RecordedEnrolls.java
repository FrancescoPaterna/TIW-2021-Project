package beans;

import java.util.List;

public class RecordedEnrolls {
	private List<Enroll> recordedEnrolls;
	private String date;
	private String time;
	private Integer IDRecord;
	
	public RecordedEnrolls(List<Enroll> recordedEnrolls, String date, String time, Integer IDRecord) {
		this.recordedEnrolls = recordedEnrolls;
		this.date = date;
		this.time = time;
		this.IDRecord = IDRecord;
	}
	
}
