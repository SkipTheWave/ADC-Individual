package adcindividual.util;

public class PasswordChangeData {
	
	public String newPassword;
	public String oldPassword;
	public String newPasswordConfirmation;
	public String userToken;
	
	public PasswordChangeData() { }

	public PasswordChangeData(String oldPassword, String newPassword, String newPasswordConfirmation, String userToken) {
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
		this.newPasswordConfirmation = newPasswordConfirmation;
		this.userToken = userToken;
	}
	
	public boolean validPassword() {		// could probably be done in client
		return (newPassword.equals(newPasswordConfirmation) && newPassword.length() >= 8);
	}

}