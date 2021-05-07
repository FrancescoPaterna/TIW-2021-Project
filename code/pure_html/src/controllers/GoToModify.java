package controllers;

import java.io.IOException;
import java.sql.Connection;


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

//import beans.User;
import utils.ConnectionHandler;

/**
 * Servlet implementation class GoToMOdify
 */
@WebServlet("/GoToModify")
public class GoToModify extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToModify() {
        super();
        // TODO Auto-generated constructor stub
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		//User user = (User) session.getAttribute("user");   //TDDO Da usare per un controllo
		
		Integer secret_code, course_id, id_stud, exam_date_id; 
		String coursename, mask, date, name, surname, email, coursedeg, mark, status;
		
		secret_code = Integer.parseInt(request.getParameter("secret_code"));
		course_id = Integer.parseInt(request.getParameter("course_id"));
		id_stud = Integer.parseInt(request.getParameter("id_stud"));
		exam_date_id = Integer.parseInt(request.getParameter("exam_date_id"));

		mark = StringEscapeUtils.escapeJava(request.getParameter("mark"));
		coursename = StringEscapeUtils.escapeJava(request.getParameter("coursename"));
		mask = StringEscapeUtils.escapeJava(request.getParameter("mask"));
		date = StringEscapeUtils.escapeJava(request.getParameter("date"));
		name = StringEscapeUtils.escapeJava(request.getParameter("name"));
		surname = StringEscapeUtils.escapeJava(request.getParameter("surname"));
		email = StringEscapeUtils.escapeJava(request.getParameter("email"));
		coursedeg = StringEscapeUtils.escapeJava(request.getParameter("coursedeg"));
		status = StringEscapeUtils.escapeJava(request.getParameter("status"));


		
		
			String path ="/WEB-INF/Modify.html";
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("secret_code", secret_code);
			ctx.setVariable("course_id", course_id);
			ctx.setVariable("mark", mark);
			ctx.setVariable("id_stud", id_stud);
			ctx.setVariable("exam_date_id", exam_date_id);


			ctx.setVariable("coursename", coursename);
			ctx.setVariable("mask", mask);
			ctx.setVariable("date", date);
			ctx.setVariable("name", name);
			ctx.setVariable("surname", surname);
			ctx.setVariable("email", email);
			ctx.setVariable("coursedeg", coursedeg);
			ctx.setVariable("status", status);

			templateEngine.process(path, ctx, response.getWriter());
		
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
