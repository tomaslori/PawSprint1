package services;

import model.User;
import services.UserService;
import database.PostgreSQLUserDAO;
import database.UserDAO;
import exceptions.ExistingUserException;
import exceptions.InvalidCredentialsException;
import exceptions.UserNotFoundException;

public class UserService {

	private UserDAO userDao;

	private static UserService instance;

	private UserService() {
		userDao = PostgreSQLUserDAO.getInstance();
	}
	
	public static UserService getInstance() {
		if (instance == null)
			instance = new UserService();

		return instance;
	}
	
	public User getUser(String email) {
		return userDao.retrieve(email);
	}
	
	public void register(User user) {
		try {
			userDao.retrieve(user.getEmail());
			throw new ExistingUserException();
		} catch (UserNotFoundException e) {
			userDao.save(user);
		}
	}
	
	public void update(User user) {
		userDao.retrieve(user.getEmail());
		userDao.save(user);
	}
	
	public User authenticate(String email, String password) {
		
		try {
			User user = userDao.retrieve(email);
			if(user != null && password.equals(user.getPassword()))
				return user;
			else
				throw new InvalidCredentialsException();
		} catch (UserNotFoundException e) {
			throw new InvalidCredentialsException();
		}
	}
	
}
