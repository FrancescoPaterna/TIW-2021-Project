package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import beans.Enroll;
import beans.User;
import dao.EnrollsDAO;
import utils.ConnectionHandler;

/**
 * Servlet implementation class UpdateResultStud
 */
@WebServlet("/UpdateResultStud")
@MultipartConfig
public class UpdateResultStud extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
       
	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		User user = (User) session.getAttribute("user");
		
		Integer IDExamDate = Integer.parseInt(request.getParameter("IDExamDate"));
		
		EnrollsDAO enrollsDAO = new EnrollsDAO(connection);

		try {
			enrollsDAO.RefuseScore(IDExamDate, user.getId());
			
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Not possible to refuse score");
			return;
		}
		
		response.sendRedirect(session.getServletContext() + "/GetResultDetails");
		
	}

}
