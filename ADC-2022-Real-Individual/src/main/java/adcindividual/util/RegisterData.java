package adcindividual.util;

public class RegisterData {
	
	public String username;
	public String password;
	public String passwordConfirmation;
	public String email;
	public String name;
	public String address;
	public String nif;
	public String phoneMobile, phoneHome;
	public boolean profilePublic;
	
	public RegisterData() { }

	public RegisterData(String username, String password, String passwordConfirmation, String email, String name,
			String address, String nif, String phoneMobile, String phoneHome, boolean profilePublic) {
		this.username = username;
		this.password = password;
		this.passwordConfirmation = passwordConfirmation;
		this.email = email;
		this.name = name;
		this.address = address;
		this.nif = nif;
		this.phoneMobile = phoneMobile;
		this.phoneHome = phoneHome;
		this.profilePublic = profilePublic;
	}



	public boolean validRegistration() {
		if(validInfoSize(username) && validInfoSize(email) && validInfoSize(name) && validPassword())
			return true;
		else
			return false;		
	}
	
	// used for user
	private boolean validInfoSize(String data) {
		return (data.length() > 1 && data.length() < 50);
	}
	
	private boolean validEmail(String email) {
		return true; // TODO regex
	}
	
	private boolean validPassword() {
		return (password.equals(passwordConfirmation) && password.length() >= 8);
	}

}
