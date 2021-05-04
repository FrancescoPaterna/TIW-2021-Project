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
import utils.ConnectionHandler;
import utils.GoodScore;

/**
 * Servlet implementation class UpdateResultStud
 */
@WebServlet("/UpdateResultStud")
public class UpdateResultStud extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;

	public UpdateResultStud() {
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
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
		
		int IDExamDate;
		User user = (User) session.getAttribute("user");
		String coursename;
		String date;
		User professor = new User();
		IDExamDate = Integer.parseInt(request.getParameter("IDExamDate"));
		IDExamDate = Integer.parseInt(request.getParameter("IDExamDate"));
		coursename = StringEscapeUtils.escapeJava(request.getParameter("coursename"));
		date = StringEscapeUtils.escapeJava(request.getParameter("date"));
		professor.setEmail(request.getParameter("professore"));
		professor.setName(request.getParameter("professorn"));
		professor.setSurname(request.getParameter("professors"));

		EnrollsDAO enrollsDAO = new EnrollsDAO(connection);
		
		Enroll enroll;

		try {
			enrollsDAO.RefuseScore(IDExamDate, user.getId());
			
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not Possible to refuse score");
			return;
		}
			
		try {

			enroll = enrollsDAO.FindStudentScore(IDExamDate, user.getId());
				
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to get Scores");
			return;
		}
			
		if (!(enroll.getStatus() == Status.NOT_INSERTED || enroll.getStatus() == Status.INSERTED)) {
			if (enroll.getStatus() == Status.PUBLISHED && GoodScore.CheckGoodScore(enroll.getMark())) {
				String path = "/WEB-INF/Result.html";
				ServletContext servletContext = getServletContext();
				final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
				ctx.setVariable("enroll", enroll);
				ctx.setVariable("enroll", enroll);
				ctx.setVariable("date", date);
				ctx.setVariable("coursename", coursename);
				ctx.setVariable("IDExamDate", IDExamDate);
				ctx.setVariable("professor", professor);
				templateEngine.process(path, ctx, response.getWriter());
			} else {
				String path = "/WEB-INF/ResultLocked.html";
				ServletContext servletContext = getServletContext();
				final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());		
				ctx.setVariable("enroll", enroll);
				ctx.setVariable("rej", "Score Rejected!");
				ctx.setVariable("enroll", enroll);
				ctx.setVariable("date", date);
				ctx.setVariable("coursename", coursename);
				ctx.setVariable("IDExamDate", IDExamDate);
				ctx.setVariable("professor", professor);
				templateEngine.process(path, ctx, response.getWriter());
			}

		} else {				
			String path = "/WEB-INF/ResultEmpty.html";
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("enroll", enroll);
			templateEngine.process(path, ctx, response.getWriter());
		}
	}
}

