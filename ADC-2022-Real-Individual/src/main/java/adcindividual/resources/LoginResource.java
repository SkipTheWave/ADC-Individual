package adcindividual.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.gson.Gson;

import adcindividual.util.AuthToken;
import adcindividual.util.LoginData;

import java.util.logging.Logger;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class LoginResource {
	
	// logger object
	private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());
	
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	private final Gson g = new Gson();
	
	public LoginResource( ) { }
	
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response doLogin(LoginData data) {
        LOG.fine("Login attempt by user: " + data.username);
        Key k = datastore.newKeyFactory().setKind("User").newKey(data.username);
        if (datastore.get(k) == null) {
            return Response.status(Status.FORBIDDEN).entity("No user with that username exists.").build();
        } else {
            Entity user = datastore.get(k);
            if (user.getString("STATE").equals("INACTIVE")) 
            	return Response.status(Status.FORBIDDEN).entity("This account is currently inactive. Please ask an administrator to activate it.").build();
            String role = user.getString("ROLE");
            if (data.password.equals(user.getString("password"))) {
	            AuthToken at = new AuthToken(data.username, role);
	            Key k2 = datastore.newKeyFactory().addAncestor(PathElement.of("User", data.username)).setKind("Token").newKey(at.TokenID);
	            Entity token = Entity.newBuilder(k2)
	            		.set("username", at.username)
	            		.set("user_role", role)
	            		.set("creation_time", Timestamp.now())
	            		.set("expiration_time", at.expDate).build();
	            datastore.put(token);
	            return Response.ok(g.toJson(at)).build();         
            }
            return Response.status(Status.FORBIDDEN).entity("Incorrect username or password.").build();
        }
    }
	
//	@POST
//	@Path("/users")
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response checkLogins(LoginData data) {
//		LOG.fine("Checking logins...");
//		
//		Query<Entity> query = Query.newEntityQueryBuilder()
//				.setKind("User")
//				.setFilter(PropertyFilter.gt("auth_token", ""))
//				.setLimit(5)
//				.build();
//		QueryResults<Entity> logins = datastore.run(query);
//		
//		List<>
//		
//		return Response.status(Status.OK).entity("{}").build();
//	}

}
