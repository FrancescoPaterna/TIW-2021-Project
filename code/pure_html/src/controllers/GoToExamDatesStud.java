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
		
		// get the user from the session
		User user = (User) session.getAttribute("user");
		Integer course_id; 
		String coursename;
		
		// check params
		try {
			course_id = Integer.parseInt(request.getParameter("course_id"));
			// If the argument of Integer.parseInt is null or is a string of length zero, a NumberFormatException is thrown
			// @see
			// https://docs.oracle.com/javase/7/docs/api/java/lang/Integer.html#parseInt(java.lang.String)
		} catch (NumberFormatException | NullPointerException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
			return;
		}
		
		coursename = StringEscapeUtils.escapeJava(request.getParameter("coursename"));
		if(coursename == null || coursename.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
			return;
		}

		
		ExamDateDAO ExamDateDAO = new ExamDateDAO(connection);
		List<ExamDate> exams = new ArrayList<>();
		
		
		// get the exam dates the student is signed up for from the database
		// this query is safe from attacks because it uses user_id to get the exam dates, 
		// which is stored in the session
		try {
			
			exams = ExamDateDAO.FindExameDateBYCourseForStudent(user.getId(), course_id);

		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover courses dates");
			return;
		}
		
		// Redirect to the ExamDates page and add exam dates to the parameters if there are any, else
		// redirect to a page to inform the user that he is not enrolled to any exam date for this course
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