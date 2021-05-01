package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import dao.EnrollsDAO;

import utils.ConnectionHandler;

/**
 * Servlet implementation class GoToSessionEnrolls
 */
@WebServlet("/GoToSessionEnrolls")
public class GoToSessionEnrolls extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;
	boolean flag1, flag2, flag3, flag4, flag5, flag6, flag7;

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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// If the user is not logged in (not present in session) redirect to the login
		String loginpath = getServletContext().getContextPath() + "/index.html";
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("user") == null) {
			response.sendRedirect(loginpath);
			return;
		}

		Integer exam_date_id = null;
		String date = StringEscapeUtils.escapeJava(request.getParameter("date"));


		try {
			exam_date_id = Integer.parseInt(request.getParameter("exam_date_id"));
		} catch (NumberFormatException | NullPointerException e) {
			// only for debugging e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
			return;
		}

		EnrollsDAO enrollsDAO = new EnrollsDAO(connection);
		List<Enroll> enrolls = new ArrayList<>();

		try {
			enrolls = enrollsDAO.FindEnrolls(exam_date_id);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Not possible to recover enrolls for this exam date");
			return;
		}
		// Redirect to the HomePage and add courses to the parameters
		String path = "/WEB-INF/ExamEnrolls.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("exam_date_id", exam_date_id);
		ctx.setVariable("enrolls", enrolls);
		ctx.setVariable("coursename", request.getSession().getAttribute("coursename"));
		ctx.setVariable("date", date);

		/*try {
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("date"));
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			String today = formatter.format(date);
			ctx.setVariable("date", today);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/

		templateEngine.process(path, ctx, response.getWriter());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String loginpath = getServletContext().getContextPath() + "/index.html";
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("user") == null) {
			response.sendRedirect(loginpath);
			return;
		}

		Integer exam_date_id = null;
		String sort;
		String coursename;
		String date;

		coursename = StringEscapeUtils.escapeJava(request.getParameter("coursename"));
		date = StringEscapeUtils.escapeJava(request.getParameter("date"));
		sort = StringEscapeUtils.escapeJava(request.getParameter("sort"));

		try {
			exam_date_id = Integer.parseInt(request.getParameter("exam_date_id"));
		} catch (NumberFormatException | NullPointerException e) {
			// only for debugging e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
			return;
		}

		EnrollsDAO enrollsDAO = new EnrollsDAO(connection);
		List<Enroll> enrolls = new ArrayList<>();

		if (sort.equals("1")) {
			if (flag1) {
				flag1 = false;
				try {
					enrolls = enrollsDAO.FindEnrollsOrderedByIDDesc(exam_date_id);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			} else {
				flag1 = true;
				try {
					enrolls = enrollsDAO.FindEnrollsOrderedByIDAsc(exam_date_id);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			}

		} 
		else if (sort.equals("2")) {
			if (flag2) {
				flag2 = false;
				try {
					enrolls = enrollsDAO.FindEnrollsOrderedByNameDesc(exam_date_id);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			} else {
				flag2 = true;
				try {
					enrolls = enrollsDAO.FindEnrollsOrderedByNameAsc(exam_date_id);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			}
		}
		else if (sort.equals("3")) {
			if (flag3) {
				flag3 = false;
				try {
					enrolls = enrollsDAO.FindEnrollsOrderedBySurnameDesc(exam_date_id);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			} else {
				flag3 = true;
				try {
					enrolls = enrollsDAO.FindEnrollsOrderedBySurnameAsc(exam_date_id);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			}
		}
		else if (sort.equals("4")) {
			if (flag4) {
				flag4 = false;
				try {
					enrolls = enrollsDAO.FindEnrollsOrderedByEmailDesc(exam_date_id);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			} else {
				flag4 = true;
				try {
					enrolls = enrollsDAO.FindEnrollsOrderedByEmailAsc(exam_date_id);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			}
		}
		else if (sort.equals("5")) {
			if (flag5) {
				flag5 = false;
				try {
					enrolls = enrollsDAO.FindEnrollsOrderedByCoursedegDesc(exam_date_id);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			} else {
				flag5 = true;
				try {
					enrolls = enrollsDAO.FindEnrollsOrderedByCoursedegAsc(exam_date_id);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			}
		}
		else if (sort.equals("6")) {
			if (flag6) {
				flag6 = false;
				try {
					enrolls = enrollsDAO.FindEnrollsOrderedByMarkDesc(exam_date_id);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			} else {
				flag6 = true;
				try {
					enrolls = enrollsDAO.FindEnrollsOrderedByMarkAsc(exam_date_id);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			}
		}

		else if (sort.equals("7")) {
			if (flag7) {
				flag7 = false;
				try {
					enrolls = enrollsDAO.FindEnrollsOrderedByStatusDesc(exam_date_id);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Not possible to recover enrolls for this exam date");
					return;
				}
			} else {
				flag7 = true;
				try {
					enrolls = enrollsDAO.FindEnrollsOrderedByStatusAsc(exam_date_id);
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

		// Redirect to the HomePage and add courses to the parameters
		String path = "/WEB-INF/ExamEnrolls.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("exam_date_id", exam_date_id);
		ctx.setVariable("enrolls", enrolls);
		ctx.setVariable("coursename", coursename);
		ctx.setVariable("date", date);

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
