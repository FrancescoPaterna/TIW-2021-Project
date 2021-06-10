package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.annotation.MultipartConfig;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;

import beans.User;
import utils.ConnectionHandler;
import dao.EnrollsDAO;
import dao.ExamDateDAO;
import utils.GoodScore;

/**
 * Servlet implementation class UpdateMark
 */
@WebServlet("/UpdateScore")
@MultipartConfig
public class UpdateScore extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateScore() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();

		Integer id_stud, exam_date_id;
		String score;
		
		try {
			id_stud = Integer.parseInt(request.getParameter("id_stud"));
			exam_date_id = Integer.parseInt(request.getParameter("exam_date_id"));
			score = StringEscapeUtils.escapeJava(request.getParameter("score"));
			// If the argument of Integer.parseInt is null or is a string of length zero, a
			// NumberFormatException is thrown
			// @see https://docs.oracle.com/javase/7/docs/api/java/lang/Integer.html#parseInt(java.lang.String)
		} catch (NumberFormatException | NullPointerException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Incorrect or missing param values");
			return;
		}
	
		EnrollsDAO enrollsdao = new EnrollsDAO(connection);
		ExamDateDAO examdatedao = new ExamDateDAO(connection);
		User user = (User) session.getAttribute("user");

		// Check if th score is valid
		if (score == null || !(GoodScore.CheckValidScore(score))) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Not Valid Score");
			return;

		}

		try {
			if (examdatedao.CheckExamDateByProf(user.getId(), exam_date_id)) {
				try {
					if (enrollsdao.insertMark(exam_date_id, id_stud, score) == 0) {
						response.setStatus(HttpServletResponse.SC_FORBIDDEN);
						response.getWriter()
								.println("UNAUTHORIZED MODIFY,Attempt to modify a score recorded, published or refused!");
						session.invalidate();
						return;
					}
				} catch (SQLException e) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().println("Cannot connect to the database");
					return;
				}
			} else {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				response.getWriter().println("Attempt to access a resource not owned by you!");
				session.invalidate();
				return;
			}
		} catch (SQLException s) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Cannot connect to the database");
			return;
		}

		// Redirect to the servlet in order to get the update list of session enrolls
		String path = getServletContext().getContextPath();
		response.sendRedirect(path + "/GetSessionEnrolls?exam_date_id=" + exam_date_id);
	}

}
