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

import beans.Course;
import beans.ExamDate;
import beans.User;
import dao.CourseDAO;
import dao.ExamDateDAO;
import utils.ConnectionHandler;

@WebServlet("/GoToExamDatesPro")
public class GoToExamDatesPro extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;
	
   public GoToExamDatesPro() {
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
		
		String path = null;
		Integer course_id; 
		String coursename = null;
		
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
		
		User user = (User) session.getAttribute("user");
		
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		/*Verifichiamo che il corso inserito appartenga al professore (Protezione Attacco SQL)*/
		CourseDAO courseDAO = new CourseDAO(connection);
		List<Course> courses = new ArrayList<>();
		
		try {
			courses = courseDAO.findCoursesByIdProf(user.getId());
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover courses");
			return;
		}
		
		boolean course_found = false;
		for(Course course : courses) {
			if(course.getId() == course_id) {
				course_found = true;
			}
		}
		
		if(!course_found) {
			path ="/WEB-INF/Forbidden.html";
			ctx.setVariable("error", "UNAUTHORIZED ACCESS");
			ctx.setVariable("description", "Attempt to access a resource not owned by you!");
			templateEngine.process(path, ctx, response.getWriter());
			session.invalidate();
			return;
		}
		
		/********************************************************************************************/
		

		ExamDateDAO ExamDateDAO = new ExamDateDAO(connection);
		List<ExamDate> exams = new ArrayList<>();
		
		try {
			exams = ExamDateDAO.FindExameDateBYCourseForProfessor(course_id);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover courses dates");
			return;
		}
		
		// Redirect to the HomePage and add courses to the parameters*/
		path = "/WEB-INF/ExamDatesPro.html";
		ctx.setVariable("coursename", coursename);
		ctx.setVariable("course_id", course_id);
		ctx.setVariable("exams", exams);
		templateEngine.process(path, ctx, response.getWriter());
	}
	
	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

