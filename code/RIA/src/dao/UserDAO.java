package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import beans.User;

public class UserDAO {
	private Connection con;

	public UserDAO(Connection connection) {
		this.con = connection;
	}

	public User checkCredentials(String id, String pwd) throws SQLException {
		String query = "SELECT  ID, name, surname, email, role, coursedeg FROM user  WHERE BINARY ID = ? AND password =?";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setString(1, id);
			pstatement.setString(2, pwd);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					result.next();
					User user = new User();
					user.setId(result.getInt("ID"));
					user.setName(result.getString("name"));
					user.setSurname(result.getString("surname"));
					user.setEmail(result.getString("email"));
					user.setRole(result.getString("role"));
					if (result.getString("role").equals("student")) {
						user.setCoursedeg(result.getString("coursedeg"));
					}
					return user;
				}
			}
		}
	}

	public User findProfessorByIdCourse(int id_course) throws SQLException {
		User user;
		String query = "SELECT  user.ID, user.name, user.surname, user.email FROM projectdb.user user JOIN projectdb.course course ON user.ID = course.IDprofessor WHERE course.ID = ?";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, id_course);
			try (ResultSet result = pstatement.executeQuery();) {
				result.next();
				user = new User();
				user.setId(result.getInt("ID"));
				user.setName(result.getString("name"));
				user.setSurname(result.getString("surname"));
				user.setEmail(result.getString("email"));
			}

		}
		return user;
	}
}
