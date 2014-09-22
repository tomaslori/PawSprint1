package servlets;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exceptions.ExistingUserException;
import model.User;
import services.UserService;

public class RegistrationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private UserService userService = UserService.getInstance();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/jsp/registration.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String name = req.getParameter("name");
		String surname = req.getParameter("surname");
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		String passConfirm = req.getParameter("passConfirm");
		String secretQuestion = req.getParameter("secretQuestion");
		String secretAnswer = req.getParameter("secretAnswer");
		String birthDateString = req.getParameter("birthDate");
		
		try {
			if(birthDateString == null)
				throw new IllegalArgumentException();
				
			Date birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(birthDateString);
			
			User user = new User(name, surname, email, password, secretQuestion, secretAnswer, birthDate, false);
			if(!password.equals(passConfirm)) {
				fillRequest(name, surname, email, secretQuestion, secretAnswer, birthDateString, req);
				req.getRequestDispatcher("/WEB-INF/jsp/registration.jsp").forward(req, resp);
			}
				
			try { userService.register(user); }
			catch (ExistingUserException e) {
				throw new IllegalArgumentException("Email already in use.");
			}
			req.getSession().setAttribute("user", user);
			resp.sendRedirect("profile?email=" + user.getEmail());
		
		} catch (IllegalArgumentException|ParseException e) {
			fillRequest(name, surname, email, secretQuestion, secretAnswer, birthDateString, req);
			req.setAttribute("error", "One or more of the fields are invalid.");
			req.getRequestDispatcher("/WEB-INF/jsp/registration.jsp").forward(req, resp);
		}
		
	}

	private void fillRequest(String name, String surname, String email,
			String secretQuestion, String secretAnswer, String birthDate,
			HttpServletRequest req) {
		req.setAttribute("name", name);
		req.setAttribute("surname", surname);
		req.setAttribute("email", email);
		req.setAttribute("secretQuestion", secretQuestion);
		req.setAttribute("secretAnswer", secretAnswer);
		req.setAttribute("birthDate", birthDate);
	}
	
}
