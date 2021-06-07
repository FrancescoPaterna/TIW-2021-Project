package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.User;
import dao.EnrollsDAO;
import dao.ExamDateDAO;
import dao.RecordDAO;
import utils.ConnectionHandler;

/**
 * Servlet implementation class RecordScores
 */
@WebServlet("/RecordScores")
@MultipartConfig
public class RecordScores extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
       
	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		Integer exam_date_id, rec;
		
		try {
			exam_date_id = Integer.parseInt(request.getParameter("exam_date_id"));
			// If the argument of Integer.parseInt is null or is a string of length zero, a
			// NumberFormatException is thrown
			// @see https://docs.oracle.com/javase/7/docs/api/java/lang/Integer.html#parseInt(java.lang.String)
		} catch (NumberFormatException | NullPointerException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Incorrect or missing param values");
			return;
		}
		
		EnrollsDAO enrollsDAO = new EnrollsDAO(connection);
		RecordDAO recordDAO = new RecordDAO(connection);
		ExamDateDAO examdatedao = new ExamDateDAO(connection);
		
		try {
			if (!examdatedao.CheckExamDateByProf(user.getId(), exam_date_id)) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println("Attempt to access a resource not owned by you!");
				session.invalidate();
				return;
			}
		} catch (SQLException s) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Cannot connect to the database!");
			return;
		}
		
		try {
			if (enrollsDAO.assertion_record(exam_date_id)) {

				// Generate new record in database using autoincrement option to autogenerate incremental ID
				recordDAO.writeRecordOnDb(exam_date_id);
				
				// Retrieve last ID created
				rec = recordDAO.getCurrentID(exam_date_id);
				
				// Bind each student enroll to the record just created
				enrollsDAO.recordScore(exam_date_id, rec);
				
				// Redirect to another servlet to get just recorded enrolls
				// results are not sent immediately by this servlet in order to avoid problems with
				// user refreshing the page after having done a POST to this servlet
				response.sendRedirect(session.getServletContext() + "/GetRecordedEnrolls?recordID=" + rec);
			}
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Cannot connect to the database!");
			return;
		}
	}

}
