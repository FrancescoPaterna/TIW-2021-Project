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
		String query = "SELECT  ID, date  FROM examdate  WHERE IDCourse = ? ";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, course_id);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					result.next();
					ExamDate ExamDate = new ExamDate();
					ExamDate.setID(result.getInt("ID"));
					ExamDate.setDate(result.getDate("date"));
					examdates.add(ExamDate);

					return examdates;
				}
			}
		}
	}
}