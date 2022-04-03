package adcindividual.util;

public class RoleChangeData {
	
	public String targetUsername, newRole, managerToken;
	
	public RoleChangeData() { }
	
	public RoleChangeData(String targetUsername, String newRole, String managerToken) {
		this.targetUsername = targetUsername;
		this.newRole = newRole;
		this.managerToken = managerToken;
	}

}
