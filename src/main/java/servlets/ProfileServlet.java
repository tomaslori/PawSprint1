package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import services.UserService;

public class ProfileServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private UserService userService = UserService.getInstance();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String email = req.getParameter("email");
		HttpSession session = req.getSession(false);
		User userSession = (User) session.getAttribute("user");


		if (email == null) {
			if (userSession != null)
				resp.sendRedirect("profile?email=" + userSession.getEmail());
			else
				req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
		} else {
			User user = userService.getUser(email);
			if (user != null) {
				if (userSession != null) {
					if (user.getEmail().equals(userSession.getEmail())) {
						req.setAttribute("isOwner", true);
					}
				}
				req.setAttribute("email", user);
			} else {
				resp.sendRedirect("profile");
				return;
			}
			req.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO: check... (nothing yet)
	}

}
