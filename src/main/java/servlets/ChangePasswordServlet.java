package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exceptions.UserNotFoundException;
import model.User;
import services.UserService;

public class ChangePasswordServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		private UserService userService = UserService.getInstance();;

		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			String email = req.getParameter("userToRecover");
			User user = null;
			
			if(email == null) {
				req.setAttribute("error", "Email field not completed.");
				req.setAttribute("userSelected", false);
			} else {
				
				try {
					user = userService.getUser(email);
					req.setAttribute("success",	"Recovering password for " + user.getEmail());
					req.setAttribute("user", user);
					req.setAttribute("userSelected", true);
					
				} catch (UserNotFoundException e) {
					req.setAttribute("error", "User does not exist");
					req.setAttribute("userSelected", false);
				}
			}
			req.getRequestDispatcher("/WEB-INF/jsp/changePassword.jsp").forward(req, resp);
		}

		@Override
		protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			String secretAnswerSubmited = req.getParameter("secretAnswer");
			String newPassword = req.getParameter("password");
			String newPasswordConfirm = req.getParameter("confirm");
			String userToRecover = req.getParameter("userToRecover");
			User user = userService.getUser(userToRecover);
			req.setAttribute("userSelected", true);
			if (secretAnswerSubmited != null) {
				boolean matches = user.getSecretAnswer().equals(secretAnswerSubmited);
				boolean passwordMatches = false;
				if (newPassword != null && newPasswordConfirm != null) {
					passwordMatches = newPassword.equals(newPasswordConfirm);
				}
				if (!matches) {
					req.setAttribute("error", "Wrong secret Answer");
					req.setAttribute("success",	"Recovering password for " + user.getEmail());
					req.setAttribute("user", user);
				} else {
					if (passwordMatches) {
						try {
						user.setPassword(newPassword);
						req.setAttribute("passwordRecovered", true);
						req.setAttribute("success",	"Your password was changed successfully!");
						user.setPassword(newPassword);
						userService.update(user);
						} catch(IllegalArgumentException e) {
							req.setAttribute("error", "Invalid new password.");
							req.setAttribute("success", "Recovering password for " + user.getEmail());
							req.setAttribute("user", user);
						}
					} else {
						req.setAttribute("error", "Passwords dont match.");
						req.setAttribute("success", "Recovering password for " + user.getEmail());
						req.setAttribute("user", user);
					}
				}
			}
			req.getRequestDispatcher("/WEB-INF/jsp/changePassword.jsp").forward(req, resp);
		}
	}