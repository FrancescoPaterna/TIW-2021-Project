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
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import beans.ExamDate;
import beans.User;
import dao.ExamDateDAO;
import utils.ConnectionHandler;

/**
 * Servlet implementation class GoToSessionEnrolls
 */
@WebServlet("/GoToExamDatesStud")
public class GoToExamDatesStud extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;
	
   public GoToExamDatesStud() {
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		User user = (User) session.getAttribute("user");
		Integer course_id; 
		String coursename;
		course_id = Integer.parseInt(request.getParameter("course_id"));
		coursename = StringEscapeUtils.escapeJava(request.getParameter("coursename"));

		
		ExamDateDAO ExamDateDAO = new ExamDateDAO(connection);
		List<ExamDate> exams = new ArrayList<>();
		
		try {
			
			exams = ExamDateDAO.FindExameDateBYCourseForStudent(user.getId(), course_id);

		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover courses");
			return;
		}
		
		// Redirect to the HomePage and add courses to the parameters*/
		if(!(exams.isEmpty())) {
		String path ="/WEB-INF/ExamDatesStud.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("course_id", course_id);
		ctx.setVariable("exams", exams);
		ctx.setVariable("coursename", coursename);
		templateEngine.process(path, ctx, response.getWriter());
		}
		else {
			String path ="/WEB-INF/ExamDatesStudEmpty.html";
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("course_id", course_id);
			ctx.setVariable("exams", exams);
			ctx.setVariable("coursename", coursename);
			templateEngine.process(path, ctx, response.getWriter());
		}
			
		}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}