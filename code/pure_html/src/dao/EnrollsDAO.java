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

	public List<Enroll> FindEnrollsOrderedByIDDesc(int exameDateId) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();

		String query = "SELECT user.ID, user.name, user.surname, user.email, user.coursedeg, enroll.mark, enroll.status "
				+ "FROM (user JOIN enroll ON user.ID = enroll.IDStudent) JOIN examdate ON enroll.IDExamDate = examdate.IDExam "
				+ "WHERE IDexam = ? ORDER BY user.ID DESC";
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

	public List<Enroll> FindEnrollsOrderedByNameAsc(int exameDateId) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();

		String query = "SELECT user.ID, user.name, user.surname, user.email, user.coursedeg, enroll.mark, enroll.status "
				+ "FROM (user JOIN enroll ON user.ID = enroll.IDStudent) JOIN examdate ON enroll.IDExamDate = examdate.IDExam "
				+ "WHERE IDExam = ? ORDER BY user.name ASC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, exameDateId);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("ID"));
					enroll.setName(result.getString("name"));
					enroll.setName(result.getString("surname"));
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

	public List<Enroll> FindEnrollsOrderedByNameDesc(int exameDateId) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();

		String query = "SELECT user.ID, user.name, user.surname, user.email, user.coursedeg, enroll.mark, enroll.status "
				+ "FROM (user JOIN enroll ON user.ID = enroll.IDStudent) JOIN examdate ON enroll.IDExamDate = examdate.IDExam "
				+ "WHERE IDExam = ? ORDER BY user.name DESC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, exameDateId);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("ID"));
					enroll.setName(result.getString("name"));
					enroll.setName(result.getString("surname"));
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

	public List<Enroll> FindEnrollsOrderedBySurnameAsc(int exameDateId) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();

		String query = "SELECT user.ID, user.name, user.surname, user.email, user.coursedeg, enroll.mark, enroll.status "
				+ "FROM (user JOIN enroll ON user.ID = enroll.IDStudent) JOIN examdate ON enroll.IDExamDate = examdate.IDExam "
				+ "WHERE IDExam = ? ORDER BY user.surname ASC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, exameDateId);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("ID"));
					enroll.setName(result.getString("name"));
					enroll.setName(result.getString("surname"));
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

	public List<Enroll> FindEnrollsOrderedBySurnameDesc(int exameDateId) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();

		String query = "SELECT user.ID, user.name, user.surname, user.email, user.coursedeg, enroll.mark, enroll.status "
				+ "FROM (user JOIN enroll ON user.ID = enroll.IDStudent) JOIN examdate ON enroll.IDExamDate = examdate.IDExam "
				+ "WHERE IDExam = ? ORDER BY user.surname DESC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, exameDateId);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("ID"));
					enroll.setName(result.getString("name"));
					enroll.setName(result.getString("surname"));
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

	public List<Enroll> FindEnrollsOrderedByEmailAsc(int exameDateId) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();

		String query = "SELECT user.ID, user.name, user.surname, user.email, user.coursedeg, enroll.mark, enroll.status "
				+ "FROM (user JOIN enroll ON user.ID = enroll.IDStudent) JOIN examdate ON enroll.IDExamDate = examdate.IDExam "
				+ "WHERE IDExam = ? ORDER BY user.email ASC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, exameDateId);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("ID"));
					enroll.setName(result.getString("name"));
					enroll.setName(result.getString("surname"));
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

	public List<Enroll> FindEnrollsOrderedByEmailDesc(int exameDateId) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();

		String query = "SELECT user.ID, user.name, user.surname, user.email, user.coursedeg, enroll.mark, enroll.status "
				+ "FROM (user JOIN enroll ON user.ID = enroll.IDStudent) JOIN examdate ON enroll.IDExamDate = examdate.IDExam "
				+ "WHERE IDExam = ? ORDER BY user.email DESC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, exameDateId);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("ID"));
					enroll.setName(result.getString("name"));
					enroll.setName(result.getString("surname"));
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

	public List<Enroll> FindEnrollsOrderedByMarkAsc(int exameDateId) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();

		String query = "SELECT user.ID, user.name, user.surname, user.email, user.coursedeg, enroll.mark, enroll.status "
				+ "FROM (user JOIN enroll ON user.ID = enroll.IDStudent) "
				+ "JOIN examdate ON enroll.IDExamDate = examdate.IDExam "
				+ "WHERE IDExam = ? ORDER BY FIELD(mark, 'ABSENT', 'RIMANDATO', 'RIPROVATO', '18', "
				+ "'19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '30L') ASC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, exameDateId);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("ID"));
					enroll.setName(result.getString("name"));
					enroll.setName(result.getString("surname"));
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

	public List<Enroll> FindEnrollsOrderedByMarkDesc(int exameDateId) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();

		String query = "SELECT user.ID, user.name, user.surname, user.email, user.coursedeg, enroll.mark, enroll.status "
				+ "FROM (user JOIN enroll ON user.ID = enroll.IDStudent) "
				+ "JOIN examdate ON enroll.IDExamDate = examdate.IDExam "
				+ "WHERE IDExam = ? ORDER BY FIELD(mark, 'ABSENT', 'RIMANDATO', 'RIPROVATO', '18', "
				+ "'19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '30L') DESC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, exameDateId);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("ID"));
					enroll.setName(result.getString("name"));
					enroll.setName(result.getString("surname"));
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

	public List<Enroll> FindEnrollsOrderedByStatusAsc(int exameDateId) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();

		String query = "SELECT user.ID, user.name, user.surname, user.email, user.coursedeg, enroll.mark, enroll.status "
				+ "FROM (user JOIN enroll ON user.ID = enroll.IDStudent) JOIN examdate ON enroll.IDExamDate = examdate.IDExam "
				+ "WHERE IDExam = ? ORDER BY enroll.status ASC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, exameDateId);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("ID"));
					enroll.setName(result.getString("name"));
					enroll.setName(result.getString("surname"));
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

	public List<Enroll> FindEnrollsOrderedByStatusDesc(int exameDateId) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();

		String query = "SELECT user.ID, user.name, user.surname, user.email, user.coursedeg, enroll.mark, enroll.status "
				+ "FROM (user JOIN enroll ON user.ID = enroll.IDStudent) JOIN examdate ON enroll.IDExamDate = examdate.IDExam"
				+ "WHERE IDExam = ? ORDER BY enroll.status DESC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, exameDateId);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("ID"));
					enroll.setName(result.getString("name"));
					enroll.setName(result.getString("surname"));
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
					enroll.setCourse(result.getString("courseID"));
					enroll.setCourseName(result.getString("coursename"));
				}
		}
		return enroll;
	}

	public void RefuseScore(int examDateId, int user_id) throws SQLException {

		String query = "UPDATE projectdb.enroll SET status='REJECTED' WHERE IDExamDate = ? AND IDStudent = ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, examDateId);
			pstatement.setInt(2, user_id);
			pstatement.executeUpdate();
		}

	}

}