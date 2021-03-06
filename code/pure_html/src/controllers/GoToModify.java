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

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import dao.EnrollsDAO;
import utils.ConnectionHandler;
import utils.ParamsChecker;

/**
 * Servlet implementation class GoToModify
 */
@WebServlet("/GoToModify")
public class GoToModify extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;

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

		Integer course_id, id_stud, exam_date_id, sort;
		String coursename, mask, date, name, surname, email, coursedeg, mark, status;

		try {
			sort = Integer.parseInt(request.getParameter("sort"));
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
		} catch (NumberFormatException | NullPointerException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
			return;
		}
		
		// check params
		if(!ParamsChecker.checkParam(coursename) || !ParamsChecker.checkParam(mask) || !ParamsChecker.checkParam(date) ||
				!ParamsChecker.checkParam(name) || !ParamsChecker.checkParam(surname) || !ParamsChecker.checkParam(email) ||
				!ParamsChecker.checkParam(coursedeg) || !ParamsChecker.checkParam(status)) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
			return;
		}
		
		String path = "/WEB-INF/Modify.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

		EnrollsDAO enrollsDAO = new EnrollsDAO(connection);
		boolean isModifiable = false;
		try {
			isModifiable = enrollsDAO.checkModifiableCondition(exam_date_id, id_stud);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to modify mark");
			return;
		}

		

		if (!isModifiable) {
			path = "/WEB-INF/Warning.html";
			ctx.setVariable("error", "Impossible To Modify Score");
			ctx.setVariable("description", "You Can Only Modify Score in NOT_INSERTED or INSERTED Status!");
			templateEngine.process(path, ctx, response.getWriter());
			return;
		}
		
		ctx.setVariable("sort", sort);
		ctx.setVariable("course_id", course_id);
		ctx.setVariable("id_stud", id_stud);
		ctx.setVariable("exam_date_id", exam_date_id);
		ctx.setVariable("mask", mask);
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}