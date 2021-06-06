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

/**
 * Servlet implementation class UpdateStatus
 */
@WebServlet("/UpdateStatus")
@MultipartConfig
public class UpdateStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateStatus() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User user = null;
		HttpSession session = request.getSession();

		user = (User) request.getSession().getAttribute("user");

		// reset mask value

		if (user.getRole().equals("professor")) {

			Integer exam_date_id;
			exam_date_id = Integer.parseInt(request.getParameter("exam_date_id"));
			
			EnrollsDAO enrollsDAO = new EnrollsDAO(connection);
			ExamDateDAO examdatedao = new ExamDateDAO(connection);


			try {
				if (!examdatedao.CheckExamDateByProf(user.getId(), exam_date_id)) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().println("UNAUTHORIZED ACCESS,Attempt to access a resource not owned by you!");
					session.invalidate();
					return;
				}
			} catch (SQLException s) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println("UNAUTHORIZED ACCESS,Attempt to access a resource not owned by you!");
				session.invalidate();
				return;
			}

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
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
			response.getWriter().println("UNAUTHORIZED USER");

		}
	}

}
