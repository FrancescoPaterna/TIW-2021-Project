package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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
import beans.Status;
import beans.User;
import dao.EnrollsDAO;
import dao.UserDAO;
import utils.ConnectionHandler;
import utils.GoodScore;
import utils.ParamsChecker;

@WebServlet("/GoToResult")
public class GoToResult extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;

	public GoToResult() {
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
	
	

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Integer IDExamDate;
		Integer course_id;
		String coursename;
		String date;
		
		HttpSession session = request.getSession();
		
		User user = (User) session.getAttribute("user");
		
		try {
			IDExamDate = Integer.parseInt(request.getParameter("IDExamDate"));
			coursename = StringEscapeUtils.escapeJava(request.getParameter("coursename"));
			date = StringEscapeUtils.escapeJava(request.getParameter("date"));
			course_id = Integer.parseInt(request.getParameter("course_id"));
		} catch (NumberFormatException | NullPointerException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
			return;
		}
		
		// check params
		if(!ParamsChecker.checkParam(date) || !ParamsChecker.checkParam(coursename)) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
			return;
		}
		


		EnrollsDAO enrollsDAO = new EnrollsDAO(connection);
		Enroll enroll;
		
		UserDAO userDAO = new UserDAO(connection);
		User professor;
		
		try {
			enroll = enrollsDAO.FindStudentScore(IDExamDate, user.getId());

		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to get Scores");
			return;
		}

		try {
			professor = userDAO.findProfessorByIdCourse(course_id);
			
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to find professor for that course");
			return;
		}
		


		if (!(enroll.getStatus() == Status.NOT_INSERTED || enroll.getStatus() == Status.INSERTED)) {
			if (enroll.getStatus() == Status.PUBLISHED && GoodScore.CheckGoodScore(enroll.getMark())) {
				String path = "/WEB-INF/Result.html";
				ServletContext servletContext = getServletContext();
				final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
				ctx.setVariable("course_id", course_id);
				ctx.setVariable("enroll", enroll);
				ctx.setVariable("date", date);
				ctx.setVariable("coursename", coursename);
				ctx.setVariable("IDExamDate", IDExamDate);
				ctx.setVariable("professor", professor);
				//ctx.setVariable("REFUSE", true); Pure Thymeleaf Version
				templateEngine.process(path, ctx, response.getWriter());
			} else {
				String path = "/WEB-INF/ResultLocked.html";
				ServletContext servletContext = getServletContext();
				final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
				ctx.setVariable("enroll", enroll);
				ctx.setVariable("date", date);
				ctx.setVariable("coursename", coursename);
				ctx.setVariable("IDExamDate", IDExamDate);
				ctx.setVariable("professor", professor);
				ctx.setVariable("course_id", course_id);
				//ctx.setVariable("REFUSE", false); Pure Thymeleaf Version
				templateEngine.process(path, ctx, response.getWriter());
			}

		} else {
			String path = "/WEB-INF/ResultEmpty.html";
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("enroll", enroll);
			ctx.setVariable("date", date);
			ctx.setVariable("coursename", coursename);
			ctx.setVariable("IDExamDate", IDExamDate);
			ctx.setVariable("professor", professor);
			ctx.setVariable("course_id", course_id);

			templateEngine.process(path, ctx, response.getWriter());
		}
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	
}
