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
			

			
			if (secretsortcode == 00) {
				if (mask.charAt(0) == '0') {
					recovered_mask = '0' + mask.substring(1, 7);
					sort = 1;
					String sorturl = "?mask=" + recovered_mask + "&exam_date_id=" + exam_date_id + "&date=" + date + "&coursename=" + coursename + "&course_id=" + course_id + "&sort=" + String.valueOf(sort);
					target = "/GoToSessionEnrolls";
					path = getServletContext().getContextPath();
					response.sendRedirect(path + target + sorturl);
				}
			}
				else if (secretsortcode == 11) {
				if (mask.charAt(0) == '0') {
					recovered_mask = '1' + mask.substring(1, 7);
					sort = 1;
					String sorturl = "?mask=" + recovered_mask + "&exam_date_id=" + exam_date_id + "&date=" + date + "&coursename=" + coursename + "&course_id=" + course_id + "&sort=" + String.valueOf(sort);
					target = "/GoToSessionEnrolls";
					path = getServletContext().getContextPath();
					response.sendRedirect(path + target + sorturl);
				}
			} else if (secretsortcode == 10) {
				if (mask.charAt(0) == '1') {
					recovered_mask = '0' + mask.substring(1, 7);
					sort = 1;
					String sorturl = "?mask=" + recovered_mask + "&exam_date_id=" + exam_date_id + "&date=" + date + "&coursename=" + coursename + "&course_id=" + course_id + "&sort=" + String.valueOf(sort);
					target = "/GoToSessionEnrolls";
					path = getServletContext().getContextPath();
					response.sendRedirect(path + target + sorturl);
				}
			}

			else if (secretsortcode == 21) {
				if (mask.charAt(1) == '0') {
					recovered_mask = mask.substring(0,1) + '1' + mask.substring(2, 7);
					sort = 2;
					String sorturl = "?mask=" + recovered_mask + "&exam_date_id=" + exam_date_id + "&date=" + date + "&coursename=" + coursename + "&course_id=" + course_id + "&sort=" + String.valueOf(sort);
					target = "/GoToSessionEnrolls";
					path = getServletContext().getContextPath();
					response.sendRedirect(path + target + sorturl);
				}
			}

			else if (secretsortcode == 20) {
				if (mask.charAt(1) == '1') {
					recovered_mask = mask.substring(0,1) + '0' + mask.substring(2, 7);
					sort = 2;
					String sorturl = "?mask=" + recovered_mask + "&exam_date_id=" + exam_date_id + "&date=" + date + "&coursename=" + coursename + "&course_id=" + course_id + "&sort=" + String.valueOf(sort);
					target = "/GoToSessionEnrolls";
					path = getServletContext().getContextPath();
					response.sendRedirect(path + target + sorturl);

				}
			}

			else if (secretsortcode == 31) {
				if (mask.charAt(2) == '0') {
					recovered_mask = mask.substring(0, 2) + '1' + mask.substring(3, 7);
					sort = 3;
					String sorturl = "?mask=" + recovered_mask + "&exam_date_id=" + exam_date_id + "&date=" + date + "&coursename=" + coursename + "&course_id=" + course_id + "&sort=" + String.valueOf(sort);
					target = "/GoToSessionEnrolls";
					path = getServletContext().getContextPath();
					response.sendRedirect(path + target + sorturl);
				}
			}

			else if (secretsortcode == 30) {
				if (mask.charAt(2) == '1') {
					recovered_mask = mask.substring(0, 2) + '0' + mask.substring(3, 7);
					sort = 3;
					String sorturl = "?mask=" + recovered_mask + "&exam_date_id=" + exam_date_id + "&date=" + date + "&coursename=" + coursename + "&course_id=" + course_id + "&sort=" + String.valueOf(sort);
					target = "/GoToSessionEnrolls";
					path = getServletContext().getContextPath();
					response.sendRedirect(path + target + sorturl);
				}
			}
			else if (secretsortcode == 41) {
				if (mask.charAt(3) == '0') {
					recovered_mask = mask.substring(0, 3) + '1' + mask.substring(4, 7);
					sort = 4;
					String sorturl = "?mask=" + recovered_mask + "&exam_date_id=" + exam_date_id + "&date=" + date + "&coursename=" + coursename + "&course_id=" + course_id + "&sort=" + String.valueOf(sort);
					target = "/GoToSessionEnrolls";
					path = getServletContext().getContextPath();
					response.sendRedirect(path + target + sorturl);
				}
			}

			else if (secretsortcode == 40) {
				if (mask.charAt(3) == '1') {
					recovered_mask = mask.substring(0, 3) + '0' + mask.substring(4, 7);
					sort = 4;
					String sorturl = "?mask=" + recovered_mask + "&exam_date_id=" + exam_date_id + "&date=" + date + "&coursename=" + coursename + "&course_id=" + course_id + "&sort=" + String.valueOf(sort);
					target = "/GoToSessionEnrolls";
					path = getServletContext().getContextPath();
					response.sendRedirect(path + target + sorturl);
				}
			}
			else if (secretsortcode == 51) {
				if (mask.charAt(4) == '0') {
					recovered_mask = mask.substring(0, 4) + '1' + mask.substring(5, 7);
					sort = 5;
					String sorturl = "?mask=" + recovered_mask + "&exam_date_id=" + exam_date_id + "&date=" + date + "&coursename=" + coursename + "&course_id=" + course_id + "&sort=" + String.valueOf(sort);
					target = "/GoToSessionEnrolls";
					path = getServletContext().getContextPath();
					response.sendRedirect(path + target + sorturl);
				}
			}

			else if (secretsortcode == 50) {
				if (mask.charAt(4) == '1') {
					recovered_mask = mask.substring(0, 4) + '0' + mask.substring(5, 7);
					sort = 5;
					String sorturl = "?mask=" + recovered_mask + "&exam_date_id=" + exam_date_id + "&date=" + date + "&coursename=" + coursename + "&course_id=" + course_id + "&sort=" + String.valueOf(sort);
					target = "/GoToSessionEnrolls";
					path = getServletContext().getContextPath();
					response.sendRedirect(path + target + sorturl);
				}
			}
			else if (secretsortcode == 61) {
				if (mask.charAt(5) == '0') {
					recovered_mask = mask.substring(0, 5) + '1' + mask.substring(6, 7);
					sort = 6;
					String sorturl = "?mask=" + recovered_mask + "&exam_date_id=" + exam_date_id + "&date=" + date + "&coursename=" + coursename + "&course_id=" + course_id + "&sort=" + String.valueOf(sort);
					target = "/GoToSessionEnrolls";
					path = getServletContext().getContextPath();
					response.sendRedirect(path + target + sorturl);
				}
			}

			else if (secretsortcode == 60) {
				if (mask.charAt(5) == '1') {
					recovered_mask = mask.substring(0, 5) + '0' + mask.substring(6, 7);
					sort = 6;
					String sorturl = "?mask=" + recovered_mask + "&exam_date_id=" + exam_date_id + "&date=" + date + "&coursename=" + coursename + "&course_id=" + course_id + "&sort=" + String.valueOf(sort);
					target = "/GoToSessionEnrolls";
					path = getServletContext().getContextPath();
					response.sendRedirect(path + target + sorturl);
				}
			}
			else if (secretsortcode == 71) {
				if (mask.charAt(6) == '0') {
					recovered_mask = mask.substring(0, 6) + '1';
					sort = 7;
					String sorturl = "?mask=" + recovered_mask + "&exam_date_id=" + exam_date_id + "&date=" + date + "&coursename=" + coursename + "&course_id=" + course_id + "&sort=" + String.valueOf(sort);
					target = "/GoToSessionEnrolls";
					path = getServletContext().getContextPath();
					response.sendRedirect(path + target + sorturl);
				}
			}

			else if (secretsortcode == 70) {
				if (mask.charAt(6) == '1') {
					recovered_mask = mask.substring(0, 6) + '0';
					sort = 7;
					String sorturl = "?mask=" + recovered_mask + "&exam_date_id=" + exam_date_id + "&date=" + date + "&coursename=" + coursename + "&course_id=" + course_id + "&sort=" + String.valueOf(sort);
					target = "/GoToSessionEnrolls";
					path = getServletContext().getContextPath();
					response.sendRedirect(path + target + sorturl);
				}
			}
			

		} else {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not Allowed User");

		}
	}

}
