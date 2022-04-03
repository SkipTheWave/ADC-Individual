package adcindividual.util;

public class ProfileData {
	
	public String email, name, nif, address, phoneHome, phoneMobile;
	public boolean publicProfile;
	
	public ProfileData() { }

	public ProfileData(String email, String name, String nif, String address, String phoneMobile,
			String phoneHome, boolean publicProfile) {
		this.email = email;
		this.name = name;
		this.nif = nif;
		this.address = address;
		this.phoneHome = phoneHome;
		this.phoneMobile = phoneMobile;
		this.publicProfile = publicProfile;
	}
	

}
