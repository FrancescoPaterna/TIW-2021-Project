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

import beans.User;
import dao.EnrollsDAO;
import utils.ConnectionHandler;

/**
 * Servlet implementation class UpdateResultStud
 */
@WebServlet("/UpdateResultStud")
@MultipartConfig
public class UpdateResultStud extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
       
	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
			response.getWriter().println("Incorrect or missing param values");
			return;
		}
		
		EnrollsDAO enrollsDAO = new EnrollsDAO(connection);

		// refuse score, this action is safe because we use the StudentID taken from the session user
		// so if another user tries to refuse a score of another student, this action has no effects
		try {
			enrollsDAO.RefuseScore(IDExamDate, user.getId());
			
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Not possible to refuse score");
			return;
		}
		
		// redirect to another servlet to get result details updated
		// results are not sent immediately by this servlet in order to avoid problems with
		// user refreshing the page after having done a POST to this servlet
		String path = getServletContext().getContextPath();
		response.sendRedirect(path + "/GetResultDetails?IDExamDate=" + IDExamDate);
		
	}

}
