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

import beans.User;
import dao.EnrollsDAO;
import dao.ExamDateDAO;
import utils.ConnectionHandler;
import utils.Rebuilder;

/**
 * Servlet implementation class UpdateStatus
 */
@WebServlet("/UpdateStatus")
public class UpdateStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateStatus() {
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User user = null;
		HttpSession session = request.getSession();
		
		String path;
		user = (User) request.getSession().getAttribute("user");
		String target;

		// reset mask value

		if (user.getRole().equals("professor")) {

			Integer exam_date_id;
			Integer sort = 1;
			Integer secretsortcode, course_id;
			String date = StringEscapeUtils.escapeJava(request.getParameter("date"));
			String mask;
			String recovered_mask;
			String coursename;
			coursename = StringEscapeUtils.escapeJava(request.getParameter("coursename"));
			date = StringEscapeUtils.escapeJava(request.getParameter("date"));
			mask = StringEscapeUtils.escapeJava(request.getParameter("mask"));
			exam_date_id = Integer.parseInt(request.getParameter("exam_date_id"));
			secretsortcode = Integer.parseInt(request.getParameter("secret_code"));
			course_id = Integer.parseInt(request.getParameter("course_id"));

			exam_date_id = Integer.parseInt(request.getParameter("exam_date_id"));
			EnrollsDAO enrollsDAO = new EnrollsDAO(connection);
			ExamDateDAO examdatedao = new ExamDateDAO(connection);
			
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			
			

			
			try {
				if(!examdatedao.CheckExamDateByProf(user.getId() ,exam_date_id)) {
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
			

				try {
					if(enrollsDAO.assertion_published(exam_date_id)) {
						try {
							enrollsDAO.PublishScore(exam_date_id);
						} catch (SQLException e) {
							response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot Publish Any Score");
							return;
						}
					}
					else {
						 path ="/WEB-INF/Warning.html";
							ctx.setVariable("error", "Impossible To Record Score");
							ctx.setVariable("description", "Cannot Find Any Score in Published State!");
							templateEngine.process(path, ctx, response.getWriter());
							return;
					}
				} catch (SQLException | IOException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot Publish Any Score");
					e.printStackTrace();
				}
			

			recovered_mask = Rebuilder.resetMask(mask, secretsortcode);
			sort = Rebuilder.resetSecretSortCode(secretsortcode);	
			String sorturl = "?mask=" + recovered_mask + "&exam_date_id=" + exam_date_id + "&date=" + date + "&coursename=" + coursename + "&course_id=" + course_id + "&sort=" + String.valueOf(sort);
			target = "/GoToSessionEnrolls";
			path = getServletContext().getContextPath();
			response.sendRedirect(path + target + sorturl);

		} else {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not Allowed User");

		}
	}

}
