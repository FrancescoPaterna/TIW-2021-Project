package controllers;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import beans.User;
import dao.EnrollsDAO;
import dao.ExamDateDAO;
import utils.ConnectionHandler;

@WebServlet("/UpdateStatus")
@MultipartConfig
public class UpdateStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User user = null;
		HttpSession session = request.getSession();

		user = (User) request.getSession().getAttribute("user");

		if (user.getRole().equals("professor")) {

			Integer exam_date_id;
			
			try {
				exam_date_id = Integer.parseInt(request.getParameter("exam_date_id"));
				// If the argument of Integer.parseInt is null or is a string of length zero, a NumberFormatException is thrown
				// @see https://docs.oracle.com/javase/7/docs/api/java/lang/Integer.html#parseInt(java.lang.String)
			} catch (NumberFormatException | NullPointerException e) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("Incorrect or missing param values");
				return;
			}
			
			
			EnrollsDAO enrollsDAO = new EnrollsDAO(connection);
			ExamDateDAO examdatedao = new ExamDateDAO(connection);

			// check if the user has the authorization to update status for the specified exam date
			try {
				if (!examdatedao.CheckExamDateByProf(user.getId(), exam_date_id)) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().println("UNAUTHORIZED ACCESS,Attempt to access a resource not owned by you!");
					session.invalidate();
					return;
				}
			} catch (SQLException s) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println("DB error");
				return;
			}

			// if there are scores to publish, publish them
			try {
				if (enrollsDAO.assertion_published(exam_date_id)) {
					try {
						enrollsDAO.PublishScore(exam_date_id);
					} catch (SQLException e) {
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						response.getWriter().println("Cannot Publish Any Score");
						return;
					}
				} else {
					response.setStatus(HttpServletResponse.SC_NO_CONTENT);
					response.getWriter()
							.println("Impossible To Publish Any Score, Cannot Find Any Score in Published State!");
					return;
				}
			} catch (SQLException | IOException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println("Cannot Publish Any Score");
			}

			response.setStatus(HttpServletResponse.SC_OK);

		} else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().println("UNAUTHORIZED USER");
		}
	}

}
