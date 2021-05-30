package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;

import com.google.gson.Gson;

import beans.Enroll;
import beans.User;
import dao.EnrollsDAO;
import dao.ExamDateDAO;

import utils.ConnectionHandler;

/**
 * Servlet implementation class GoToSessionEnrolls
 */
@WebServlet("/GetSessionEnrolls")
public class GetSessionEnrolls extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	public GetSessionEnrolls() {
		super();
	}

	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();

		Integer exam_date_id = null;

		ServletContext servletContext = getServletContext();

		exam_date_id = Integer.parseInt(request.getParameter("exam_date_id"));

		ExamDateDAO examdateDAO = new ExamDateDAO(connection);
		User user = (User) session.getAttribute("user");

		try {
			if (!examdateDAO.CheckExamDateByProf(user.getId(), exam_date_id)) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println("UNAUTHORIZED ACCESS");
				session.invalidate();
				return;
			}
		} catch (NumberFormatException | NullPointerException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("DB Error");
			return;
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("DB Error");
			return;
		}

		EnrollsDAO enrollsDAO = new EnrollsDAO(connection);
		List<Enroll> enrolls = new ArrayList<>();

		try {
			enrolls = enrollsDAO.FindEnrollsOrderedByIDAsc(exam_date_id);

		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Not possible to recover enrolls for this exam date");
			return;
		}
		
		System.out.print("ECCOMI");
		// Redirect to the HomePage and add courses to the parameters*/
		String serialized_enrolls = new Gson().toJson(enrolls);		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(serialized_enrolls);

/*		try

		{
			publish = enrollsDAO.assertion_published(exam_date_id);
			record = enrollsDAO.assertion_record(exam_date_id);
		} catch (SQLException e) {
			e.printStackTrace();
		}*/

	}

	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
