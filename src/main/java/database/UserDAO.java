package database;

import model.User;

public interface UserDAO {
	
	public User retrieve(String email);
	public void save(User user);

}
