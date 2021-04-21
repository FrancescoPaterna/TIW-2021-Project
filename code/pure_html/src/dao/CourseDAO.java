package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Course;

public class CourseDAO {
	private Connection connection;
	
	public CourseDAO(Connection connection) {
		this.connection = connection;
	}
	
	public List<Course> findCoursesByIdProf(int profId) throws SQLException {
		List<Course> courses = new ArrayList<>();
		
		String query = "SELECT * from course where IDProfessor = ? ORDER BY ID ASC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, profId);
			try (ResultSet result = pstatement.executeQuery();) {
				while(result.next()) {
					Course course = new Course();
					course.setId(result.getInt("ID"));
					course.setName(result.getString("name"));
					course.setIDprofessor(result.getInt("IDProfessor"));
					courses.add(course);
				}
			}
		}
		return courses;
	}

}
