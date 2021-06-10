package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class checker
 */

public class LoginChecker implements Filter {
	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String loginpath = req.getServletContext().getContextPath() + "/index.html";

		
		// if the user is not logged in, redirect him to the login page
		HttpSession s = req.getSession();
		if (s.isNew() || s.getAttribute("user") == null) {
			s.invalidate();
			res.sendRedirect(loginpath);
			return;
		}
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}


	

}
