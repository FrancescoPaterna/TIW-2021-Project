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

import com.google.gson.Gson;

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
		id_stud = Integer.parseInt(request.getParameter("id_stud"));
		exam_date_id = Integer.parseInt(request.getParameter("exam_date_id"));
		score = StringEscapeUtils.escapeJava(request.getParameter("score"));

		EnrollsDAO enrollsdao = new EnrollsDAO(connection);
		ExamDateDAO examdatedao = new ExamDateDAO(connection);
		User user = (User) session.getAttribute("user");

		if (score == null || !(GoodScore.CheckValidScore(score))) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Not Valid Score");
			return;

		}

		try {
			if (examdatedao.CheckExamDateByProf(user.getId(), exam_date_id)) {
				try {
					if (enrollsdao.insertMark(exam_date_id, id_stud, score) == 0) {
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println("Attempt to access a resource not owned by you!");
				session.invalidate();
				return;
			}
		} catch (SQLException s) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Cannot connect to the database");
			return;
		}

		//String serialized_score = new Gson().toJson(score);
		//response.setContentType("application/json");
		//response.setCharacterEncoding("UTF-8");
		//response.getWriter().write(serialized_score);
		//response.setStatus(HttpServletResponse.SC_OK);
		// redirect

		String path = getServletContext().getContextPath();
		response.sendRedirect(path + "/GetSessionEnrolls?exam_date_id=" + exam_date_id);
	}

}
