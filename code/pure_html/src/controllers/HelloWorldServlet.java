package controllers;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class HelloWorldServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6771589930614684222L;

	public void doGet(HttpServletRequest request,HttpServletResponse response)
	throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		out.println("<HTML>");
		out.println("<HEAD><TITLE>Operativi</TITLE></HEAD>");
		out.println("<BODY>");
		out.println("Se mi leggete, allora sta andando tutto a gonfie vele");
		out.println("</BODY>");
		out.println("</HTML>");
		out.close();
	}
}
