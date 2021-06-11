package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import beans.Enroll;
import beans.Status;
import beans.User;
import dao.EnrollsDAO;
import utils.ConnectionHandler;

/**
 * Servlet implementation class GetResultDetails
 */
@WebServlet("/GetResultDetails")
@MultipartConfig
public class GetResultDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
       
	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		Integer IDExamDate;
		try {
			IDExamDate = Integer.parseInt(request.getParameter("IDExamDate"));
			// If the argument of Integer.parseInt is null or is a string of length zero, a
			// NumberFormatException is thrown
			// @see https://docs.oracle.com/javase/7/docs/api/java/lang/Integer.html#parseInt(java.lang.String)
		} catch (NumberFormatException | NullPointerException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Incorrect param values");
			return;
		}
		
		EnrollsDAO enrollsDAO = new EnrollsDAO(connection);
		Enroll enroll;
		
		// recover the enroll bean for the specified student and the specified exam date
		try {
			enroll = enrollsDAO.FindStudentScore(IDExamDate, user.getId());

		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Not possible to recover result details");
			return;
		}
		
		// if the result can be shown to the student then send it as JSON
		if(!(enroll.getStatus() == Status.NOT_INSERTED || enroll.getStatus() == Status.INSERTED)) {
			String serialized_resultDetails = new Gson().toJson(enroll);		
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(serialized_resultDetails);
		} else {
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
