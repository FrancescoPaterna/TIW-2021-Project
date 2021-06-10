package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import beans.ExamDate;


public class ExamDateDAO {
	private Connection con;

	public ExamDateDAO(Connection connection) {
			this.con = connection;
		}

	public List<ExamDate> FindExameDateBYCourseForProfessor(int course_id) throws SQLException {
		List<ExamDate> examdates = new ArrayList<>();
		String query = "SELECT  IDExam, date  FROM examdate WHERE IDCourse = ? ORDER BY date DESC";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, course_id);
			try (ResultSet result = pstatement.executeQuery();) {
				while(result.next()) {
					ExamDate ExamDate = new ExamDate();
					ExamDate.setID(result.getInt("IDExam"));
					ExamDate.setData(result.getDate("date"));
					examdates.add(ExamDate);
				}
			}
		}
		return examdates;

	}
	
	public List<ExamDate> FindExameDateBYCourseForStudent(int id_Stud, int course_id) throws SQLException {
		List<ExamDate> examdates = new ArrayList<>();
		String query = "SELECT IDExamDate, date FROM projectdb.enroll enroll JOIN projectdb.examdate examdate ON enroll.IDExamDate = examdate.IDExam WHERE IDStudent = ? AND IDCourse = ? ORDER BY date DESC";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, id_Stud);
			pstatement.setInt(2, course_id);
			try (ResultSet result = pstatement.executeQuery();) {
				while(result.next()) {
					ExamDate ExamDate = new ExamDate();
					ExamDate.setID(result.getInt("IDExamDate"));
					ExamDate.setData(result.getDate("date"));
					examdates.add(ExamDate);
				}
			}
		}
		return examdates;

	}
	
	public boolean CheckExamDateByProf(int user_id, int exam_id) throws SQLException {
		boolean allowed;
		String query = "SELECT * FROM projectdb.examdate examdate JOIN projectdb.course course ON IDCourse = course.ID WHERE IDprofessor = ? AND IDExam = ? ";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, user_id);
			pstatement.setInt(2, exam_id);
			try(ResultSet result = pstatement.executeQuery();) {
				if(!result.isBeforeFirst()) {
					allowed = false;
				} else allowed = true;
			}
		}
		return allowed;
	}
	
	
	
}