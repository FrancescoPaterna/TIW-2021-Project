package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import beans.Course;
import beans.User;
import dao.CourseDAO;
import utils.ConnectionHandler;

@WebServlet("/GetCoursePro")
@MultipartConfig
public class GetCoursePro extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	public GetCoursePro() {
		super();
	}

	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		User user = (User) session.getAttribute("user");
		
		CourseDAO courseDAO = new CourseDAO(connection);
		List<Course> courses = new ArrayList<>();
		
		// recover courses from the database 
		try {
			courses = courseDAO.findCoursesByIdProf(user.getId());
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Not possible to recover courses");
			return;
		}

		// send back retrieved courses as JSON
		
		String serialized_courses = new Gson().toJson(courses);

		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(serialized_courses);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
