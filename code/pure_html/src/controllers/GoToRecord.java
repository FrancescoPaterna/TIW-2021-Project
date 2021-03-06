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
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import beans.Enroll;
import beans.User;
import utils.ConnectionHandler;
import utils.ParamsChecker;
import dao.EnrollsDAO;
import dao.ExamDateDAO;
import dao.RecordDAO;


/**
 * Servlet implementation class GoToRecord
 */
@WebServlet("/GoToRecord")
public class GoToRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */

	public GoToRecord() {
		super();
	}

	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();

		int rec, exam_date_id, course_id;
		Integer sort;
		String mask, coursename, date, time, path, professor;
		Timestamp timestamp;

		EnrollsDAO enrollsDAO = new EnrollsDAO(connection);
		RecordDAO recordDAO = new RecordDAO(connection);
		ExamDateDAO examdatedao = new ExamDateDAO(connection);
		
		User user = (User) session.getAttribute("user");
		
		professor = user.getName() + ' ' + user.getSurname();
		
		try {
			sort = Integer.parseInt(request.getParameter("sort"));
			course_id = Integer.parseInt(request.getParameter("course_id"));
			exam_date_id = Integer.parseInt(request.getParameter("exam_date_id"));
			coursename = StringEscapeUtils.escapeJava(request.getParameter("coursename"));
			date = StringEscapeUtils.escapeJava(request.getParameter("date"));
			mask = StringEscapeUtils.escapeJava(request.getParameter("mask"));
		} catch (NumberFormatException | NullPointerException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
			return;
		}
		
		// check params
		if(!ParamsChecker.checkParam(mask) || !ParamsChecker.checkParam(date) || !ParamsChecker.checkParam(coursename)) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
			return;
		}
		

		path = "/WEB-INF/Record.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

		List<Enroll> recorded;

		try {
			if (!examdatedao.CheckExamDateByProf(user.getId(), exam_date_id)) {
				path = "/WEB-INF/Forbidden.html";
				ctx.setVariable("error", "UNAUTHORIZED ACCESS");
				ctx.setVariable("description", "Attempt to access a resource not owned by you!");
				templateEngine.process(path, ctx, response.getWriter());
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

				ctx.setVariable("mask", mask);
				ctx.setVariable("sort", sort);
				ctx.setVariable("recorded", recorded);
				ctx.setVariable("rec", rec);
				ctx.setVariable("coursename", coursename);
				ctx.setVariable("date", date);
				ctx.setVariable("time", time);
				ctx.setVariable("exam_date_id", exam_date_id);
				ctx.setVariable("course_id", course_id);
				ctx.setVariable("professor", professor);

				templateEngine.process(path, ctx, response.getWriter());
			} else {
				path = "/WEB-INF/Warning.html";
				ctx.setVariable("error", "Impossible To Record Score");
				ctx.setVariable("description", "Cannot Find Any Score in PUBLISHED or REJECTED Status!");
				templateEngine.process(path, ctx, response.getWriter());
				return;

			}

		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Course not recognized");
		}

	}

}