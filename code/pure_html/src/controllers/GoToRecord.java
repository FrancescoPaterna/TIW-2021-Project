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

import dao.EnrollsDAO;
import dao.ExamDateDAO;
import dao.RecordDAO;

//import utils.GenPdf;

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
		String loginpath = "/index.html";
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("user") == null) {
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsg", "You're not logged in");
			templateEngine.process(loginpath, ctx, response.getWriter());
			return;
		}

		int sort = 1;
		int rec, secretsortcode, exam_date_id, course_id;
		String mask, recovered_mask, coursename, date, time, path, sorturl;
		Timestamp timestamp;

		EnrollsDAO enrollsDAO = new EnrollsDAO(connection);
		RecordDAO recordDAO = new RecordDAO(connection);
		ExamDateDAO examdatedao = new ExamDateDAO(connection);
		User user = (User) session.getAttribute("user");

		course_id = Integer.parseInt(request.getParameter("course_id"));
		exam_date_id = Integer.parseInt(request.getParameter("exam_date_id"));
		coursename = StringEscapeUtils.escapeJava(request.getParameter("coursename"));
		coursename = StringEscapeUtils.escapeJava(request.getParameter("coursename"));
		date = StringEscapeUtils.escapeJava(request.getParameter("date"));
		mask = StringEscapeUtils.escapeJava(request.getParameter("mask"));
		secretsortcode = Integer.parseInt(request.getParameter("secret_code"));
		//target = "/GoToSessionEnrolls";

		path = "/WEB-INF/Record.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

		/* Recover URI To Go Back */
		if (secretsortcode == 00) {
			if (mask.charAt(0) == '0') {
				recovered_mask = '0' + mask.substring(1, 7);
				sort = 1;
				ctx.setVariable("mask", recovered_mask);
				ctx.setVariable("sort", sort);


			}
		} else if (secretsortcode == 11) {
			if (mask.charAt(0) == '0') {
				recovered_mask = '1' + mask.substring(1, 7);
				sort = 1;
				ctx.setVariable("mask", recovered_mask);
				ctx.setVariable("sort", sort);



			}
		} else if (secretsortcode == 10) {
			if (mask.charAt(0) == '1') {
				recovered_mask = '0' + mask.substring(1, 7);
				sort = 1;
				ctx.setVariable("mask", recovered_mask);
				ctx.setVariable("sort", sort);



			}
		}

		else if (secretsortcode == 21) {
			if (mask.charAt(1) == '0') {
				recovered_mask = mask.substring(0, 1) + '1' + mask.substring(2, 7);
				sort = 2;
				ctx.setVariable("mask", recovered_mask);
				ctx.setVariable("sort", sort);



			}
		}

		else if (secretsortcode == 20) {
			if (mask.charAt(1) == '1') {
				recovered_mask = mask.substring(0, 1) + '0' + mask.substring(2, 7);
				sort = 2;
				ctx.setVariable("mask", recovered_mask);
				ctx.setVariable("sort", sort);



			}
		}

		else if (secretsortcode == 31) {
			if (mask.charAt(2) == '0') {
				recovered_mask = mask.substring(0, 2) + '1' + mask.substring(3, 7);
				sort = 3;
				ctx.setVariable("mask", recovered_mask);
				ctx.setVariable("sort", sort);



			}

		}

		else if (secretsortcode == 30) {
			if (mask.charAt(2) == '1') {
				recovered_mask = mask.substring(0, 2) + '0' + mask.substring(3, 7);
				sort = 3;
				ctx.setVariable("mask", recovered_mask);
				ctx.setVariable("sort", sort);



			}

		} else if (secretsortcode == 41) {
			if (mask.charAt(3) == '0') {
				recovered_mask = mask.substring(0, 3) + '1' + mask.substring(4, 7);
				sort = 4;
				ctx.setVariable("mask", recovered_mask);
				ctx.setVariable("sort", sort);



			}
		}

		else if (secretsortcode == 40) {
			if (mask.charAt(3) == '1') {
				recovered_mask = mask.substring(0, 3) + '0' + mask.substring(4, 7);
				sort = 4;
				ctx.setVariable("mask", recovered_mask);
				ctx.setVariable("sort", sort);



			}
		} else if (secretsortcode == 51) {
			if (mask.charAt(4) == '0') {
				recovered_mask = mask.substring(0, 4) + '1' + mask.substring(5, 7);
				sort = 5;
				ctx.setVariable("mask", recovered_mask);
				ctx.setVariable("sort", sort);



			}
		}

		else if (secretsortcode == 50) {
			if (mask.charAt(4) == '1') {
				recovered_mask = mask.substring(0, 4) + '0' + mask.substring(5, 7);
				sort = 5;
				ctx.setVariable("mask", recovered_mask);
				ctx.setVariable("sort", sort);



			}
		} else if (secretsortcode == 61) {
			if (mask.charAt(5) == '0') {
				recovered_mask = mask.substring(0, 5) + '1' + mask.substring(6, 7);
				sort = 6;
				ctx.setVariable("mask", recovered_mask);
				ctx.setVariable("sort", sort);


			}
		}

		else if (secretsortcode == 60) {
			if (mask.charAt(5) == '1') {
				recovered_mask = mask.substring(0, 5) + '0' + mask.substring(6, 7);
				sort = 6;
				ctx.setVariable("mask", recovered_mask);
				ctx.setVariable("sort", sort);


			}
		} else if (secretsortcode == 71) {
			if (mask.charAt(6) == '0') {
				recovered_mask = mask.substring(0, 6) + '1';
				sort = 7;
				ctx.setVariable("mask", recovered_mask);
				ctx.setVariable("sort", sort);


			}
		}

		else if (secretsortcode == 70) {
			if (mask.charAt(6) == '1') {
				recovered_mask = mask.substring(0, 6) + '0';
				sort = 7;
				ctx.setVariable("mask", recovered_mask);
				ctx.setVariable("sort", sort);


			}
		}

		/*******************************************************************************************************/

		List<Enroll> recorded;

		try {
			if(!examdatedao.CheckExamDateByProf(user.getId() ,exam_date_id)) {
					 path ="/WEB-INF/Forbidden.html";
						ctx.setVariable("error", "UNAUTHORIZED ACCESS");
						ctx.setVariable("description", "Attempt to access a resource not owned by you!");
						templateEngine.process(path, ctx, response.getWriter());
						session.invalidate();
						return;
				}
		}
			catch (SQLException s) {
			 path ="/WEB-INF/Forbidden.html";
				ctx.setVariable("error", "UNAUTHORIZED ACCESS");
				ctx.setVariable("description", "Attempt to access a resource not owned by you!");
				templateEngine.process(path, ctx, response.getWriter());
				session.invalidate();
				return;
		}
		
		
		
		
		
		
		try {
			if (enrollsDAO.assertion_record(exam_date_id)) {

				try {
					recordDAO.WriteRecordOnDb(exam_date_id);
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
					enrollsDAO.RecordScore(exam_date_id, rec);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot record any Score");
					return;
				}

				try {
					recorded = enrollsDAO.FindRecordedStudents(exam_date_id);
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

				ctx.setVariable("recorded", recorded);
				ctx.setVariable("rec", rec);
				ctx.setVariable("coursename", coursename);
				ctx.setVariable("date", date);
				ctx.setVariable("time", time);
				ctx.setVariable("exam_date_id", exam_date_id);
				ctx.setVariable("course_id", course_id);

				templateEngine.process(path, ctx, response.getWriter());
			}
			else {
				 path ="/WEB-INF/Warning.html";
					ctx.setVariable("error", "Impossible To Record Score");
					ctx.setVariable("description", "Cannot Find Any Score in Published State!");
					templateEngine.process(path, ctx, response.getWriter());
					return;

			}

		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Course not recognized");
		}

	}

}
