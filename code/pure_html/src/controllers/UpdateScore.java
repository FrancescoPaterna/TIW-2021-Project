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

import beans.Status;
import beans.User;
import utils.ConnectionHandler;
import dao.EnrollsDAO;
import dao.ExamDateDAO;
import utils.GoodScore;

/**
 * Servlet implementation class UpdateMark
 */
@WebServlet("/UpdateScore")
public class UpdateScore extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateScore() {
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


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		
		Integer secret_code, course_id, id_stud, exam_date_id, sort; 
		String coursename, mask, date, name, surname, email, coursedeg, score, status, path;
		
		secret_code = Integer.parseInt(request.getParameter("secret_code"));
		course_id = Integer.parseInt(request.getParameter("course_id"));
		id_stud = Integer.parseInt(request.getParameter("id_stud"));
		exam_date_id = Integer.parseInt(request.getParameter("exam_date_id"));
		sort = Integer.parseInt(request.getParameter("sort"));

		 

		score = StringEscapeUtils.escapeJava(request.getParameter("score"));
		coursename = StringEscapeUtils.escapeJava(request.getParameter("coursename"));
		mask = StringEscapeUtils.escapeJava(request.getParameter("mask"));
		date = StringEscapeUtils.escapeJava(request.getParameter("date"));
		name = StringEscapeUtils.escapeJava(request.getParameter("name"));
		surname = StringEscapeUtils.escapeJava(request.getParameter("surname"));
		email = StringEscapeUtils.escapeJava(request.getParameter("email"));
		coursedeg = StringEscapeUtils.escapeJava(request.getParameter("coursedeg"));

		EnrollsDAO enrollsdao = new EnrollsDAO(connection);
		ExamDateDAO examdatedao = new ExamDateDAO(connection);
		User user = (User) session.getAttribute("user");

		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		if(score == null || !(GoodScore.CheckValidScore(score))) {
			 path ="/WEB-INF/Warning.html";
				ctx.setVariable("error", "Not Valid Score");
				ctx.setVariable("description", "Selected Score is not Valid");
				templateEngine.process(path, ctx, response.getWriter());
				return;
			
		}
		
		

			try {
				if(examdatedao.CheckExamDateByProf(user.getId() ,exam_date_id)) {
					try {
						if(enrollsdao.insertMark(exam_date_id, id_stud, score) == 0) {
							path ="/WEB-INF/Forbidden.html";
							ctx.setVariable("error", "UNAUTHORIZED MODIFY");
							ctx.setVariable("description", "Attempt to modify a score recorded or refused");
							templateEngine.process(path, ctx, response.getWriter());
							session.invalidate();
							return;
						}
					} catch (SQLException e) {
						 path ="/WEB-INF/Forbidden.html";
							ctx.setVariable("error", "UNAUTHORIZED MODIFY");
							ctx.setVariable("description", "Attempt to modify a score recorded or refused");
							templateEngine.process(path, ctx, response.getWriter());
							session.invalidate();
							return;
					}
				}
				else {
					 path ="/WEB-INF/Forbidden.html";
						ctx.setVariable("error", "UNAUTHORIZED ACCESS");
						ctx.setVariable("description", "Attempt to access a resource not owned by you!");
						templateEngine.process(path, ctx, response.getWriter());
						session.invalidate();
						return;
				}
			} catch (SQLException s) {
				 path ="/WEB-INF/Forbidden.html";
					ctx.setVariable("error", "UNAUTHORIZED ACCESS");
					ctx.setVariable("description", "Attempt to access a resource not owned by you!");
					templateEngine.process(path, ctx, response.getWriter());
					session.invalidate();
					return;
			}
		
		
			path ="/WEB-INF/Modify.html";

			ctx.setVariable("secret_code", secret_code);
			ctx.setVariable("course_id", course_id);
			ctx.setVariable("id_stud", id_stud);
			ctx.setVariable("exam_date_id", exam_date_id);
			ctx.setVariable("sort", sort);

			ctx.setVariable("mark", score);
			ctx.setVariable("coursename", coursename);
			ctx.setVariable("mask", mask);
			ctx.setVariable("date", date);
			ctx.setVariable("name", name);
			ctx.setVariable("surname", surname);
			ctx.setVariable("email", email);
			ctx.setVariable("coursedeg", coursedeg);
			ctx.setVariable("status", "INSERTED");

			templateEngine.process(path, ctx, response.getWriter());
		
	
	
	}

}
