package controllers;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import beans.User;
import dao.UserDAO;
import utils.ConnectionHandler;




@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	//private TemplateEngine templateEngine;


	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// obtain and escape params
		String id = null;
		String pwd = null;
		try {
			id = StringEscapeUtils.escapeJava(request.getParameter("id"));
			pwd = StringEscapeUtils.escapeJava(request.getParameter("pwd"));

		} catch (Exception e) {

			// for debugging only 
			e.printStackTrace();

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value aka BRO NON HAI SCRITTO NIENTE!");
			return;
		}

		// query db to authenticate for user
		UserDAO userDao = new UserDAO(connection);
		User user = null;
		try {
			user = userDao.checkCredentials(id, pwd);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not Possible to check credentials aka BRO STO DATABASE SI FA I CAZZI SUOI");
			return;
		}

		// If the user exists, add info to the session and go to home page, otherwise
		// show login page with error message

		String path;
		if (user == null) {
			path = getServletContext().getContextPath() + "/index.html";
		} else {
			request.getSession().setAttribute("user", user);
			String target = (user.getRole().equals("student")) ? "/GoToHomePageStud" : "/GoToHomePagePro";
			path = getServletContext().getContextPath();
			//response.sendRedirect(path + target);
			//response.setContentType("text/html");
			PrintWriter out = response.getWriter();
		    out.println("Benvenuto " + user.getName() + ' ' + user.getSurname() +  " stai per essere indirizzato in   --->" + path + target);
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
