package adcindividual.util;

public class StateChangeData {
	
	public String targetUsername, state, managerToken;
	
	public StateChangeData() { }
	
	public StateChangeData(String targetUsername, String state, String managerToken) {
		this.targetUsername = targetUsername;
		this.state = state;
		this.managerToken = managerToken;
	}

}
