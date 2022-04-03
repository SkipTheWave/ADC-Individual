package adcindividual.util;

public class ProfileInputData {
	
	public String name, nif, address, phoneHome, phoneMobile, userToken;
	public boolean publicProfile;
	
	public ProfileInputData() { }

	public ProfileInputData(String name, String nif, String address, String phoneMobile,
			String phoneHome, boolean publicProfile, String userToken) {
		this.name = name;
		this.nif = nif;
		this.address = address;
		this.phoneHome = phoneHome;
		this.phoneMobile = phoneMobile;
		this.publicProfile = publicProfile;
		this.userToken = userToken;
	}
	

}
