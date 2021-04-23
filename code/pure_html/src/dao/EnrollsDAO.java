package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import beans.Enroll;
import beans.Status;


public class EnrollsDAO {
	private Connection con;

	public EnrollsDAO(Connection connection) {
			this.con = connection;
		}

	public Enroll getCourseFindExameDateBYCourseForProfessora() throws SQLException {
		String query = "SELECT  ID, name, surname, email, role, coursedeg FROM enroll JOIN user ON enroll.IDstudent == user.ID  WHERE ID = ? AND password =?";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			//pstatement.setString(1, id);
			//pstatement.setString(2, pwd);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					result.next();
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("iDstudent"));
					enroll.setIDexamdate(result.getInt("iDexamdate"));
					enroll.setMark(result.getString("name"));
					//enroll.setStatus(result.getString("Status"));
					//enroll.setEmail(result.getString("email"));
					//enroll.setCoursedeg(result.getString("coursedeg"));

					return enroll;
				}
			}
		}
	}
}