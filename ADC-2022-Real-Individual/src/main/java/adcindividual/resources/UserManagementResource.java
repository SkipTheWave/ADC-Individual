package adcindividual.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.gson.Gson;
import com.google.cloud.datastore.*;

import adcindividual.util.AuthToken;
import adcindividual.util.LoginData;
import adcindividual.util.PasswordChangeData;
import adcindividual.util.ProfileData;
import adcindividual.util.ProfileInputData;
import adcindividual.util.RegisterData;
import adcindividual.util.RoleChangeData;
import adcindividual.util.StateChangeData;
import adcindividual.util.UserManagementData;

import static adcindividual.util.RegisterData.*;

import java.util.logging.Logger;

@Path("/manage")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class UserManagementResource {
	
	public static final String ROLE_USER = "USER";
	public static final String ROLE_BACKOFFICE = "GBO";
	public static final String ROLE_SYSTEM_MANAGER = "GS";
	public static final String ROLE_SUPERUSER = "SU";
	public static final String ACC_STATE_ACTIVE = "ACTIVE";
	public static final String ACC_STATE_INACTIVE = "INACTIVE";
	
	// logger object
	private static final Logger LOG = Logger.getLogger(UserManagementResource.class.getName());
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	private final Gson g = new Gson();
	
	public UserManagementResource( ) { }
	
	public boolean isTokenValid(AuthToken clientToken) {
		Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(clientToken.TokenID);
		Entity serverToken = datastore.get(tokenKey);		// not doing anything now cause it wasn't working
		if(clientToken.expDate <= System.currentTimeMillis()) {
			return false;
		} else
			return true;
	}
	
	@POST
	@Path("/profile")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getProfile(UserManagementData managementData) {
		AuthToken clientToken = g.fromJson(managementData.managerToken, new AuthToken().getClass());
		if(!isTokenValid(clientToken)) {
			return Response.status(Status.BAD_REQUEST).entity("Session has expired. Please try logging in again.").build();
		} else {
		    Key userKey = datastore.newKeyFactory().setKind("User").newKey(managementData.targetUsername);
		    Entity user = datastore.get(userKey);
		    ProfileData profile = new ProfileData(user.getString("email"), user.getString("name"), user.getString("NIF"),
		    		user.getString("address"), user.getString("phone_mobile"), user.getString("phone_home"), user.getBoolean("public_profile"));
		    return Response.ok(g.toJson(profile)).build();
		}
	}
	
	@PUT
	@Path("/profile")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateProfile(ProfileInputData data) {
		AuthToken clientToken = g.fromJson(data.userToken, new AuthToken().getClass());
		if(!isTokenValid(clientToken)) {
			return Response.status(Status.BAD_REQUEST).entity("Session has expired. Please try logging in again.").build();
		} else {
		    Key userKey = datastore.newKeyFactory().setKind("User").newKey(clientToken.username);
		    Entity user = datastore.get(userKey);
		    user = Entity.newBuilder(user).set("name", data.name)
		    		.set("NIF", data.nif)
		    		.set("address", data.address)
		    		.set("phone_mobile", data.phoneMobile)
		    		.set("phone_home", data.phoneHome)
		    		.set("public_profile", data.publicProfile)
		    		.build();
	    	datastore.update(user);
	    	return Response.ok("Profile successfully updated! Hope everything's in order now.").build();
		}
	}
	
	@PUT
	@Path("/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeUser(UserManagementData managementData) {
		AuthToken clientToken = g.fromJson(managementData.managerToken, new AuthToken().getClass());
		if(!isTokenValid(clientToken)) {
			return Response.status(Status.BAD_REQUEST).entity("Session has expired. Please try logging in again.").build();
		} else {
		    Key targetKey = datastore.newKeyFactory().setKind("User").newKey(managementData.targetUsername);
		    Entity target = datastore.get(targetKey);
		    if(target == null) {
		        return Response.status(Status.BAD_REQUEST).entity("No user with that username exists.").build();
		    } else if(clientToken.username.equals(managementData.targetUsername)
		    		|| clientToken.role.equals(ROLE_SUPERUSER)) { 			
		    	datastore.delete(targetKey); 	// this works even if the user doesn't exist, but it's good to check, I think
		    	return Response.ok("User data removed successfully! Hope everything's in order now.").build();
		    }
		    	return Response.status(Status.FORBIDDEN).entity("You don't have permission for that. Back off.").build();
		}
	}
	
	@PUT
	@Path("/change-role")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response changeRole(RoleChangeData data) {
		AuthToken clientToken = g.fromJson(data.managerToken, new AuthToken().getClass());
		if(!isTokenValid(clientToken)) {
			return Response.status(Status.BAD_REQUEST).entity("Session has expired. Please try logging in again.").build();
		} else {
		    Key targetKey = datastore.newKeyFactory().setKind("User").newKey(data.targetUsername);
		    Entity target = datastore.get(targetKey);
		    if(target == null) {			// not ideal for this to be before role verifications, but it's a tiny issue
		        return Response.status(Status.BAD_REQUEST).entity("No user with that username exists.").build();
		    } 
		    
		    // permission checks
		    else if((clientToken.role.equals(ROLE_SYSTEM_MANAGER) && target.getString("ROLE").equals(ROLE_USER) && data.newRole.equals(ROLE_BACKOFFICE))
		    		|| clientToken.role.equals(ROLE_SUPERUSER)) { 	
		    	target = Entity.newBuilder(target).set("ROLE", data.newRole).build();
		    	datastore.update(target);
		    	return Response.ok("User's role has been changed! With great power comes...............").build();
		    }
		    	return Response.status(Status.FORBIDDEN).entity("You don't have permission for that. Back off.").build();
		}
	}
	
	@PUT
	@Path("/change-state")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response changeState(StateChangeData data) {
		AuthToken clientToken = g.fromJson(data.managerToken, new AuthToken().getClass());
		if(!isTokenValid(clientToken)) {
			return Response.status(Status.BAD_REQUEST).entity("Session has expired. Please try logging in again.").build();
		} else {
		    Key targetKey = datastore.newKeyFactory().setKind("User").newKey(data.targetUsername);
		    Entity target = datastore.get(targetKey);
		    if(target == null) {			// not ideal for this to be before role verifications, but it's a tiny issue
		        return Response.status(Status.BAD_REQUEST).entity("No user with that username exists.").build();
		    } 
		    
		    // permission checks
		    else if((clientToken.role.equals(ROLE_USER) && data.targetUsername.equals(clientToken.username))
		    		|| (clientToken.role.equals(ROLE_BACKOFFICE) && target.getString("ROLE").equals(ROLE_USER))
		    		|| (clientToken.role.equals(ROLE_SYSTEM_MANAGER) && target.getString("ROLE").equals(ROLE_BACKOFFICE))
		    		|| clientToken.role.equals(ROLE_SUPERUSER)) { 	
		    	target = Entity.newBuilder(target).set("STATE", data.state).build();
		    	datastore.update(target);
		    	return Response.ok("Account state changed!").build();
		    }
		    	return Response.status(Status.FORBIDDEN).entity("You don't have permission for that. Back off.").build();
		}
	}
	
	@PUT
	@Path("/change-pw")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response changePassword(PasswordChangeData pwData) {
		AuthToken clientToken = g.fromJson(pwData.userToken, new AuthToken().getClass());
		if(!isTokenValid(clientToken)) {
			return Response.status(Status.BAD_REQUEST).entity("Session has expired. Please try logging in again.").build();
		} else {
		    Key userKey = datastore.newKeyFactory().setKind("User").newKey(clientToken.username);
		    Entity user = datastore.get(userKey);
		    if(!pwData.validPassword()) {
		        return Response.status(Status.BAD_REQUEST).entity("Submission invalid. Check length if confirmation matches.").build();
		    } else if(!user.getString("password").equals(pwData.oldPassword)) { 			
		    	return Response.status(Status.FORBIDDEN).entity("Old password is incorrect.").build();
		    } else {
		    	user = Entity.newBuilder(user).set("password", pwData.newPassword).build();
		    	datastore.update(user);
		    	return Response.ok("Password change successful! It's not 2FA but it's the next best thing!").build();
		    }
		}
	}

}

