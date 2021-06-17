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

import beans.ExamDate;
import beans.User;
import dao.ExamDateDAO;
import utils.ConnectionHandler;

@WebServlet("/GetCourseDateStud")
@MultipartConfig
public class GetCourseDateStud extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
       
	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		User user = (User) session.getAttribute("user");
		
		Integer course_id;
		try {
			course_id = Integer.parseInt(request.getParameter("course_id"));
			// If the argument of Integer.parseInt is null or is a string of length zero, a
			// NumberFormatException is thrown
			// @see https://docs.oracle.com/javase/7/docs/api/java/lang/Integer.html#parseInt(java.lang.String)
		} catch(NumberFormatException | NullPointerException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Incorrect param values");
			return;
		}
		
		
		ExamDateDAO ExamDateDAO = new ExamDateDAO(connection);
		List<ExamDate> exams = new ArrayList<>();
		
		try {
			
			exams = ExamDateDAO.FindExameDateBYCourseForStudent(user.getId(), course_id);

		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Not possible to recover exam dates");
			return;
		}
		
		// Send exams if found any
		if(!exams.isEmpty()) {
			String serialized_coursesDate = new Gson().toJson(exams);		
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(serialized_coursesDate);
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("You are not enrolled to any exam date for this exam date yet");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
