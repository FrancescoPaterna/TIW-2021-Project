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

import beans.Enroll;
import beans.User;
import dao.EnrollsDAO;
import dao.ExamDateDAO;

import utils.ConnectionHandler;

@WebServlet("/GetSessionEnrolls")
@MultipartConfig
public class GetSessionEnrolls extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();

		Integer exam_date_id = null;
		
		try {
			exam_date_id = Integer.parseInt(request.getParameter("exam_date_id"));
			// If the argument of Integer.parseInt is null or is a string of length zero, a
			// NumberFormatException is thrown
			// @see https://docs.oracle.com/javase/7/docs/api/java/lang/Integer.html#parseInt(java.lang.String)
		} catch (NumberFormatException | NullPointerException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Incorrect param values");
			return;
		}

		ExamDateDAO examdateDAO = new ExamDateDAO(connection);
		User user = (User) session.getAttribute("user");

		// check if the professor can see data of this exam date, otherwise send an error
		try {
			if (!examdateDAO.CheckExamDateByProf(user.getId(), exam_date_id)) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println("UNAUTHORIZED ACCESS");
				session.invalidate();
				return;
			}
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("DB Error");
			return;
		}

		EnrollsDAO enrollsDAO = new EnrollsDAO(connection);
		List<Enroll> enrolls = new ArrayList<>();

		// recover enrolls for the specified exam date from the database
		try {
			enrolls = enrollsDAO.FindEnrollsOrderedByIDAsc(exam_date_id);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("DB Error");
			return;
		}
		
		// send enrolls retrieved back to the client as JSON
		String serialized_enrolls = new Gson().toJson(enrolls);		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(serialized_enrolls);

	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
