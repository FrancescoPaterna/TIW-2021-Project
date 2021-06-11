package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import beans.Enroll;
import beans.RecordedEnrolls;
import dao.EnrollsDAO;
import dao.RecordDAO;
import utils.ConnectionHandler;

/**
 * Servlet implementation class GetRecordedEnrolls
 */
@WebServlet("/GetRecordedEnrolls")
@MultipartConfig
public class GetRecordedEnrolls extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
       
	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Integer recordID;
		try {
			recordID = Integer.parseInt(request.getParameter("recordID"));
			// If the argument of Integer.parseInt is null or is a string of length zero, a
			// NumberFormatException is thrown
			// @see https://docs.oracle.com/javase/7/docs/api/java/lang/Integer.html#parseInt(java.lang.String)
		} catch (NumberFormatException | NullPointerException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Incorrect or missing param values");
			return;
		}
		
		EnrollsDAO enrollsDAO = new EnrollsDAO(connection);
		RecordDAO recordDAO = new RecordDAO(connection);
		
		List<Enroll> recorded;
		Timestamp timestamp;
		String date, time;
		
		// recover enrolls associated with the recordID specified,
		// then recover current timestamp from the database
		// finally convert the timestamp to the desired format
		try {
			recorded = enrollsDAO.findRecordedStudents(recordID);
		
			timestamp = recordDAO.getCurrentTimestamp(recordID);
			date = new SimpleDateFormat("dd-MM-yyyy").format(timestamp);
			time = new SimpleDateFormat("HH:mm").format(timestamp);

		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Cannot connect to the database!");
			return;
		}
		
		// send all data to the client as JSON
		RecordedEnrolls recordedEnrolls = new RecordedEnrolls(recorded, date, time, recordID);
		String serialized_enrolls = new Gson().toJson(recordedEnrolls);		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(serialized_enrolls);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
