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
		
		Integer secretsortcode, course_id, id_stud, exam_date_id, sort; 
		String coursename, mask, date, name, surname, email, coursedeg, mark, status, recovered_mask;
		
		secretsortcode = Integer.parseInt(request.getParameter("secret_code"));
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

		
		

			ctx.setVariable("secret_code", secretsortcode);
			ctx.setVariable("course_id", course_id);
			ctx.setVariable("id_stud", id_stud);
			ctx.setVariable("exam_date_id", exam_date_id);

			ctx.setVariable("mark", mark);
			ctx.setVariable("coursename", coursename);
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
