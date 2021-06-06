package beans;

import java.util.List;

public class MultipleUpdate {
	int exam_date_id;
	String score;
	List<Integer> id_stud;
	
	public String getScore() {
		return score;
	}
	
	public void setScore(String score) {
		this.score = score;
	}
	
	public List<Integer> getId_stud() {
		return id_stud;
	}
	
	public void setId_stud(List<Integer> id_stud) {
		this.id_stud = id_stud;
	}

	public int getExam_date_id() {
		return exam_date_id;
	}

	public void setExam_date_id(int exam_date_id) {
		this.exam_date_id = exam_date_id;
	}
}