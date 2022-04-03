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
import adcindividual.util.ProfileData;
import adcindividual.util.RegisterData;

import java.util.logging.Logger;

@Path("/register")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class RegisterResource {
	
	// logger object
	private static final Logger LOG = Logger.getLogger(RegisterResource.class.getName());
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	private final Gson g = new Gson();
	
	public RegisterResource( ) { }
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerUser(RegisterData data) {
		
		if(data.validRegistration()) {
	
		    Key k = datastore.newKeyFactory().setKind("User").newKey(data.username);
		    if(datastore.get(k) == null)    {
		        Entity newUser = Entity.newBuilder(k)
		            .set("password",data.password)		// DISCLAIMER: I'm aware you should normally never put this in the DB without encryption
		            .set("email", data.email).set("name", data.name)
		            .set("address", data.address).set("NIF", data.nif)
		            .set("phone_mobile", data.phoneMobile).set("phone_home", data.phoneHome)
		            .set("public_profile", data.profilePublic)
		            .set("creation_time",System.currentTimeMillis())
		            .set("ROLE", UserManagementResource.ROLE_USER)
		            .set("STATE", UserManagementResource.ACC_STATE_INACTIVE)
		            .build();
	
		    datastore.put(newUser);
		    return Response.ok("User registered successfully! Welcome to the SYSTEM!").build(); 
		    } else 
		        return Response.status(Status.BAD_REQUEST).entity("A user with that username already exists.").build();
	
		} else 
		    return Response.status(Status.BAD_REQUEST).entity("The provided data is invalid... try re-checking passwords, or something.").build();
	}
}
