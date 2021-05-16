package controllers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException;
//import java.io.PrintWriter;
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

import beans.User;
import dao.UserDAO;
import utils.ConnectionHandler;
import utils.HexString;

@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;

	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path;

		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		path = "/index.html";
		templateEngine.process(path, ctx, response.getWriter());

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// obtain and escape params
		String id = null;
		String pwd = null;
		String secure_pwd = null;
		try {
			id = StringEscapeUtils.escapeJava(request.getParameter("id"));
			pwd = StringEscapeUtils.escapeJava(request.getParameter("pwd"));

		} catch (Exception e) {

			// for debugging only
			e.printStackTrace();

			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Missing credential value aka BRO NON HAI SCRITTO NIENTE!");
			return;
		}
		

		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-1");
			byte[] hash = digest.digest(pwd.getBytes(StandardCharsets.UTF_8));
			secure_pwd = HexString.toHexString(hash);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// query db to authenticate for user
		UserDAO userDao = new UserDAO(connection);
		User user = null;
		try {
			user = userDao.checkCredentials(id, secure_pwd);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Not Possible to check credentials aka BRO STO DATABASE SI FA I CAZZI SUOI");
			return;
		}

		// If the user exists, add info to the session and go to home page, otherwise
		// show login page with error message

		String path;
		if (user == null) {
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsg", "Incorrect username or password");
			path = "/index.html";
			templateEngine.process(path, ctx, response.getWriter());
		} else {
			request.getSession().setAttribute("user", user);
			String target;
			if (user.getRole().equals("student")) {
				target = "/GoToHomePageStud";
			} else if (user.getRole().equals("professor")) {
				target = "/GoToHomePagePro";
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"Undefined User, Please Contact Registrar's office");
				return;
			}

			path = getServletContext().getContextPath();
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
