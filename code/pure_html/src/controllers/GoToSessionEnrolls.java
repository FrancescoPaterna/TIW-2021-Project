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

import beans.Enroll;
import beans.User;
import dao.EnrollsDAO;
import dao.ExamDateDAO;

import utils.ConnectionHandler;

/**
 * Servlet implementation class GoToSessionEnrolls
 */
@WebServlet("/GoToSessionEnrolls")
public class GoToSessionEnrolls extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;

	public GoToSessionEnrolls() {
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

	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String loginpath = "/index.html";
		HttpSession session = request.getSession();

		Integer course_id = null;
		Integer exam_date_id = null;
		Integer secretsortcode = null;
		String sort;
		String coursename;
		String date;
		String mask;
		String maskget;
		String path;
		Boolean publish = true, record = true;
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

		coursename = StringEscapeUtils.escapeJava(request.getParameter("coursename"));
		date = StringEscapeUtils.escapeJava(request.getParameter("date"));
		sort = StringEscapeUtils.escapeJava(request.getParameter("sort"));
		mask = StringEscapeUtils.escapeJava(request.getParameter("mask"));
		course_id = Integer.parseInt(request.getParameter("course_id"));
		exam_date_id = Integer.parseInt(request.getParameter("exam_date_id"));

		
		ExamDateDAO examdateDAO = new ExamDateDAO(connection);
		User user = (User) session.getAttribute("user");

		
		try {
			if(!examdateDAO.CheckExamDateByProf(user.getId(), exam_date_id)) {
				 path ="/WEB-INF/Forbidden.html";
					ctx.setVariable("error", "UNAUTHORIZED ACCESS");
					ctx.setVariable("description", "Attempt to access a resource not owned by you!");
					templateEngine.process(path, ctx, response.getWriter());
					session.invalidate();
					return;
			}
		} catch (NumberFormatException | NullPointerException e) {
			// only for debugging e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
			return;
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "DB Error");
			e.printStackTrace();
		}

		EnrollsDAO enrollsDAO = new EnrollsDAO(connection);
		List<Enroll> enrolls = new ArrayList<>();
		path = "/WEB-INF/ExamEnrolls.html";

		
		if (sort.equals("0")) {
			maskget = "2222222";
			secretsortcode = 00;
			try {
				enrolls = enrollsDAO.FindEnrollsOrderedByIDAsc(exam_date_id);

			} catch (SQLException e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"Not possible to recover enrolls for this exam date");
				return;
			}
		}
		
		else if (sort.equals("1")) {
			if (mask.charAt(0) == '0' || mask.charAt(0) == '2') {
				maskget = '1' + mask.substring(1, 7);
				secretsortcode = 10;
				try {
					enrolls = enrollsDAO.FindEnrollsOrderedByIDAsc(exam_date_id);

				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			} else {
				maskget = '0' + mask.substring(1, 7);
				secretsortcode = 11;
				try {
					enrolls = enrollsDAO.FindEnrollsOrderedByIDDesc(exam_date_id);

				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			}

		} else if (sort.equals("2")) {
			if (mask.charAt(1) == '0' || mask.charAt(1) == '2') {
				maskget = mask.substring(0,1) + '1' + mask.substring(2, 7);
				secretsortcode = 20;
				try {
					enrolls = enrollsDAO.FindEnrollsOrderedByNameAsc(exam_date_id);

				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			} else {
				maskget = mask.substring(0,1) + '0' + mask.substring(2, 7);
				secretsortcode = 21;
				try {
					enrolls = enrollsDAO.FindEnrollsOrderedByNameDesc(exam_date_id);

				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			}
		} else if (sort.equals("3")) {
			if (mask.charAt(2) == '0' || mask.charAt(2) == '2') {
				maskget = mask.substring(0, 2) + '1' + mask.substring(3, 7);
				secretsortcode = 30;
				try {
					enrolls = enrollsDAO.FindEnrollsOrderedBySurnameAsc(exam_date_id);

				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			} else {
				maskget = mask.substring(0, 2) + '0' + mask.substring(3, 7);
				secretsortcode = 31;
				try {
					enrolls = enrollsDAO.FindEnrollsOrderedBySurnameDesc(exam_date_id);

				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			}
		} else if (sort.equals("4")) {
			if (mask.charAt(3) == '0' || mask.charAt(3) == '2') {
				maskget = mask.substring(0, 3) + '1' + mask.substring(4, 7);
				secretsortcode = 40;

				try {
					enrolls = enrollsDAO.FindEnrollsOrderedByEmailAsc(exam_date_id);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			} else {
				maskget = mask.substring(0, 3) + '0' + mask.substring(4, 7);
				secretsortcode = 41;

				try {
					enrolls = enrollsDAO.FindEnrollsOrderedByEmailDesc(exam_date_id);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			}
		} else if (sort.equals("5")) {
			if (mask.charAt(4) == '0' || mask.charAt(4) == '2') {
				maskget = mask.substring(0, 4) + '1' + mask.substring(5, 7);
				secretsortcode = 50;

				try {
					enrolls = enrollsDAO.FindEnrollsOrderedByCoursedegAsc(exam_date_id);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			} else {
				maskget = mask.substring(0, 4) + '0' + mask.substring(5, 7);
				secretsortcode = 51;
				try {
					enrolls = enrollsDAO.FindEnrollsOrderedByCoursedegDesc(exam_date_id);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			}
		} else if (sort.equals("6")) {
			if (mask.charAt(5) == '0' || mask.charAt(5) == '2') {
				maskget = mask.substring(0, 5) + '1' + mask.substring(6, 7);
				secretsortcode = 60;

				try {
					enrolls = enrollsDAO.FindEnrollsOrderedByMarkAsc(exam_date_id);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			} else {
				maskget = mask.substring(0, 5) + '0' + mask.substring(6, 7);
				secretsortcode = 61;
				try {
					enrolls = enrollsDAO.FindEnrollsOrderedByMarkDesc(exam_date_id);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			}
		}

		else if (sort.equals("7")) {
			if (mask.charAt(6) == '0' || mask.charAt(6) == '2') {
				maskget = mask.substring(0, 6) + '1';
				secretsortcode = 70;

				try {
					enrolls = enrollsDAO.FindEnrollsOrderedByStatusAsc(exam_date_id);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			} else {
				maskget = mask.substring(0, 6) + '0';
				secretsortcode = 71;
				try {
					enrolls = enrollsDAO.FindEnrollsOrderedByStatusDesc(exam_date_id);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			}
		}

		else {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Attempted SQL Injection");
			return;

		}
		
	
			try {
				publish = enrollsDAO.assertion_published(exam_date_id);
				record = enrollsDAO.assertion_record(exam_date_id);
			} catch (SQLException e) {
				e.printStackTrace();
			}
	
		

		
		ctx.setVariable("publish", publish);
		ctx.setVariable("record", record);
		ctx.setVariable("course_id", course_id);
		ctx.setVariable("exam_date_id", exam_date_id);
		ctx.setVariable("enrolls", enrolls);
		ctx.setVariable("coursename", coursename);
		ctx.setVariable("date", date);
		ctx.setVariable("mask", maskget);
		ctx.setVariable("secret_code", secretsortcode);
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
