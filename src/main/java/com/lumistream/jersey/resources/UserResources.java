package com.lumistream.jersey.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import com.lumistream.jersey.user.User;
import com.lumistream.jersey.user.UserSupervisor;

import com.google.firebase.auth.FirebaseAuth;


@Path("media/{username}/{pass}")

public class UserResources {

    private final Integer APP1 = 1;
    // private static String URL = "jdbc:sqlite:LumiStream/src/main/databases/user.db";
    private static String URL = "jdbc:sqlite:/src/main/databases/user.db";

    @Path("/addUser")
    @POST
    public void addUser(@PathParam("username") String username, @PathParam("pass") String userpass) {
        User.addUser(username, userpass, URL);
    }

/*    @Path("/login")
    @POST
    public void login(@PathParam("username") String username, @PathParam("pass") String userpass) {

        UserSupervisor.loginUser(username, userpass, APP1, URL);
    }
*/

    @Path("/logout")
    @POST
    public void logout(@PathParam("username") String username, @PathParam("pass") String userpass) {
        UserSupervisor.logoutUser(username, userpass, APP1);
    }

    @Path("/delete")
    @POST
    public void deleteUser(@PathParam("username") String username, @PathParam("pass") String userpass) {
        User.deleteUser(username, URL);
    } 

	

    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(User user) {
        try {
            // validate user credentials
           // UserSupervisor supervisor = UserSupervisor.getInstance();
            boolean isValid = User.Authenticate(user.getUsername(), user.getPassword(), URL);

            if (isValid) {
                // Generate a Firebase custom token
                String customToken = FirebaseAuth.getInstance().createCustomToken(user.getUsername());

                // return token 
                // Invalid 
                return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid username or password").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error during login").build();
        }
    }
}
