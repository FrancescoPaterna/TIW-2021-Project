package controllers;

import java.io.BufferedReader;
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
import java.util.*;

import beans.MultipleUpdate;
import beans.User;
import utils.ConnectionHandler;
import dao.EnrollsDAO;
import dao.ExamDateDAO;
import utils.GoodScore;

import com.google.gson.Gson;

@WebServlet("/UpdateMultipleScore")
@MultipartConfig
public class UpdateMultipleScore extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();

		int exam_date_id;
		List<Integer> id_stud;
		String score;
		MultipleUpdate multipleUpdate;

		BufferedReader reader = request.getReader();
		Gson gson = new Gson();

		multipleUpdate = gson.fromJson(reader, MultipleUpdate.class);
		exam_date_id = multipleUpdate.getExam_date_id();
		id_stud = multipleUpdate.getId_stud();
		score = multipleUpdate.getScore();

		// check if the list of students is valid
		if (id_stud == null || id_stud.size() == 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("No students selected");
			return;
		}

		EnrollsDAO enrollsdao = new EnrollsDAO(connection);
		ExamDateDAO examdatedao = new ExamDateDAO(connection);
		User user = (User) session.getAttribute("user");

		// check if the mark is valid
		if (score == null || !(GoodScore.CheckValidScore(score))) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Selected score is not valid.");
			return;
		}

		// check if the user has the authorization to insert marks for the specified exam date
		try {
			if (examdatedao.CheckExamDateByProf(user.getId(), exam_date_id)) {
				try {
					for (Integer id : id_stud)
						enrollsdao.insertMark(exam_date_id, id, score);
				} catch (SQLException e) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().println("Cannot connect to the database");
					return;
				}
			} else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println("Attempt to access a resource not owned by you!");
				return;
			}
		} catch (SQLException s) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Cannot connect to the database");
			return;
		}

		// redirect to another servlet to retrieve session enrolls updated
		// results are not sent immediately by this servlet in order to avoid problems with
		// user refreshing the page after having done a POST to this servlet
		response.sendRedirect(getServletContext().getContextPath() + "/GetSessionEnrolls?exam_date_id=" + exam_date_id);
	}
}