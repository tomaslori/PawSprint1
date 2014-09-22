package model;

import java.util.Date;

public class User extends AbstractEntity {

	public static final int MIN_PASSWORD_LENGTH = 8;
	public static final int MAX_PASSWORD_LENGTH = 16;
	
	private String name;
	private String surname;
	private String email;
	private String password;
	private String secretQuestion;
	private String secretAnswer;
	private Date birthDate;
	private boolean vip;
	
	public User(String name, String surname, String email, String password,
			String secretQuestion, String secretAnswer, Date birthDate, boolean vip)
			throws IllegalArgumentException {

		setId(-1);
		setName(name);
		setSurname(surname);
		setEmail(email);
		setPassword(password);
		setSecretQuestion(secretQuestion);
		setSecretAnswer(secretAnswer);
		setBirthDate(birthDate);
		setVip(vip);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) throws IllegalArgumentException {
		if(name == null)
			throw new IllegalArgumentException();
		else
			this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) throws IllegalArgumentException {
		if(surname == null)
			throw new IllegalArgumentException();
		else
			this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) throws IllegalArgumentException {
		if (email == null || !email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
			throw new IllegalArgumentException();
		else
			this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws IllegalArgumentException {
		if (password == null || password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH)
			throw new IllegalArgumentException();
		else
			this.password = password;
	}

	public String getSecretQuestion() {
		return secretQuestion;
	}

	public void setSecretQuestion(String secretQuestion) throws IllegalArgumentException {
		if(secretQuestion == null)
			throw new IllegalArgumentException();
		else
			this.secretQuestion = secretQuestion;
	}

	public String getSecretAnswer() {
		return secretAnswer;
	}

	public void setSecretAnswer(String secretAnswer) throws IllegalArgumentException {
		if(secretAnswer == null)
			throw new IllegalArgumentException();
		else
			this.secretAnswer = secretAnswer;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) throws IllegalArgumentException {
		if(birthDate == null)
			throw new IllegalArgumentException();
		else
			this.birthDate = birthDate;
	}
	
	public boolean getVip() {
		return vip;
	}
	
	public void setVip(boolean vip) {
		this.vip = vip;
	}
	
	
}
