package filters;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class AuthenticationFilter implements Filter {

	public Logger log = Logger.getLogger(this.getClass().getName());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Do nothing
	}

	@Override
	public void destroy() {
		// Do nothing
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;

		if (req.getSession().getAttribute("user") == null)
			req.setAttribute("isLogged", false);
		else {
			req.setAttribute("userSession",	req.getSession().getAttribute("user"));
			req.setAttribute("isLogged", true);
		}
		chain.doFilter(request, response);
	}
}
