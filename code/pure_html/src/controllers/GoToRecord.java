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
import utils.ConnectionHandler;


import dao.EnrollsDAO;
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
		//response.sendRedirect(loginpath);
			return;
		}
		int exam_date_id = Integer.parseInt(request.getParameter("exam_date_id"));

		int rec;
		EnrollsDAO enrollsDAO = new EnrollsDAO(connection);
		RecordDAO recordDAO = new RecordDAO(connection);
		String coursename = StringEscapeUtils.escapeJava(request.getParameter("coursename"));
		Timestamp timestamp;
		String date;
		String time;
		List<Enroll> recorded;


		
		try {
			enrollsDAO.RecordScore(exam_date_id);
			System.out.println("OK 1");
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot record any Score");
			return;
		}
		
		try {
			recorded = enrollsDAO.FindRecordedStudents(exam_date_id);
			System.out.println("OK 2");
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot Find any Recorded Student");
			return;
		}
		
		try {
			recordDAO.WriteRecordOnDb(exam_date_id);
			System.out.println("OK 3");
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot generate new RECORD");
			return;
		}
		
		try {
			rec = recordDAO.getCurrentID(exam_date_id);
			System.out.println(rec);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot find the new RECORD ID");
			return;
		}
		
		
			
		try {
			timestamp = recordDAO.getCurrentTimestamp(rec);
			System.out.println(timestamp);
			date = new SimpleDateFormat("dd-MM-yyyy").format(timestamp);
			System.out.println(date);
			time = new SimpleDateFormat("HH:mm").format(timestamp);
			System.out.println(time);

		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot find timestamp and work on the date");
			return;
		}

		// Redirect to the HomePage and add courses to the parameters*/
		String path = "/WEB-INF/Record.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());


		ctx.setVariable("recorded", recorded);
		ctx.setVariable("rec", rec);
		ctx.setVariable("coursename", coursename);
		ctx.setVariable("date", date);
		ctx.setVariable("time", time);
		ctx.setVariable("exam_date_id", exam_date_id);


		templateEngine.process(path, ctx, response.getWriter());

	}

}
