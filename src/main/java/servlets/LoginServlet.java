package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exceptions.InvalidCredentialsException;
import model.User;
import services.UserService;

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private UserService userService = UserService.getInstance();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute("user");
		if (user == null) {
			getServletContext().getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
		} else {
			resp.sendRedirect("profile?email=" + user.getEmail());
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String email = req.getParameter("email");
		String password = req.getParameter("password");
		try {
			User user = userService.authenticate(email, password);
			req.getSession().setAttribute("user", user);
			req.getSession().setAttribute("isLogged", true);
			resp.sendRedirect("profile?user=" + user.getEmail());
		} catch (InvalidCredentialsException e) {
			req.setAttribute("email", email);
			req.setAttribute("error", "Invalid email or password.");
			req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
		}
	}
	
}
