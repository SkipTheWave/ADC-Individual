package adcindividual.util;

import java.util.UUID;

public class AuthToken {

    public String username, TokenID, role;
    public long begDate, expDate;
    // 30 minutes
    private static final long EXP_DATE =1000*60*30*5;

    public AuthToken() {

    }

    public AuthToken(String username, String role) {
        
	    this.username = username;
	    this.role = role;
	    TokenID = UUID.randomUUID().toString();
	    this.begDate = System.currentTimeMillis();
	    this.expDate = begDate + EXP_DATE;

    }

}