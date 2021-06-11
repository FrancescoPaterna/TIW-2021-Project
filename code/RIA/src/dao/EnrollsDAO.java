package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Enroll;
import beans.Status;

public class EnrollsDAO {
	private Connection connection;

	public EnrollsDAO(Connection connection) {
		this.connection = connection;
	}

	public List<Enroll> FindEnrolls(int exameDateId) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();

		String query = "SELECT user.ID, user.name, user.surname, user.email, user.coursedeg, enroll.mark, enroll.status "
				+ "FROM (user JOIN enroll ON user.ID = enroll.IDStudent) JOIN examdate ON enroll.IDExamDate = examdate.IDExam "
				+ "WHERE IDexam = ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, exameDateId);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("ID"));
					enroll.setName(result.getString("name"));
					enroll.setSurname(result.getString("surname"));
					enroll.setMail(result.getString("email"));
					enroll.setCourseDeg(result.getString("coursedeg"));
					enroll.setMark(result.getString("mark"));
					enroll.setStatus(Status.valueOf(result.getString("status")));
					enrolls.add(enroll);
				}
			}
		}
		return enrolls;
	}

	public List<Enroll> FindEnrollsOrderedByIDAsc(int exameDateId) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();

		String query = "SELECT user.ID, user.name, user.surname, user.email, user.coursedeg, enroll.mark, enroll.status "
				+ "FROM (user JOIN enroll ON user.ID = enroll.IDStudent) JOIN examdate ON enroll.IDExamDate = examdate.IDExam "
				+ "WHERE IDexam = ? ORDER BY user.ID ASC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, exameDateId);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("ID"));
					enroll.setName(result.getString("name"));
					enroll.setSurname(result.getString("surname"));
					enroll.setMail(result.getString("email"));
					enroll.setCourseDeg(result.getString("coursedeg"));
					enroll.setMark(result.getString("mark"));
					enroll.setStatus(Status.valueOf(result.getString("status")));
					enrolls.add(enroll);
				}
			}
		}
		return enrolls;
	}

	public Enroll FindStudentScore(int exameDateId, int user_id) throws SQLException {
		Enroll enroll;

		String query = "SELECT user.ID, user.name, user.surname, user.coursedeg, enroll.mark, enroll.status, examdate.IDExam, course.name coursename, course.ID courseID \r\n"
				+ "FROM projectdb.user user JOIN projectdb.enroll enroll ON user.ID = enroll.IDStudent JOIN projectdb.examdate examdate ON enroll.IDExamDate = examdate.IDExam JOIN projectdb.course course ON examdate.IDCourse = course.ID \r\n"
				+ "WHERE examdate.IDExam = ? AND user.ID= ?";

		try (PreparedStatement pstatement = connection.prepareStatement(query);) {

			pstatement.setInt(1, exameDateId);
			pstatement.setInt(2, user_id);
			try (ResultSet result = pstatement.executeQuery();) {
				result.next();
				enroll = new Enroll();
				enroll.setIDstudent(result.getInt("ID"));
				enroll.setName(result.getString("name"));
				enroll.setSurname(result.getString("surname"));
				enroll.setMark(result.getString("mark"));
				enroll.setIDSession(result.getInt("IDExam"));
				enroll.setCourseDeg(result.getString("coursedeg"));
				enroll.setStatus(Status.valueOf(result.getString("status")));
			}
		}
		return enroll;
	}
	
	public boolean checkModifiableCondition(int examDateId, int studentId) throws SQLException {
		
		boolean isStatusModifiable;
		String query = "SELECT * \r\n" +
				"FROM projectdb.enroll \r\n" + 
				"WHERE IDExamDate = ? AND IDStudent = ? AND (status = 'NOT_INSERTED' OR status = 'INSERTED')";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, examDateId);
			pstatement.setInt(2, studentId);
			try(ResultSet result = pstatement.executeQuery();) {
				if(!result.isBeforeFirst()) {
					isStatusModifiable = false;
				} else isStatusModifiable = true;
			}
		}
		return isStatusModifiable;
		
	}

	public int insertMark(int examDateId, int studentId, String mark) throws SQLException {
		int modifiedRows = 0;
		String query = "UPDATE projectdb.enroll SET status='INSERTED', mark = ? WHERE (IDExamDate = ? AND IDStudent = ?) AND (status = 'INSERTED' OR status = 'NOT_INSERTED') ";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, mark);
			pstatement.setInt(2, examDateId);
			pstatement.setInt(3, studentId);
			modifiedRows = pstatement.executeUpdate();
		}
		return modifiedRows;
	}

	public void RefuseScore(int examDateId, int user_id) throws SQLException {

		String query = "UPDATE projectdb.enroll SET status='REJECTED' WHERE IDExamDate = ? AND IDStudent = ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, examDateId);
			pstatement.setInt(2, user_id);
			pstatement.executeUpdate();
		}

	}

	public void PublishScore(int examDateId) throws SQLException {

		String query = "UPDATE projectdb.enroll SET status='PUBLISHED' WHERE IDExamDate = ? AND status = 'INSERTED'";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, examDateId);
			pstatement.executeUpdate();
		}

	}

	public void recordScore(int examDateId, int recordID) throws SQLException {

		String query1 = "UPDATE projectdb.enroll SET mark='RIMANDATO' WHERE IDExamDate = ? AND status = 'REJECTED'";
		String query2 = "UPDATE projectdb.enroll SET status='RECORDED' WHERE IDExamDate = ? AND (status = 'PUBLISHED' OR status = 'REJECTED')";
		String query3 = "UPDATE projectdb.enroll SET IDRecord= ? WHERE IDRecord is null AND IDExamDate = ? AND status='RECORDED'";

		try (PreparedStatement pstatement = connection.prepareStatement(query1);) {
			pstatement.setInt(1, examDateId);
			pstatement.executeUpdate();
		}
		try (PreparedStatement pstatement = connection.prepareStatement(query2);) {
			pstatement.setInt(1, examDateId);
			pstatement.executeUpdate();
		}
		try (PreparedStatement pstatement = connection.prepareStatement(query3);) {

			pstatement.setInt(1, recordID);
			pstatement.setInt(2, examDateId);
			pstatement.executeUpdate();

		}
	}

	public List<Enroll> findRecordedStudents(int record_id) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();

		String query = "SELECT user.ID, user.name, user.surname, enroll.mark \r\n"
				+ "FROM user JOIN enroll ON user.ID = enroll.IDStudent \r\n"
				+ "WHERE enroll.IDRecord = ? ";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, record_id);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("ID"));
					enroll.setName(result.getString("name"));
					enroll.setSurname(result.getString("surname"));
					enroll.setMark(result.getString("mark"));
					enrolls.add(enroll);
				}
			}
		}
		return enrolls;
	}

	public boolean assertion_record(int id_exam) throws SQLException {
		boolean assert_rec;

		String query = "SELECT * FROM projectdb.enroll WHERE enroll.IDExamDate = ? AND (enroll.status = 'PUBLISHED' OR enroll.status = 'REJECTED')";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {

			pstatement.setInt(1, id_exam);
			try (ResultSet result = pstatement.executeQuery();) {
				if(!result.isBeforeFirst()) {
					assert_rec = false;
				} else assert_rec = true;
			}
		}
		return assert_rec;
	}
	
	public boolean assertion_published(int id_exam) throws SQLException {
		boolean assert_pub;

		String query = "SELECT * FROM projectdb.enroll WHERE enroll.IDExamDate = ? AND enroll.status = 'INSERTED'";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {

			pstatement.setInt(1, id_exam);
			try (ResultSet result = pstatement.executeQuery();) {
				if(!result.isBeforeFirst()) {
					assert_pub = false;
				} else assert_pub = true;
			}
		}
		return assert_pub;
	}
	

}