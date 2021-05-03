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
import beans.User;
import dao.CourseDAO;
import utils.ConnectionHandler;


@WebServlet("/GoToHomePagePro")
public class GoToHomePagePro extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;
	
    public GoToHomePagePro() {
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
		
		User user = (User) session.getAttribute("user");
		CourseDAO courseDAO = new CourseDAO(connection);
		List<Course> courses = new ArrayList<>();
		
		try {
			courses = courseDAO.findCoursesByIdProf(user.getId());
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover courses");
			return;
		}
		
		// Redirect to the HomePage and add courses to the parameters
		String path ="/WEB-INF/HomePro.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("courses", courses);
		templateEngine.process(path, ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String path = null;
			int course_id; 
			String coursename = null;
			course_id = Integer.parseInt(request.getParameter("course_id"));
			coursename = StringEscapeUtils.escapeJava(request.getParameter("coursename"));

			HttpSession session = request.getSession();
			
			User user = (User) session.getAttribute("user");
			
			CourseDAO courseDAO = new CourseDAO(connection);
			List<Course> courses = new ArrayList<>();
			
			try {
				courses = courseDAO.findCoursesByIdProf(user.getId());
				request.getSession().setAttribute("courses", courses); //TODO: Discutere con il Team
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
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Accesso Non Autorizzato");
			}
			else {
				request.getSession().setAttribute("course_id", course_id);
				request.getSession().setAttribute("coursename", coursename);
				path = getServletContext().getContextPath();
				String target = "/GoToExamDatesPro";
				response.sendRedirect(path + target);
				
			}

	}
	
	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
