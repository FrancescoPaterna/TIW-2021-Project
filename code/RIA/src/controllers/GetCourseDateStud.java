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
		
		Integer course_id; 
		course_id = Integer.parseInt(request.getParameter("course_id"));
		User user = (User) session.getAttribute("user");
		
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
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			response.getWriter().println("You are not enrolled to any exam date for this exam date yet");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
