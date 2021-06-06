package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;


import beans.Enroll;
import beans.User;
import utils.ConnectionHandler;
import utils.Rebuilder;

import dao.EnrollsDAO;
import dao.ExamDateDAO;
import dao.RecordDAO;

//import utils.GenPdf;

/**
 * Servlet implementation class GoToRecord
 */
@WebServlet("/GoToRecord")
public class GetRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */

	public GetRecord() {
		super();
	}

	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// If the user is not logged in (not present in session) redirect to the login
		HttpSession session = request.getSession();

		int sort = 1;
		int rec, secretsortcode, exam_date_id, course_id;
		String mask, recovered_mask, coursename, date, time, path;
		Timestamp timestamp;

		EnrollsDAO enrollsDAO = new EnrollsDAO(connection);
		RecordDAO recordDAO = new RecordDAO(connection);
		ExamDateDAO examdatedao = new ExamDateDAO(connection);
		User user = (User) session.getAttribute("user");

		course_id = Integer.parseInt(request.getParameter("course_id"));
		exam_date_id = Integer.parseInt(request.getParameter("exam_date_id"));
		coursename = StringEscapeUtils.escapeJava(request.getParameter("coursename"));
		date = StringEscapeUtils.escapeJava(request.getParameter("date"));
		mask = StringEscapeUtils.escapeJava(request.getParameter("mask"));
		secretsortcode = Integer.parseInt(request.getParameter("secret_code"));
		// target = "/GoToSessionEnrolls";

		ServletContext servletContext = getServletContext();

		recovered_mask = Rebuilder.resetMask(mask, secretsortcode);
		sort = Rebuilder.resetSecretSortCode(secretsortcode);



		List<Enroll> recorded;

	/*	try {
			if (!examdatedao.CheckExamDateByProf(user.getId(), exam_date_id)) {
				path = "/WEB-INF/Forbidden.html";
				ctx.setVariable("error", "UNAUTHORIZED ACCESS");
				ctx.setVariable("description", "Attempt to access a resource not owned by you!");
				session.invalidate();
				return;
			}
		} catch (SQLException s) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot connect to the database");
			return;
		}

		try {
			if (enrollsDAO.assertion_record(exam_date_id)) {

				try {
					recordDAO.writeRecordOnDb(exam_date_id);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot generate new RECORD");
					return;
				}

				try {
					rec = recordDAO.getCurrentID(exam_date_id);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot find the new RECORD ID");
					return;
				}

				try {
					enrollsDAO.recordScore(exam_date_id, rec);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot record any Score");
					return;
				}

				try {
					recorded = enrollsDAO.findRecordedStudents(rec);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Cannot Find any Recorded Student");
					return;
				}

				try {
					timestamp = recordDAO.getCurrentTimestamp(rec);
					date = new SimpleDateFormat("dd-MM-yyyy").format(timestamp);
					time = new SimpleDateFormat("HH:mm").format(timestamp);

				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Cannot find timestamp and work on the date");
					return;
				}

				// Redirect to the HomePage and add courses to the parameters*/

		/*	} else {
				path = "/WEB-INF/Warning.html";
				ctx.setVariable("error", "Impossible To Record Score");
				ctx.setVariable("description", "Cannot Find Any Score in Published State!");

				return;
		templateResolver.setSuffix(".html");

			}

		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Course not recognized");
		}
*/
	}

}