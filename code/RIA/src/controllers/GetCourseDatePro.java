package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import beans.Course;
import beans.ExamDate;
import beans.User;
import dao.CourseDAO;
import dao.ExamDateDAO;
import utils.ConnectionHandler;

@WebServlet("/GetCourseDatePro")
@MultipartConfig
public class GetCourseDatePro extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		Integer course_id;
		try {
			course_id = Integer.parseInt(request.getParameter("course_id"));
			// If the argument of Integer.parseInt is null or is a string of length zero, a NumberFormatException is thrown
			// @see
			// https://docs.oracle.com/javase/7/docs/api/java/lang/Integer.html#parseInt(java.lang.String)
		} catch (NumberFormatException | NullPointerException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Incorrect or missing param values");
			return;
		}

		User user = (User) session.getAttribute("user");

		/*
		 * Verifichiamo che il corso inserito appartenga al professore (Protezione
		 * Attacco SQL)
		 */
		CourseDAO courseDAO = new CourseDAO(connection);
		List<Course> courses = new ArrayList<>();

		try {
			courses = courseDAO.findCoursesByIdProf(user.getId());
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Not possible to recover courses");
			return;
		}

		boolean course_found = false;
		for (Course course : courses) {
			if (course.getId() == course_id) {
				course_found = true;
			}
		}
		
		// se il corso non appartiene al professore, significa che sta cercando di accedere ad una risorsa non sua
		if (!course_found) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("unauthorized access");
			session.invalidate();
			return;
		}

		/********************************************************************************************/

		ExamDateDAO ExamDateDAO = new ExamDateDAO(connection);
		List<ExamDate> exams = new ArrayList<>();

		// recover exam dates from the database
		try {
			exams = ExamDateDAO.FindExameDateBYCourseForProfessor(course_id);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Not possible to recover courses dates");
			return;
		}

		// Send exams back to the client as JSON string
		String serialized_coursesDate = new Gson().toJson(exams);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(serialized_coursesDate);
	}

	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
